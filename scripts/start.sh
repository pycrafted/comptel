#!/bin/bash
echo "Lancement de Comptel (backend + frontend)..."

# Vérifier les prérequis
./scripts/check-prereqs.sh || exit 1

# Lancer le backend
cd backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Lancer le frontend
cd frontend
npm start &
FRONTEND_PID=$!
cd ..

echo "Backend lancé (PID: $BACKEND_PID) sur http://localhost:8080"
echo "Frontend lancé (PID: $FRONTEND_PID) sur http://localhost:3000"
echo "Appuyez sur Ctrl+C pour arrêter..."

# Attendre la fin des processus
wait $BACKEND_PID $FRONTEND_PID