# Comptel - Application de Gestion Comptable

Application web complète pour la gestion comptable avec backend Spring Boot et frontend React.

## 🚀 Déploiement sur Render

### Option 1: Déploiement Automatique avec render.yaml (Recommandé)

1. **Forkez ou clonez ce repository**
2. **Connectez-vous à Render** et créez un nouveau "Blueprint"
3. **Sélectionnez votre repository** GitHub
4. **Render détectera automatiquement** le fichier `render.yaml` et configurera tous les services

### Option 2: Déploiement Manuel

#### 1. Base de Données PostgreSQL

1. Créez un nouveau service **PostgreSQL** sur Render
2. Notez les informations de connexion (URL, username, password)

#### 2. Backend Spring Boot

1. Créez un nouveau service **Web Service**
2. **Configuration:**
   - **Environment**: Docker
   - **Build Command**: (laissé vide, géré par Dockerfile)
   - **Start Command**: (laissé vide, géré par Dockerfile)
   - **Dockerfile Path**: `./backend/Dockerfile`
   - **Docker Context**: `./backend`

3. **Variables d'environnement:**
   ```
   SPRING_DATASOURCE_URL=<URL_DE_VOTRE_DB>
   SPRING_DATASOURCE_USERNAME=<USERNAME_DE_VOTRE_DB>
   SPRING_DATASOURCE_PASSWORD=<PASSWORD_DE_VOTRE_DB>
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_JPA_SHOW_SQL=false
   SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
   SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
   MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics
   SERVER_PORT=8080
   ```

4. **Health Check Path**: `/actuator/health`

#### 3. Frontend React

1. Créez un nouveau service **Web Service**
2. **Configuration:**
   - **Environment**: Docker
   - **Build Command**: (laissé vide, géré par Dockerfile)
   - **Start Command**: (laissé vide, géré par Dockerfile)
   - **Dockerfile Path**: `./frontend/Dockerfile`
   - **Docker Context**: `./frontend`

3. **Variables d'environnement:**
   ```
   REACT_APP_API_URL=https://<nom-de-votre-backend>.onrender.com
   NODE_ENV=production
   ```

4. **Health Check Path**: `/`

## 📁 Structure du Projet

```
comptel/
├── backend/                 # Application Spring Boot
│   ├── Dockerfile          # Configuration Docker pour le backend
│   ├── src/
│   └── pom.xml
├── frontend/               # Application React
│   ├── Dockerfile          # Configuration Docker pour le frontend
│   ├── nginx.conf          # Configuration Nginx
│   ├── src/
│   └── package.json
├── docker/                 # Configuration Docker Compose (développement)
├── render.yaml             # Configuration Render (production)
└── README.md
```

## 🔧 Configuration Avancée

### Variables d'environnement Backend

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de connexion à la base de données | `jdbc:postgresql://localhost:5432/comptel` |
| `SPRING_DATASOURCE_USERNAME` | Nom d'utilisateur de la DB | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Mot de passe de la DB | `postgres` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Mode de création des tables | `update` |
| `SPRING_JPA_SHOW_SQL` | Afficher les requêtes SQL | `false` |
| `SERVER_PORT` | Port du serveur | `8080` |

### Variables d'environnement Frontend

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `REACT_APP_API_URL` | URL de l'API backend | `http://localhost:8080/api` |
| `NODE_ENV` | Environnement Node.js | `production` |

## 🚀 Démarrage Rapide

### Développement Local

```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
npm start
```

### Production avec Docker

```bash
# Build et démarrage complet
docker-compose -f docker/docker-compose.yml up --build
```

## 📊 Monitoring

- **Backend Health Check**: `https://<backend-url>/actuator/health`
- **Frontend Health Check**: `https://<frontend-url>/health`

## 🔒 Sécurité

- Les variables sensibles sont gérées via les variables d'environnement Render
- Configuration CORS appropriée
- Headers de sécurité configurés dans Nginx

## 👤 Utilisateur par Défaut

Après le déploiement, un utilisateur administrateur est automatiquement créé :

- **Username**: `admin`
- **Password**: `admin`
- **Rôle**: Administrateur

⚠️ **IMPORTANT**: Changez ce mot de passe après votre première connexion !

### Vérification de l'utilisateur admin

Après le déploiement, vous pouvez vérifier que l'utilisateur admin fonctionne :

```bash
# Linux/Mac
./scripts/verify-admin-user.sh https://comptel-backend.onrender.com

# Windows PowerShell
.\scripts\verify-admin-user.ps1 https://comptel-backend.onrender.com
```

## 📝 Notes Importantes

1. **Base de données**: Assurez-vous que votre base PostgreSQL est créée avant le déploiement du backend
2. **URLs**: Mettez à jour `REACT_APP_API_URL` avec l'URL réelle de votre backend après déploiement
3. **SSL**: Render fournit automatiquement des certificats SSL
4. **Scaling**: Les services peuvent être mis à l'échelle selon vos besoins
5. **Sécurité**: L'utilisateur admin/admin est créé automatiquement - changez le mot de passe !

## 🆘 Support

En cas de problème:
1. Vérifiez les logs dans le dashboard Render
2. Assurez-vous que tous les services sont démarrés
3. Vérifiez la connectivité entre les services
