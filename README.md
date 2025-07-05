# Comptel - Application de Gestion de Blanchisserie

[![Tests](https://github.com/pycrafted/comptel/workflows/Tests/badge.svg)](https://github.com/pycrafted/comptel/actions/workflows/tests.yml)
[![Security](https://github.com/pycrafted/comptel/workflows/Backend%20Security%20Scan%20(OWASP%20ZAP)/badge.svg)](https://github.com/pycrafted/comptel/actions/workflows/security-owasp-zap.yml)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 🚀 Démarrage Rapide

### Prérequis
- Java 21
- Node.js 18+
- PostgreSQL 15+
- Docker (optionnel)

### Installation
```bash
# Cloner le projet
git clone https://github.com/pycrafted/comptel.git
cd comptel

# Installation automatique (Windows)
.\scripts\setup.ps1

# Ou installation manuelle
cd backend && mvn clean install -DskipTests
cd ../frontend && npm install
```

### Lancement
```bash
# Lancement automatique (Windows)
.\scripts\start.ps1

# Ou lancement manuel
# Terminal 1 - Backend
cd backend && mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend && npm start
```

### Accès
- **Frontend** : http://localhost:3000
- **Backend** : http://localhost:8080
- **Admin** : admin/admin

## 🧪 Tests

Le projet inclut une suite de tests complète :

### Tests Backend
```bash
cd backend
mvn test
```

### Tests Frontend
```bash
cd frontend
npm test
```

### Tests d'Intégration
```bash
# Vérification de l'utilisateur admin
.\scripts\verify-admin-user.ps1
```

## 📊 Badges de Qualité

Les badges ci-dessus montrent le statut en temps réel :
- 🟢 **Tests** : Tous les tests passent
- 🟢 **Security** : Scan de sécurité réussi
- 🟢 **Build** : Compilation réussie

## 🏗️ Architecture

- **Backend** : Spring Boot 3 + JPA + PostgreSQL
- **Frontend** : React 18 + Material-UI
- **Tests** : JUnit 5 + Mockito + React Testing Library
- **CI/CD** : GitHub Actions

## 📝 Scripts Utiles

| Script | Description |
|--------|-------------|
| `setup.ps1` | Installation complète |
| `start.ps1` | Lancement de l'application |
| `verify-admin-user.ps1` | Vérification admin |
| `docker-start.ps1` | Lancement avec Docker |
| `reset-db.ps1` | Réinitialisation base |

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails. 