#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# Convert SPRING_DATASOURCE_URL from postgresql:// to jdbc:postgresql:// if needed
if [ -n "$SPRING_DATASOURCE_URL" ]; then
    if echo "$SPRING_DATASOURCE_URL" | grep -q '^postgresql://'; then
        export SPRING_DATASOURCE_URL=$(echo "$SPRING_DATASOURCE_URL" | sed 's|postgresql://|jdbc:postgresql://|')
        echo "✅ Converted SPRING_DATASOURCE_URL to JDBC format"
    fi
    echo "🔧 Using SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
else
    echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
fi

echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 