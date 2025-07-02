#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# Convert DATABASE_URL from postgresql:// to jdbc:postgresql:// if needed
if [ -n "$DATABASE_URL" ]; then
    if echo "$DATABASE_URL" | grep -q '^postgresql://'; then
        export DATABASE_URL=$(echo "$DATABASE_URL" | sed 's|postgresql://|jdbc:postgresql://|')
        echo "✅ Converted DATABASE_URL to JDBC format"
    fi
    echo "🔧 Using DATABASE_URL: $DATABASE_URL"
else
    echo "⚠️  No DATABASE_URL provided, using individual properties"
fi

echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 