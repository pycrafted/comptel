#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# URL encoding function for special characters
url_encode() {
    echo "$1" | sed 's/:/%3A/g' | sed 's/@/%40/g' | sed 's/#/%23/g' | sed 's/\$/%24/g' | sed 's/&/%26/g' | sed 's/+/%2B/g' | sed 's/,/%2C/g' | sed 's/;/%3B/g' | sed 's/=/%3D/g' | sed 's/\?/%3F/g' | sed 's/ /%20/g'
}

# Convert SPRING_DATASOURCE_URL from postgresql:// to jdbc:postgresql:// if needed
if [ -n "$SPRING_DATASOURCE_URL" ]; then
    if echo "$SPRING_DATASOURCE_URL" | grep -q '^postgresql://'; then
        # Extract components from the URL
        URL_WITHOUT_PROTOCOL=$(echo "$SPRING_DATASOURCE_URL" | sed 's|postgresql://||')
        
        # Split into credentials and host/database
        CREDENTIALS_PART=$(echo "$URL_WITHOUT_PROTOCOL" | cut -d'@' -f1)
        HOST_DB_PART=$(echo "$URL_WITHOUT_PROTOCOL" | cut -d'@' -f2-)
        
        # Split credentials into username and password
        USERNAME=$(echo "$CREDENTIALS_PART" | cut -d':' -f1)
        PASSWORD=$(echo "$CREDENTIALS_PART" | cut -d':' -f2-)
        
        # URL encode the password
        ENCODED_PASSWORD=$(url_encode "$PASSWORD")
        
        # Reconstruct the URL with encoded password
        export SPRING_DATASOURCE_URL="jdbc:postgresql://$USERNAME:$ENCODED_PASSWORD@$HOST_DB_PART"
        
        echo "✅ Converted SPRING_DATASOURCE_URL to JDBC format with URL-encoded password"
    fi
    echo "🔧 Using SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
else
    echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
fi

echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 