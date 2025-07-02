#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# Convert DATABASE_URL from postgresql:// to jdbc:postgresql:// if needed
if [ -n "$DATABASE_URL" ]; then
    if echo "$DATABASE_URL" | grep -q '^postgresql://'; then
        # Convert postgresql:// to jdbc:postgresql://
        export SPRING_DATASOURCE_URL=$(echo "$DATABASE_URL" | sed 's|postgresql://|jdbc:postgresql://|')
        echo "✅ Converted DATABASE_URL to JDBC format"
    else
        # If it's already in JDBC format, use it as is
        export SPRING_DATASOURCE_URL="$DATABASE_URL"
        echo "✅ DATABASE_URL already in JDBC format"
    fi
    echo "🔧 Using SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
else
    echo "⚠️  No DATABASE_URL provided, using default"
fi

echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 