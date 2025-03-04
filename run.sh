#!/bin/bash

echo -e "\n\033[1;34m🚀 Démarrage du build...\033[0m"

cd backend || { echo -e "\n\033[1;31m❌ Erreur : Impossible d'accéder au dossier backend !\033[0m"; exit 1; }

MYSQL_CONTAINER=$(docker-compose ps -q mysql)
if [ -n "$MYSQL_CONTAINER" ] && [ "$(docker inspect -f '{{.State.Running}}' $MYSQL_CONTAINER)" == "true" ]; then
    echo -e "\n\033[1;32m✅ MySQL est déjà en cours d'exécution !\033[0m"
else
    echo -e "\n\033[1;33m📦 Démarrage de MySQL...\033[0m"
    docker-compose up -d mysql
fi

echo -e "\033[1;36m⏳ Vérification de l'état de MySQL...\033[0m"
until docker exec $(docker-compose ps -q mysql) mysqladmin ping -h "mysql" --silent; do
    echo -e "\033[1;36m⌛ MySQL n'est pas encore prêt, attente de 2 secondes...\033[0m"
    sleep 2
done
echo -e "\n\033[1;32m✅ MySQL est prêt !\033[0m"

echo -e "\n\033[1;33m🛠 Exécution de mvn clean package...\033[0m"
mvn clean package
if [ $? -ne 0 ]; then
    echo -e "\033[1;31m❌ Erreur : Échec du build Maven !\033[0m"
    exit 1
fi
echo -e "\n\033[1;32m✅ Build Maven réussi !\033[0m"

cd .. || { echo -e "\033[1;31m❌ Erreur : Impossible de revenir au dossier principal !\033[0m"; exit 1; }

echo -e "\n\033[1;33m🚀 Démarrage de tous les services Docker...\033[0m"
docker-compose up -d backend
