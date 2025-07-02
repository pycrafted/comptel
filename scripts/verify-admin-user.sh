#!/bin/bash

# Script de vérification de l'utilisateur admin
# Usage: ./verify-admin-user.sh <backend-url>

set -e

BACKEND_URL=${1:-"http://localhost:8080"}

echo "🔍 Vérification de l'utilisateur admin sur $BACKEND_URL"

# Attendre que le backend soit prêt
echo "⏳ Attente du démarrage du backend..."
for i in {1..30}; do
    if curl -s -f "$BACKEND_URL/actuator/health" > /dev/null; then
        echo "✅ Backend est prêt!"
        break
    fi
    echo "⏳ Tentative $i/30..."
    sleep 2
done

# Test de connexion avec l'utilisateur admin
echo "🔐 Test de connexion avec admin/admin..."

LOGIN_RESPONSE=$(curl -s -X POST "$BACKEND_URL/login" \
    -H "Content-Type: application/json" \
    -d '{"username": "admin", "password": "admin"}')

if echo "$LOGIN_RESPONSE" | grep -q "token"; then
    echo "✅ Connexion réussie! L'utilisateur admin/admin fonctionne correctement."
    echo "🎉 Votre application est prête à être utilisée!"
    echo ""
    echo "📝 Informations de connexion:"
    echo "   Username: admin"
    echo "   Password: admin"
    echo "   URL Frontend: $FRONTEND_URL"
    echo ""
    echo "⚠️  IMPORTANT: Changez le mot de passe admin après la première connexion!"
else
    echo "❌ Échec de la connexion. Vérifiez les logs du backend."
    echo "Réponse reçue: $LOGIN_RESPONSE"
    exit 1
fi 