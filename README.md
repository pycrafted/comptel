# Comptel

Application de comptabilité avec un backend en Spring Boot et un frontend en React.

## Structure du projet
- `backend/` : Code source du backend (Spring Boot).
- `frontend/` : Code source du frontend (React).
- `docker/` : Fichiers de configuration Docker.
- `docs/` : Documentation du projet.

## Prérequis
- Java 21
- Node.js 16+
- Docker

## Installation
(À compléter ultérieurement)

## Lancement avec Docker
1. Assurez-vous que Docker est installé.
2. Depuis le dossier `docker/` :
   ```bash
   docker-compose up --build


## Scripts d’automatisation
Les scripts dans `scripts/` facilitent la configuration et le lancement :

- **Vérifier les prérequis** :
  - Linux/Mac : `./scripts/check-prereqs.sh`
  - Windows : `.\scripts\check-prereqs.ps1`
- **Configurer le projet** (clone, dépendances) :
  - Linux/Mac : `./scripts/setup.sh`
  - Windows : `.\scripts\setup.ps1`
- **Lancer localement** (backend + frontend) :
  - Linux/Mac : `./scripts/start.sh`
  - Windows : `.\scripts\start.ps1`
- **Lancer avec Docker** :
  - Linux/Mac : `./scripts/docker-start.sh`
  - Windows : `.\scripts\docker-start.ps1`