@echo off

set KEYCLOAK_IMAGE=my-keycloak
set KEYCLOAK_CONTAINER=keycloak
set KEYCLOAK_REALM=my-realm
set KEYCLOAK_ADMIN_PASSWORD=123456
set ADMIN_USERNAME=power_admin
set USER_USERNAME=user
set KCADM_PATH=/opt/keycloak/bin/kcadm.sh

docker container rm -f %KEYCLOAK_CONTAINER%
docker container rm -f postgresql
docker container rm -f redis
docker image rm -f %KEYCLOAK_IMAGE%

docker run --detach --name postgresql -e "POSTGRES_USER=postgres" -e "POSTGRES_PASSWORD=123456" -e "POSTGRES_DB=springboot4poc" -p 5432:5432 -v postgresql-volume:/var/lib/postgresql/data postgres:18.0-alpine3.22
docker run --detach --name redis -v redis-volume:/data -p 6379:6379 redis:8.2.3-bookworm redis-server --save 60 1 --loglevel warning
docker build -t %KEYCLOAK_IMAGE% my-keycloak
docker run --name %KEYCLOAK_CONTAINER% --detach -p 8080:8080 -p 9000:9000 -e "KC_BOOTSTRAP_ADMIN_USERNAME=admin" -e "KC_BOOTSTRAP_ADMIN_PASSWORD=%KEYCLOAK_ADMIN_PASSWORD%" -e "KC_HEALTH_ENABLED=true" -e "KC_METRICS_ENABLED=true" %KEYCLOAK_IMAGE% start-dev

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
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% config credentials --server http://localhost:8080 --realm master --user admin --password %KEYCLOAK_ADMIN_PASSWORD%

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create realms -s realm=%KEYCLOAK_REALM% -s enabled=true

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create clients -r %KEYCLOAK_REALM% -s clientId=my-client -s enabled=true -s directAccessGrantsEnabled=true -s publicClient=true

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create roles -r %KEYCLOAK_REALM% -s name=admin
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create roles -r %KEYCLOAK_REALM% -s name=user

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create users -r %KEYCLOAK_REALM% -s username=%ADMIN_USERNAME% -s enabled=true -s email=power_user@email.com -s firstName=Power -s lastName=User
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% set-password -r %KEYCLOAK_REALM% --username %ADMIN_USERNAME% --new-password 123456
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% add-roles -r %KEYCLOAK_REALM% --uusername %ADMIN_USERNAME% --rolename admin

echo Created user [%ADMIN_USERNAME%] with password 123456, realm [%KEYCLOAK_REALM%] and role [admin].

docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% create users -r %KEYCLOAK_REALM% -s username=%USER_USERNAME% -s enabled=true -s email=user@email.com -s firstName=Normal -s lastName=User
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% set-password -r %KEYCLOAK_REALM% --username %USER_USERNAME% --new-password 123456
docker exec %KEYCLOAK_CONTAINER% %KCADM_PATH% add-roles -r %KEYCLOAK_REALM% --uusername %USER_USERNAME% --rolename user

echo Created user [%USER_USERNAME%] with password 123456, realm [%KEYCLOAK_REALM%] and role [user].