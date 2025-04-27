#!/bin/bash
echo "Configuration initiale de Comptel..."

# Vérifier les prérequis
./scripts/check-prereqs.sh || exit 1

# Cloner le dépôt si non présent
if [ ! -d ".git" ]; then
    git clone git@github.com:pycrafted/comptel.git .
fi

# Mettre à jour les branches
git checkout feature/authentification-backend
git pull origin feature/authentification-backend
git checkout feature/authentification-frontend
git pull origin feature/authentification-frontend
git checkout feature/authentification
git pull origin feature/authentification

# Installer les dépendances backend
cd backend
./mvnw clean install
cd ..

# Installer les dépendances frontend
cd frontend
npm install
cd ..

echo "Configuration terminée ! Utilisez start.sh ou start.ps1 pour lancer."