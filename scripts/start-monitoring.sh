#!/bin/bash

echo "🚀 Démarrage du système de monitoring Comptel..."

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez installer Docker d'abord."
    exit 1
fi

# Vérifier que Docker Compose est installé
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose n'est pas installé. Veuillez installer Docker Compose d'abord."
    exit 1
fi

# Aller dans le répertoire docker
cd "$(dirname "$0")/../docker"

echo "📦 Construction des images..."
docker-compose build

echo "🔄 Démarrage des services..."
docker-compose up -d

echo "⏳ Attente du démarrage des services..."
sleep 30

echo "🔍 Vérification des services..."

# Vérifier le backend
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend Spring Boot: http://localhost:8080"
    echo "📊 Métriques Prometheus: http://localhost:8080/actuator/prometheus"
else
    echo "❌ Backend Spring Boot n'est pas accessible"
fi

# Vérifier Prometheus
if curl -f http://localhost:9090/-/healthy > /dev/null 2>&1; then
    echo "✅ Prometheus: http://localhost:9090"
else
    echo "❌ Prometheus n'est pas accessible"
fi

# Vérifier Grafana
if curl -f http://localhost:3001/api/health > /dev/null 2>&1; then
    echo "✅ Grafana: http://localhost:3001"
    echo "👤 Identifiants Grafana: admin/admin"
else
    echo "❌ Grafana n'est pas accessible"
fi

echo ""
echo "🎉 Système de monitoring démarré !"
echo ""
echo "📋 URLs importantes:"
echo "   • Application: http://localhost:3000"
echo "   • Backend API: http://localhost:8080"
echo "   • Prometheus: http://localhost:9090"
echo "   • Grafana: http://localhost:3001 (admin/admin)"
echo ""
echo "📊 Pour voir les métriques:"
echo "   1. Ouvrez Grafana: http://localhost:3001"
echo "   2. Connectez-vous avec admin/admin"
echo "   3. Le dashboard 'Comptel Application Dashboard' devrait être disponible"
echo ""
echo "🛑 Pour arrêter: docker-compose down" 