Write-Host "Configuration initiale de Comptel..." -ForegroundColor Cyan

# Vérifier les prérequis
Write-Host "[1/4] Vérification des prérequis..." -ForegroundColor Yellow
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de la vérification des prérequis. Corrigez les erreurs ci-dessus puis relancez ce script." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Prérequis OK." -ForegroundColor Green
}

# Cloner le dépôt si non présent
Write-Host "[2/4] Vérification du dépôt git..." -ForegroundColor Yellow
if (-not (Test-Path ".git")) {
    Write-Host "Dépôt git absent, tentative de clonage..." -ForegroundColor Yellow
    git clone git@github.com:pycrafted/comptel.git .
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Échec du clonage du dépôt. Vérifiez votre connexion internet et vos accès SSH/Git." -ForegroundColor Red
        exit 1
    }
    Write-Host "Dépôt cloné avec succès." -ForegroundColor Green
} else {
    Write-Host "Dépôt git déjà présent." -ForegroundColor Green
}

# Mettre à jour les branches
Write-Host "[3/4] Mise à jour des branches..." -ForegroundColor Yellow
$branches = @("feature/authentification-backend", "feature/authentification-frontend", "feature/authentification")
foreach ($branch in $branches) {
    git checkout $branch
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Échec du checkout de la branche $branch. Vérifiez que la branche existe." -ForegroundColor Red
        exit 1
    }
    git pull origin $branch
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Échec du pull sur la branche $branch. Vérifiez votre connexion et vos droits." -ForegroundColor Red
        exit 1
    }
    Write-Host "Branche $branch à jour." -ForegroundColor Green
}

# Installer les dépendances backend
Write-Host "[4/4] Installation des dépendances backend..." -ForegroundColor Yellow
Set-Location backend
.\mvnw clean install
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de l'installation des dépendances backend. Vérifiez les logs Maven ci-dessus." -ForegroundColor Red
    exit 1
}
Set-Location ..
Write-Host "Dépendances backend installées avec succès." -ForegroundColor Green

# Installer les dépendances frontend
Write-Host "Installation des dépendances frontend..." -ForegroundColor Yellow
Set-Location frontend
npm install
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de l'installation des dépendances frontend. Vérifiez les logs npm ci-dessus." -ForegroundColor Red
    exit 1
}
Set-Location ..
Write-Host "Dépendances frontend installées avec succès." -ForegroundColor Green

Write-Host "\nConfiguration terminée ! Utilisez start.ps1 pour lancer." -ForegroundColor Cyan