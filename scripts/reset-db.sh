#!/bin/bash
# Script de reset de la base PostgreSQL locale pour Comptel

DB_NAME="comptel"
DB_USER="postgres"
DB_HOST="localhost"

read -sp "Mot de passe PostgreSQL pour l'utilisateur $DB_USER : " PGPASSWORD
export PGPASSWORD

echo -e "\nSuppression de la base $DB_NAME..."
dropdb -U $DB_USER -h $DB_HOST $DB_NAME

echo "Création de la base $DB_NAME..."
createdb -U $DB_USER -h $DB_HOST $DB_NAME

echo "Base $DB_NAME réinitialisée."
unset PGPASSWORD 