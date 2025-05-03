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



## Pour la partie authentification 
**User.java** : Fournit des getters/setters et constructeurs pour gérer les entités utilisateur dans la base de données.

**AccountCredentials.java** : Fournit des getters implicites pour les identifiants envoyés par le client.

**LoginUserController.java** : Gère l'authentification via /login et renvoie un token JWT.

**JwtService.java** : Crée et valide les tokens JWT pour sécuriser les requêtes.

**UseImpl.java**: Charge les détails des utilisateurs pour l'authentification.

**AuthentificationFilter.java** : Valide les tokens JWT dans les requêtes protégées.

**SecurityConfig.java** : Configure les règles de sécurité, CORS, et les composants d'authentification.

