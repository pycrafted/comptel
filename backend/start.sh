#!/bin/bash

# Script de démarrage pour convertir l'URL de base de données Render
# Convertit postgresql:// en jdbc:postgresql://

echo "🚀 Starting Comptel Backend..."

# Convert Render's postgresql:// URL to jdbc:postgresql:// format
if [ ! -z "$SPRING_DATASOURCE_URL" ]; then
    if [[ "$SPRING_DATASOURCE_URL" == postgresql://* ]]; then
        export SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL/postgresql:\/\//jdbc:postgresql:\/\/}"
        echo "✅ Converted database URL: $SPRING_DATASOURCE_URL"
    else
        echo "ℹ️  Database URL already in correct format: $SPRING_DATASOURCE_URL"
    fi
else
    echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
fi

# Start the Spring Boot application
echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 