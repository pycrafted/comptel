Write-Host "Vérification des prérequis pour Comptel..."

# Vérifier Java 21
if (-not (Get-Command java -ErrorAction SilentlyContinue) -or (-not (java -version 2>&1 | Select-String "21."))) {
    Write-Host "Erreur : Java 21 est requis. Installez-le depuis https://adoptium.net/" -ForegroundColor Red
    exit 1
} else {
    $javaVersion = (java -version 2>&1 | Select-Object -First 1)
    Write-Host "Java 21 OK : $javaVersion" -ForegroundColor Green
}

# Vérifier Node.js 16+
if (-not (Get-Command node -ErrorAction SilentlyContinue) -or (-not (node -v | Select-String "v1[6-9]\.|v[2-9][0-9]\."))) {
    Write-Host "Erreur : Node.js 16+ est requis. Installez-le depuis https://nodejs.org/" -ForegroundColor Red
    exit 1
} else {
    $nodeVersion = (node -v)
    Write-Host "Node.js OK : $nodeVersion" -ForegroundColor Green
}

# Vérifier npm
if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    Write-Host "Erreur : npm est requis." -ForegroundColor Red
    exit 1
} else {
    $npmVersion = (npm -v)
    Write-Host "npm OK : $npmVersion" -ForegroundColor Green
}

# Vérifier Docker
if (-not (Get-Command docker -ErrorAction SilentlyContinue) -or (-not (docker info --format '{{.ServerVersion}}' 2>$null))) {
    Write-Host "Erreur : Docker est requis et doit être en cours d'exécution. Installez Docker Desktop." -ForegroundColor Red
    exit 1
} else {
    $dockerVersion = (docker --version)
    Write-Host "Docker OK : $dockerVersion" -ForegroundColor Green
}

Write-Host "Tous les prérequis sont satisfaits !" -ForegroundColor Green