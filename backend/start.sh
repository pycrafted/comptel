#!/bin/sh

echo "🚀 Starting Comptel Backend..."

# URL encoding function for special characters (fixed regex escaping)
url_encode() {
    echo "$1" | sed 's/:/%3A/g' | sed 's/@/%40/g' | sed 's/#/%23/g' | sed 's/\$/%24/g' | sed 's/&/%26/g' | sed 's/+/%2B/g' | sed 's/,/%2C/g' | sed 's/;/%3B/g' | sed 's/=/%3D/g' | sed 's/?/%3F/g' | sed 's/ /%20/g'
}

# Function to convert internal hostname to external
convert_to_external_hostname() {
    local host_db_part="$1"
    # Extract hostname and database
    local hostname=$(echo "$host_db_part" | cut -d'/' -f1)
    local database=$(echo "$host_db_part" | cut -d'/' -f2)
    
    # Convert hostname to external format
    local external_hostname="${hostname}.oregon-postgres.render.com"
    
    echo "${external_hostname}/${database}"
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
        
        # Convert internal hostname to external hostname
        EXTERNAL_HOST_DB_PART=$(convert_to_external_hostname "$HOST_DB_PART")
        
        echo "🔍 External Host/DB part: $EXTERNAL_HOST_DB_PART"
        
        # Extract hostname and database from external part
        EXTERNAL_HOSTNAME=$(echo "$EXTERNAL_HOST_DB_PART" | cut -d'/' -f1)
        DATABASE=$(echo "$EXTERNAL_HOST_DB_PART" | cut -d'/' -f2)
        
        # URL encode the password
        ENCODED_PASSWORD=$(url_encode "$SPRING_DATASOURCE_PASSWORD")
        ENCODED_USERNAME=$(url_encode "$SPRING_DATASOURCE_USERNAME")
        
        # Construct JDBC URL with external hostname and credentials as query parameters
        export SPRING_DATASOURCE_URL="jdbc:postgresql://${EXTERNAL_HOSTNAME}:5432/${DATABASE}?user=${ENCODED_USERNAME}&password=${ENCODED_PASSWORD}"
        echo "✅ Constructed JDBC URL from individual credentials with external hostname"
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
            
            # Convert internal hostname to external hostname
            EXTERNAL_HOST_DB_PART=$(convert_to_external_hostname "$HOST_DB_PART")
            echo "🔍 External Host/DB part: $EXTERNAL_HOST_DB_PART"
            
            # Extract hostname and database from external part
            EXTERNAL_HOSTNAME=$(echo "$EXTERNAL_HOST_DB_PART" | cut -d'/' -f1)
            DATABASE=$(echo "$EXTERNAL_HOST_DB_PART" | cut -d'/' -f2)
            
            # Split credentials into username and password
            USERNAME=$(echo "$CREDENTIALS_PART" | cut -d':' -f1)
            PASSWORD=$(echo "$CREDENTIALS_PART" | cut -d':' -f2-)
            
            echo "🔍 Username: $USERNAME"
            echo "🔍 Password: [HIDDEN]"
            
            # URL encode the credentials if they exist
            if [ -n "$PASSWORD" ]; then
                ENCODED_PASSWORD=$(url_encode "$PASSWORD")
                ENCODED_USERNAME=$(url_encode "$USERNAME")
                echo "✅ Credentials URL encoded"
                export SPRING_DATASOURCE_URL="jdbc:postgresql://${EXTERNAL_HOSTNAME}:5432/${DATABASE}?user=${ENCODED_USERNAME}&password=${ENCODED_PASSWORD}"
            else
                echo "⚠️  No password available"
                ENCODED_USERNAME=$(url_encode "$USERNAME")
                export SPRING_DATASOURCE_URL="jdbc:postgresql://${EXTERNAL_HOSTNAME}:5432/${DATABASE}?user=${ENCODED_USERNAME}"
            fi
            
            echo "✅ Converted SPRING_DATASOURCE_URL to JDBC format with external hostname"
        fi
    else
        echo "⚠️  No SPRING_DATASOURCE_URL provided, using default"
    fi
fi

echo "🔧 Using SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
echo "⚡ Starting Spring Boot application with performance optimizations..."
echo "🔧 JVM Options: $JAVA_OPTS"

# Start with performance optimizations
exec java $JAVA_OPTS -jar app.jar 

# Start with performance optimizations
exec java $JAVA_OPTS -jar app.jar 