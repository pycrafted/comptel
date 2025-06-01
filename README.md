# Comptel

[![Backend CI](https://github.com/pycrafted/comptel/actions/workflows/backend-ci.yml/badge.svg?branch=develop)](https://github.com/pycrafted/comptel/actions/workflows/backend-ci.yml)
[![codecov](https://codecov.io/gh/pycrafted/comptel/branch/develop/graph/badge.svg)](https://codecov.io/gh/pycrafted/comptel)

---

# 🌟 Comptel – Plateforme de gestion comptable moderne

Comptel est une application de gestion financière complète, pensée pour la simplicité, la sécurité et la performance.

- **Backend** : Spring Boot (Java 21)
- **Frontend** : React
- **Base de données** : PostgreSQL
- **Orchestration** : Docker Compose
- **CI/CD & Monitoring** : GitHub Actions, Prometheus, Grafana, ELK, Codecov

---

## 🚀 Guide d'utilisation rapide

### 1. Lancer la stack complète (dev, test, monitoring)

```sh
cd docker
docker-compose pull
docker-compose up -d
```

**Services accessibles :**
- [Backend API](http://localhost:8080)
- [Frontend](http://localhost:3000)
- [Prometheus (métriques)](http://localhost:9090)
- [Grafana (dashboards)](http://localhost:3001)
- [Kibana (logs)](http://localhost:5601)
- PostgreSQL : localhost:5432

Pour arrêter :
```sh
docker-compose down
```

### 2. Documentation API (Swagger)
- [Swagger UI](http://localhost:8080/swagger-ui.html) : explorer et tester tous les endpoints
- **Exemple d'appel API (curl) :**
  ```sh
  curl -X POST http://localhost:8080/login \
    -H 'Content-Type: application/json' \
    -d '{"username": "admin", "password": "admin"}'
  ```

### 3. CI/CD et qualité
- **Build, tests, couverture, sécurité** : automatisés à chaque push/PR (voir l'onglet Actions GitHub)
- **Changelog** : généré automatiquement à chaque release (onglet Releases)
- **Notification d'échec** : mail envoyé à l'équipe en cas d'échec de build

### 4. Monitoring & logs
- **Prometheus** : http://localhost:9090 (métriques backend)
- **Grafana** : http://localhost:3001 (login : admin/admin)
- **Kibana** : http://localhost:5601 (logs centralisés)

### 5. Bonnes pratiques pour l'équipe
- Ne jamais versionner de secrets (utiliser `.env` et GitHub Secrets)
- Utiliser les scripts du dossier `scripts/` pour setup, start, reset-db, etc.
- Respecter le workflow Git (feature branch, PR, review, merge)
- Documenter toute nouvelle API dans Swagger
- Vérifier la CI avant de merger
- Consulter les dashboards Prometheus/Grafana pour surveiller la santé de l'app
- Consulter Kibana pour diagnostiquer les erreurs

### 6. Dépannage rapide
- **Un service ne démarre pas ?**
  ```sh
  docker-compose logs <service>
  ```
- **Problème de build/test ?**
  - Voir l'onglet Actions sur GitHub, ou le mail de notification d'échec
- **Problème de base de données ?**
  - Utiliser les scripts `reset-db.*` pour réinitialiser la base

---

## 📂 Structure du projet
- `backend/` : API Spring Boot (Java 21)
- `frontend/` : Interface React
- `docker/` : Orchestration Docker Compose, monitoring, logs
- `docs/` : Documentation technique et guides
- `scripts/` : Automatisation (setup, start, etc.)

---

## 🛠️ Prérequis
- Docker Desktop (recommandé)
- (Pour dev local : Java 21, Node.js 16+)

---

## 🔒 Authentification
- Gestion JWT, endpoints `/login`, `/api/**` protégés
- Voir la doc Swagger pour les détails

---

## 📚 Documentation complémentaire
- [docs/installation.md](docs/installation.md) : Installation et configuration locale
- [docs/architecture.md](docs/architecture.md) : Architecture technique
- [docs/scripts.md](docs/scripts.md) : Utilisation des scripts
- [docs/git-workflow.md](docs/git-workflow.md) : Bonnes pratiques Git/CI
- [devops.md](devops.md) : Bonnes pratiques DevOps, checklist, sécurité

---

## 🏷️ Changelog automatique
- Généré à chaque release (voir l'onglet Releases sur GitHub)

---

## 👥 Équipe & contact
- Pour toute question, ouvrez une issue sur GitHub ou contactez l'équipe DevOps

---

## ✨ Pour aller plus loin
- Ajoutez vos dashboards Grafana personnalisés
- Connectez Prometheus à d'autres services
- Intégrez la stack ELK à vos applications pour centraliser tous les logs

---

> Ce projet suit les meilleures pratiques DevOps et est prêt pour la production, le développement collaboratif et l'observabilité avancée.
