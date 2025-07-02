#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# URL encoding function for special characters (fixed regex escaping)
url_encode() {
    echo "$1" | sed 's/:/%3A/g' | sed 's/@/%40/g' | sed 's/#/%23/g' | sed 's/\$/%24/g' | sed 's/&/%26/g' | sed 's/+/%2B/g' | sed 's/,/%2C/g' | sed 's/;/%3B/g' | sed 's/=/%3D/g' | sed 's/?/%3F/g' | sed 's/ /%20/g'
}

# Check if we have individual database credentials
if [ -n "$SPRING_DATASOURCE_USERNAME" ] && [ -n "$SPRING_DATASOURCE_PASSWORD" ]; then
    echo "✅ Using individual database credentials"
    
    # Extract host and database from SPRING_DATASOURCE_URL if available
    if [ -n "$SPRING_DATASOURCE_URL" ] && echo "$SPRING_DATASOURCE_URL" | grep -q '^postgresql://'; then
        # Extract host and database from the URL
        URL_WITHOUT_PROTOCOL=$(echo "$SPRING_DATASOURCE_URL" | sed 's|postgresql://||')
        HOST_DB_PART=$(echo "$URL_WITHOUT_PROTOCOL" | cut -d'@' -f2-)
        
        echo "🔍 Host/DB part: $HOST_DB_PART"
        
        # URL encode the password
        ENCODED_PASSWORD=$(url_encode "$SPRING_DATASOURCE_PASSWORD")
        
        # Construct JDBC URL
        export SPRING_DATASOURCE_URL="jdbc:postgresql://$SPRING_DATASOURCE_USERNAME:$ENCODED_PASSWORD@$HOST_DB_PART"
        echo "✅ Constructed JDBC URL from individual credentials"
    else
        echo "⚠️  SPRING_DATASOURCE_URL not in expected format, using default"
    fi
else
    echo "⚠️  Individual credentials not available, trying URL conversion"
    
    # Fallback: Convert SPRING_DATASOURCE_URL from postgresql:// to jdbc:postgresql:// if needed
    if [ -n "$SPRING_DATASOURCE_URL" ]; then
        if echo "$SPRING_DATASOURCE_URL" | grep -q '^postgresql://'; then
            echo "🔍 Original URL: $SPRING_DATASOURCE_URL"
            
            # Extract components from the URL
            URL_WITHOUT_PROTOCOL=$(echo "$SPRING_DATASOURCE_URL" | sed 's|postgresql://||')
            
            # Split into credentials and host/database
            CREDENTIALS_PART=$(echo "$URL_WITHOUT_PROTOCOL" | cut -d'@' -f1)
            HOST_DB_PART=$(echo "$URL_WITHOUT_PROTOCOL" | cut -d'@' -f2-)
            
            echo "🔍 Credentials part: $CREDENTIALS_PART"
            echo "🔍 Host/DB part: $HOST_DB_PART"
            
            # Split credentials into username and password
            USERNAME=$(echo "$CREDENTIALS_PART" | cut -d':' -f1)
            PASSWORD=$(echo "$CREDENTIALS_PART" | cut -d':' -f2-)
            
            echo "🔍 Username: $USERNAME"
            echo "🔍 Password: [HIDDEN]"
            
            # URL encode the password if it exists
            if [ -n "$PASSWORD" ]; then
                ENCODED_PASSWORD=$(url_encode "$PASSWORD")
                echo "✅ Password URL encoded"
                export SPRING_DATASOURCE_URL="jdbc:postgresql://$USERNAME:$ENCODED_PASSWORD@$HOST_DB_PART"
            else
                echo "⚠️  No password available"
                export SPRING_DATASOURCE_URL="jdbc:postgresql://$USERNAME@$HOST_DB_PART"
            fi
            
            echo "✅ Converted SPRING_DATASOURCE_URL to JDBC format"
        fi
    else
        echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
    fi
fi

echo "🔧 Using SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
echo "🎯 Starting Spring Boot application..."
exec java -jar app.jar 