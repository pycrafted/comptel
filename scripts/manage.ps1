Write-Host "==== Gestion Comptel (installation, vérification, lancement) ====" -ForegroundColor Cyan

# 1. Vérification des prérequis
Write-Host "\n[1/4] Vérification des prérequis..." -ForegroundColor Yellow
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de la vérification des prérequis. Corrigez les erreurs ci-dessus puis relancez ce script." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Prérequis OK." -ForegroundColor Green
}

# 2. Vérification de l'installation
$backendBuilt = Test-Path "backend\target"
$frontendInstalled = Test-Path "frontend\node_modules"

if ($backendBuilt -and $frontendInstalled) {
    Write-Host "\n[2/4] Dépendances déjà installées (backend et frontend)." -ForegroundColor Green
    $installNeeded = $false
} else {
    Write-Host "\n[2/4] Dépendances manquantes :" -ForegroundColor Yellow
    if (-not $backendBuilt) { Write-Host "- Backend non compilé." -ForegroundColor Red }
    if (-not $frontendInstalled) { Write-Host "- Frontend non installé." -ForegroundColor Red }
    $installNeeded = $true
}

if ($installNeeded) {
    $install = Read-Host "Voulez-vous installer les dépendances maintenant ? (o/n)"
    if ($install -eq 'o' -or $install -eq 'O') {
        # Installation backend
        Write-Host "\n[3/4] Installation des dépendances backend..." -ForegroundColor Yellow
        Set-Location backend
        .\mvnw clean install
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Échec de l'installation des dépendances backend. Vérifiez les logs Maven ci-dessus." -ForegroundColor Red
            exit 1
        }
        Set-Location ..
        Write-Host "Dépendances backend installées avec succès." -ForegroundColor Green
        # Installation frontend
        Write-Host "Installation des dépendances frontend..." -ForegroundColor Yellow
        Set-Location frontend
        npm install
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Échec de l'installation des dépendances frontend. Vérifiez les logs npm ci-dessus." -ForegroundColor Red
            exit 1
        }
        Set-Location ..
        Write-Host "Dépendances frontend installées avec succès." -ForegroundColor Green
    } else {
        Write-Host "Installation annulée. Vous pouvez relancer ce script plus tard." -ForegroundColor Yellow
        exit 0
    }
}

# 3. Lancement de l'application
$run = Read-Host "Voulez-vous lancer l'application maintenant ? (o/n)"
if ($run -eq 'o' -or $run -eq 'O') {
    Write-Host "\n[4/4] Lancement de Comptel (backend + frontend)..." -ForegroundColor Yellow
    # Lancer le backend
    Set-Location backend
    Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c .\mvnw spring-boot:run"
    Start-Sleep -Seconds 5
    Set-Location ..
    # Lancer le frontend
    Set-Location frontend
    Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c npm start"
    Start-Sleep -Seconds 3
    Set-Location ..
    Write-Host "\nBackend lancé sur http://localhost:8080" -ForegroundColor Green
    Write-Host "Frontend lancé sur http://localhost:3000" -ForegroundColor Green
    Write-Host "Appuyez sur Ctrl+C pour arrêter..." -ForegroundColor Cyan
    pause
} else {
    Write-Host "Lancement annulé. Vous pouvez relancer ce script plus tard pour démarrer l'application." -ForegroundColor Yellow
} 