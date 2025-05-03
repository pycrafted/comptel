Write-Host "Configuration initiale de Comptel..."

# Vérifier les prérequis
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) { exit 1 }

# Cloner le dépôt si non présent
if (-not (Test-Path ".git")) {
    git clone git@github.com:pycrafted/comptel.git .
}

# Mettre à jour les branches
git checkout feature/authentification-backend
git pull origin feature/authentification-backend
git checkout feature/authentification-frontend
git pull origin feature/authentification-frontend
git checkout feature/authentification
git pull origin feature/authentification

# Installer les dépendances backend
Set-Location backend
.\mvnw clean install
Set-Location ..

# Installer les dépendances frontend
Set-Location frontend
npm install
Set-Location ..

Write-Host "Configuration terminée ! Utilisez start.ps1 pour lancer." -ForegroundColor Green