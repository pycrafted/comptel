#!/bin/sh

# Script de démarrage pour convertir l'URL de base de données Render
# Convertit postgresql:// en jdbc:postgresql://

echo "🚀 Starting Comptel Backend..."

# Convert Render's postgresql:// URL to jdbc:postgresql:// format
if [ -n "$SPRING_DATASOURCE_URL" ]; then
    case "$SPRING_DATASOURCE_URL" in
        postgresql://*)
            SPRING_DATASOURCE_URL=$(echo "$SPRING_DATASOURCE_URL" | sed 's|postgresql://|jdbc:postgresql://|')
            export SPRING_DATASOURCE_URL
            echo "✅ Converted database URL: $SPRING_DATASOURCE_URL"
            ;;
        *)
            echo "ℹ️  Database URL already in correct format: $SPRING_DATASOURCE_URL"
            ;;
    esac
else
    echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
fi

# Start the Spring Boot application
echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 