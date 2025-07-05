Write-Host "Configuration initiale de Comptel..." -ForegroundColor Cyan

# Vérifier les prérequis
Write-Host "[1/4] Vérification des prérequis..." -ForegroundColor Yellow
$checkPrereqsPath = Join-Path $PSScriptRoot "check-prereqs.ps1"
& $checkPrereqsPath
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
Write-Host "[3/4] Vérification de la branche courante..." -ForegroundColor Yellow
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-Host "Branche courante : $currentBranch" -ForegroundColor Green

# Installer les dépendances backend
Write-Host "[4/4] Installation des dépendances backend..." -ForegroundColor Yellow
$backendPath = (Get-Item (Join-Path $PSScriptRoot "..\backend")).FullName
Set-Location $backendPath
& "mvn" clean install -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de l'installation des dépendances backend. Vérifiez les logs Maven ci-dessus." -ForegroundColor Red
    exit 1
}
Set-Location $PSScriptRoot
Write-Host "Dépendances backend installées avec succès." -ForegroundColor Green

# Installer les dépendances frontend
Write-Host "Installation des dépendances frontend..." -ForegroundColor Yellow
$frontendPath = (Get-Item (Join-Path $PSScriptRoot "..\frontend")).FullName
Set-Location $frontendPath
npm install
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de l'installation des dépendances frontend. Vérifiez les logs npm ci-dessus." -ForegroundColor Red
    exit 1
}
Set-Location $PSScriptRoot
Write-Host "Dépendances frontend installées avec succès." -ForegroundColor Green

Write-Host "\nConfiguration terminée ! Utilisez start.ps1 pour lancer." -ForegroundColor Cyan