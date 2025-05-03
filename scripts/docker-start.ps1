Write-Host "Lancement de Comptel avec Docker..."

# Vérifier les prérequis
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) { exit 1 }

# Lancer Docker Compose
Set-Location docker
docker-compose up --build