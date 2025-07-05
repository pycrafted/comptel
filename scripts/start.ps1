Write-Host "Lancement de Comptel (backend + frontend)..." -ForegroundColor Cyan

# Vérifier les prérequis
Write-Host "[1/3] Vérification des prérequis..." -ForegroundColor Yellow
$checkPrereqsPath = Join-Path $PSScriptRoot "check-prereqs.ps1"
& $checkPrereqsPath
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de la vérification des prérequis. Corrigez les erreurs ci-dessus puis relancez ce script." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Prérequis OK." -ForegroundColor Green
}

# Lancer le backend
Write-Host "[2/3] Lancement du backend..." -ForegroundColor Yellow
$backendPath = (Get-Item (Join-Path $PSScriptRoot "..\backend")).FullName
if (!(Test-Path $backendPath)) {
    Write-Host "ERREUR : Le dossier backend est introuvable" -ForegroundColor Red
    exit 1
}
Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c mvn spring-boot:run" -WorkingDirectory $backendPath
Write-Host "Attente du démarrage du backend..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Vérifier si le port 8080 est ouvert (backend lancé)
$maxAttempts = 5
$attempt = 0
do {
    $attempt++
    Write-Host "Tentative $attempt/$maxAttempts de vérification du backend..." -ForegroundColor Yellow
    try {
        $tcp = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet
        if ($tcp) {
            Write-Host "Backend lancé sur http://localhost:8080" -ForegroundColor Green
            break
        } else {
            if ($attempt -lt $maxAttempts) {
                Write-Host "Backend pas encore prêt, nouvelle tentative dans 3 secondes..." -ForegroundColor Yellow
                Start-Sleep -Seconds 3
            } else {
                Write-Host "Le backend ne répond pas sur le port 8080 après $maxAttempts tentatives." -ForegroundColor Red
            }
        }
    } catch {
        if ($attempt -lt $maxAttempts) {
            Write-Host "Erreur de connexion, nouvelle tentative dans 3 secondes..." -ForegroundColor Yellow
            Start-Sleep -Seconds 3
        } else {
            Write-Host "Impossible de vérifier le port 8080 après $maxAttempts tentatives." -ForegroundColor Red
        }
    }
} while ($attempt -lt $maxAttempts -and -not $tcp)

# Lancer le frontend
Write-Host "[3/3] Lancement du frontend..." -ForegroundColor Yellow
$frontendPath = Join-Path $PSScriptRoot "..\frontend"
Set-Location $frontendPath
if (!(Test-Path "node_modules")) {
    Write-Host "Installation des dépendances frontend..." -ForegroundColor Yellow
    npm install
}
Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c npm start" -WorkingDirectory $frontendPath
Start-Sleep -Seconds 5
Set-Location $PSScriptRoot

# Vérifier si le port 3000 est ouvert (frontend lancé)
try {
    $tcp = Test-NetConnection -ComputerName localhost -Port 3000
    if ($tcp.TcpTestSucceeded) {
        Write-Host "Frontend lancé sur http://localhost:3000" -ForegroundColor Green
    } else {
        Write-Host "Le frontend ne répond pas sur le port 3000. Vérifiez les logs du frontend dans le dossier 'frontend'." -ForegroundColor Red
    }
} catch {
    Write-Host "Impossible de vérifier le port 3000. Vérifiez manuellement si le frontend est lancé." -ForegroundColor Yellow
}

Write-Host "Appuyez sur Ctrl+C pour arrêter..." -ForegroundColor Cyan
pause