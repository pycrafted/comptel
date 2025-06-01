#!/bin/bash
# Script pour générer des données de test dans la base Comptel

API_URL="http://localhost:8080/api/dev/populate"

echo "Appel à $API_URL pour générer les données de test..."
curl -X POST $API_URL

echo "Données de test générées (si l'endpoint existe côté backend)." 