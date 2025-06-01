Write-Host "Lancement de Comptel (backend + frontend)..." -ForegroundColor Cyan

# Vérifier les prérequis
Write-Host "[1/3] Vérification des prérequis..." -ForegroundColor Yellow
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Échec de la vérification des prérequis. Corrigez les erreurs ci-dessus puis relancez ce script." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Prérequis OK." -ForegroundColor Green
}

# Lancer le backend
Write-Host "[2/3] Lancement du backend..." -ForegroundColor Yellow
Set-Location backend
Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c .\mvnw spring-boot:run"
Start-Sleep -Seconds 5  # Attendre que le backend démarre
# Vérifier si le port 8080 est ouvert (backend lancé)
try {
    $tcp = Test-NetConnection -ComputerName localhost -Port 8080
    if ($tcp.TcpTestSucceeded) {
Write-Host "Backend lancé sur http://localhost:8080" -ForegroundColor Green
    } else {
        Write-Host "Le backend ne répond pas sur le port 8080. Vérifiez les logs du backend dans le dossier 'backend'." -ForegroundColor Red
    }
} catch {
    Write-Host "Impossible de vérifier le port 8080. Vérifiez manuellement si le backend est lancé." -ForegroundColor Yellow
}
Set-Location ..

# Lancer le frontend
Write-Host "[3/3] Lancement du frontend..." -ForegroundColor Yellow
Set-Location frontend
Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c npm start"
Start-Sleep -Seconds 3
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
Set-Location ..

Write-Host "Appuyez sur Ctrl+C pour arrêter..." -ForegroundColor Cyan
pause