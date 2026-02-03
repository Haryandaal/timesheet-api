#!/bin/bash

# Check if description provided
if [ -z "$1" ]; then
    echo "Usage: ./create-migration.sh <migration_description>"
    echo "Example: ./create-migration.sh create_users_table"
    exit 1
fi

# Generate timestamp
TIMESTAMP=$(date +%Y%m%d%H%M%S)

# Get description and replace spaces with underscores
DESCRIPTION=$(echo "$1" | tr ' ' '_' | tr '[:upper:]' '[:lower:]')

# Create filename
FILENAME="V${TIMESTAMP}__${DESCRIPTION}.sql"

# Path to migration folder
MIGRATION_PATH="src/main/resources/db/migration"

# Create migration file
FILEPATH="${MIGRATION_PATH}/${FILENAME}"

# Create file with template
cat > "$FILEPATH" << EOF
-- Migration: ${DESCRIPTION}
-- Created: $(date '+%Y-%m-%d %H:%M:%S')
-- Author: $(git config user.name)

-- Write your migration SQL here

EOF

echo "✅ Migration created: ${FILENAME}"
echo "📁 Location: ${FILEPATH}"