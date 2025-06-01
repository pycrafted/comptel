# Script PowerShell pour générer des données de test dans la base Comptel

$API_URL = "http://localhost:8080/api/dev/populate"

Write-Host "Appel à $API_URL pour générer les données de test..."
Invoke-RestMethod -Uri $API_URL -Method Post

Write-Host "Données de test générées (si l'endpoint existe côté backend)." 