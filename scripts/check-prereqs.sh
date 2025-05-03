#!/bin/bash
echo "Vérification des prérequis pour Comptel..."

# Vérifier Java 21
if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "21."; then
    echo "Erreur : Java 21 est requis. Installez-le depuis https://adoptium.net/"
    exit 1
else
    echo "Java 21 OK : $(java -version 2>&1 | head -n 1)"
fi

# Vérifier Node.js 16+
if ! command -v node &> /dev/null || ! node -v | grep -qE "v1[6-9]\.|v[2-9][0-9]\."; then
    echo "Erreur : Node.js 16+ est requis. Installez-le depuis https://nodejs.org/"
    exit 1
else
    echo "Node.js OK : $(node -v)"
fi

# Vérifier npm
if ! command -v npm &> /dev/null; then
    echo "Erreur : npm est requis."
    exit 1
else
    echo "npm OK : $(npm -v)"
fi

# Vérifier Docker
if ! command -v docker &> /dev/null || ! docker info &> /dev/null; then
    echo "Erreur : Docker est requis et doit être en cours d'exécution. Installez Docker Desktop."
    exit 1
else
    echo "Docker OK : $(docker --version)"
fi

echo "Tous les prérequis sont satisfaits !"