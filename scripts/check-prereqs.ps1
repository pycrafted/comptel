Write-Host "Vérification des prérequis pour Comptel..." -ForegroundColor Cyan

# Vérifier Java 21
Write-Host "- Vérification de Java 21..." -ForegroundColor Yellow
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java n'est pas installé ou non trouvé dans le PATH." -ForegroundColor Red
    Write-Host "Téléchargez Java 21 ici : https://adoptium.net/" -ForegroundColor Yellow
    exit 1
} elseif (-not (java -version 2>&1 | Select-String "21.")) {
    Write-Host "Java trouvé, mais la version n'est pas 21. Installez Java 21 depuis https://adoptium.net/" -ForegroundColor Red
    exit 1
} else {
    $javaVersion = (java -version 2>&1 | Select-Object -First 1)
    Write-Host "Java 21 OK : $javaVersion" -ForegroundColor Green
}

# Vérifier Node.js 16+
Write-Host "- Vérification de Node.js 16+..." -ForegroundColor Yellow
if (-not (Get-Command node -ErrorAction SilentlyContinue)) {
    Write-Host "Node.js n'est pas installé ou non trouvé dans le PATH." -ForegroundColor Red
    Write-Host "Téléchargez Node.js 16+ ici : https://nodejs.org/" -ForegroundColor Yellow
    exit 1
} elseif (-not (node -v | Select-String "v1[6-9]\\.|v[2-9][0-9]\\.")) {
    Write-Host "Node.js trouvé, mais la version est trop ancienne. Installez Node.js 16 ou supérieur." -ForegroundColor Red
    exit 1
} else {
    $nodeVersion = (node -v)
    Write-Host "Node.js OK : $nodeVersion" -ForegroundColor Green
}

# Vérifier npm
Write-Host "- Vérification de npm..." -ForegroundColor Yellow
if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    Write-Host "npm n'est pas installé ou non trouvé dans le PATH." -ForegroundColor Red
    Write-Host "npm est inclus avec Node.js. Réinstallez Node.js si besoin." -ForegroundColor Yellow
    exit 1
} else {
    $npmVersion = (npm -v)
    Write-Host "npm OK : $npmVersion" -ForegroundColor Green
}

# Vérifier Docker
Write-Host "- Vérification de Docker..." -ForegroundColor Yellow
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "Docker n'est pas installé ou non trouvé dans le PATH." -ForegroundColor Red
    Write-Host "Téléchargez Docker Desktop ici : https://www.docker.com/products/docker-desktop/" -ForegroundColor Yellow
    exit 1
} elseif (-not (docker info --format '{{.ServerVersion}}' 2>$null)) {
    Write-Host "Docker est installé mais ne semble pas démarré. Lancez Docker Desktop puis réessayez." -ForegroundColor Red
    exit 1
} else {
    $dockerVersion = (docker --version)
    Write-Host "Docker OK : $dockerVersion" -ForegroundColor Green
}

Write-Host "\nTous les prérequis sont satisfaits !" -ForegroundColor Cyan