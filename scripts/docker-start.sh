#!/bin/bash
echo "Lancement de Comptel avec Docker..."

# Vérifier les prérequis
./scripts/check-prereqs.sh || exit 1

# Lancer Docker Compose
cd docker
docker-compose up --build