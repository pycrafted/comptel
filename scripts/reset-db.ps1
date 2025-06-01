# Script PowerShell de reset de la base PostgreSQL locale pour Comptel

$DB_NAME = "comptel"
$DB_USER = "postgres"
$DB_HOST = "localhost"

$PGPASSWORD = Read-Host -AsSecureString "Mot de passe PostgreSQL pour l'utilisateur $DB_USER"
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($PGPASSWORD)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host "Suppression de la base $DB_NAME..."
$env:PGPASSWORD = $plainPassword
& dropdb -U $DB_USER -h $DB_HOST $DB_NAME

Write-Host "Création de la base $DB_NAME..."
& createdb -U $DB_USER -h $DB_HOST $DB_NAME

Write-Host "Base $DB_NAME réinitialisée."
Remove-Item Env:PGPASSWORD 