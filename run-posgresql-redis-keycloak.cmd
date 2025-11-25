@echo off
setlocal EnableDelayedExpansion

set KEYCLOAK_IMAGE=my-keycloak
set KEYCLOAK_CONTAINER=keycloak
set KEYCLOAK_REALM=my-realm
set KEYCLOAK_ADMIN_PASSWORD=123456
set ADMIN_USERNAME=power_admin
set USER_USERNAME=user
set CLIENT_ID=my-client
set KCADM_PATH=/opt/keycloak/bin/kcadm.sh

echo Stopping and removing old containers/images...
docker container rm -f %KEYCLOAK_CONTAINER% 2>nul
docker container rm -f postgresql 2>nul
docker container rm -f redis 2>nul
docker image rm -f %KEYCLOAK_IMAGE% 2>nul

echo Starting PostgreSQL...
docker run --detach --name postgresql -e "POSTGRES_USER=postgres" -e "POSTGRES_PASSWORD=123456" -e "POSTGRES_DB=springboot4poc" -p 5432:5432 -v postgresql-volume:/var/lib/postgresql/data postgres:18.0-alpine3.22

echo Starting Redis...
docker run --detach --name redis -v redis-volume:/data -p 6379:6379 redis:8.2.3-bookworm redis-server --save 60 1 --loglevel warning

echo Building custom Keycloak image...
docker build -t %KEYCLOAK_IMAGE% my-keycloak

echo Starting Keycloak...
docker run --name %KEYCLOAK_CONTAINER% --detach -p 8080:8080 -p 9000:9000 ^
    -e "KC_BOOTSTRAP_ADMIN_USERNAME=admin" ^
    -e "KC_BOOTSTRAP_ADMIN_PASSWORD=%KEYCLOAK_ADMIN_PASSWORD%" ^
    -e "KC_HEALTH_ENABLED=true" ^
    -e "KC_METRICS_ENABLED=true" ^
    %KEYCLOAK_IMAGE% start-dev

:loop
docker exec -it %KEYCLOAK_CONTAINER% java HealthCheck.java
if %ERRORLEVEL%==0 (
    echo Keycloak is serviceable!
    goto after_health_check
) else (
    echo Waiting for Keycloak to be healthy...
    timeout /t 5 >nul
    goto loop
)

:after_health_check

echo Configuring kcadm...
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% config credentials --server http://localhost:8080 --realm master --user admin --password %KEYCLOAK_ADMIN_PASSWORD%

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create realms -s realm=%KEYCLOAK_REALM% -s enabled=true

echo Creating client %CLIENT_ID%...

:: if anyone has any better idea to not use variable, let me know
set "CLIENT_CREATE_CMD=docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create clients -r %KEYCLOAK_REALM% -s clientId=%CLIENT_ID% -s enabled=true -s publicClient=true -s directAccessGrantsEnabled=true -i"

for /f "usebackq delims=" %%i in (`!CLIENT_CREATE_CMD!`) do (
    set CLIENT_UUID=%%i
)

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create clients/%CLIENT_UUID%/roles -r %KEYCLOAK_REALM% -s name=ADMIN
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create clients/%CLIENT_UUID%/roles -r %KEYCLOAK_REALM% -s name=USER

echo Creating admin user [%ADMIN_USERNAME%]...
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create users -r %KEYCLOAK_REALM% -s username=%ADMIN_USERNAME% -s enabled=true -s email=power_user@email.com -s firstName=Power -s lastName=User
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% set-password -r %KEYCLOAK_REALM% --username %ADMIN_USERNAME% --new-password 123456

echo Assigning client role ADMIN to %ADMIN_USERNAME%...
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% add-roles -r %KEYCLOAK_REALM% --uusername %ADMIN_USERNAME% --cclientid %CLIENT_ID% --rolename ADMIN
echo Created user [%ADMIN_USERNAME%] with password 123456 and client role [ADMIN] in client [%CLIENT_ID%]

echo Creating regular user [%USER_USERNAME%]...
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create users -r %KEYCLOAK_REALM% -s username=%USER_USERNAME% -s enabled=true -s email=user@email.com -s firstName=Normal -s lastName=User
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% set-password -r %KEYCLOAK_REALM% --username %USER_USERNAME% --new-password 123456

echo Assigning client role USER to %USER_USERNAME%...
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% add-roles -r %KEYCLOAK_REALM% --uusername %USER_USERNAME% --cclientid %CLIENT_ID% --rolename USER
echo Created user [%USER_USERNAME%] with password 123456 and client role [USER] in client [%CLIENT_ID%]