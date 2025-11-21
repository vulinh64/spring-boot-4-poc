@echo off

docker run --detach --name postgresql -e "POSTGRES_USER=postgres" -e "POSTGRES_PASSWORD=123456" -e "POSTGRES_DB=springboot4poc" -p 5432:5432 -v postgresql-volume:/var/lib/postgresql/data postgres:alpine
docker run --detach --name redis -v redis-volume:/data -p 6379:6379 redis:alpine redis-server --save 60 1 --loglevel warning
docker run --name keycloak --detach -p 8080:8080 -p 9000:9000 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=123456 -e KC_HEALTH_ENABLED=true -e KC_METRICS_ENABLED=true quay.io/keycloak/keycloak:latest start-dev