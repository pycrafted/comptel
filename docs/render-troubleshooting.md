# 🔧 Guide de Dépannage - Déploiement Render

## ❌ Erreur: "failed to read dockerfile: open Dockerfile: no such file or directory"

### Cause
Render ne trouve pas le fichier Dockerfile à l'emplacement attendu.

### Solutions

#### Solution 1: Vérification des Chemins
Assurez-vous que les chemins dans `render.yaml` sont corrects :

```yaml
services:
  - type: web
    name: comptel-backend
    dockerfilePath: ./backend/Dockerfile  # ✅ Correct
    dockerContext: ./backend              # ✅ Correct
```

#### Solution 2: Déploiement Manuel
Si le Blueprint ne fonctionne pas, déployez manuellement :

1. **Créez d'abord la base de données PostgreSQL**
2. **Créez le service backend** :
   - Environment: Docker
   - Dockerfile Path: `./backend/Dockerfile`
   - Docker Context: `./backend`
   - Health Check Path: `/actuator/health`

3. **Créez le service frontend** :
   - Environment: Docker
   - Dockerfile Path: `./frontend/Dockerfile`
   - Docker Context: `./frontend`
   - Health Check Path: `/`

#### Solution 3: Vérification de la Structure
Vérifiez que votre repository contient bien :

```
comptel/
├── backend/
│   └── Dockerfile          # ✅ Doit exister
├── frontend/
│   └── Dockerfile          # ✅ Doit exister
├── render.yaml             # ✅ Doit exister
└── Dockerfile              # ✅ Placeholder à la racine
```

## 🔍 Autres Erreurs Courantes

### Erreur: "Build failed"
1. **Vérifiez les logs de build** dans le dashboard Render
2. **Assurez-vous que tous les fichiers sont commités** sur GitHub
3. **Vérifiez les permissions** des fichiers

### Erreur: "Health check failed"
1. **Vérifiez que l'application démarre correctement**
2. **Vérifiez les variables d'environnement**
3. **Vérifiez les logs de l'application**

### Erreur: "Database connection failed"
1. **Vérifiez que la base de données PostgreSQL est créée**
2. **Vérifiez les variables d'environnement de connexion**
3. **Vérifiez que la base de données est accessible**

## 🚀 Étapes de Déploiement Recommandées

### 1. Préparation
```bash
# Vérifiez que tous les fichiers sont présents
ls -la backend/Dockerfile
ls -la frontend/Dockerfile
ls -la render.yaml
```

### 2. Commit et Push
```bash
git add .
git commit -m "Prepare for Render deployment"
git push origin main
```

### 3. Déploiement sur Render
1. Connectez-vous à Render
2. Créez un nouveau "Blueprint"
3. Sélectionnez votre repository
4. Vérifiez que `render.yaml` est détecté
5. Cliquez sur "Apply"

### 4. Vérification
```bash
# Testez l'utilisateur admin
./scripts/verify-admin-user.sh https://comptel-backend.onrender.com
```

## 📋 Checklist de Vérification

- [ ] Repository GitHub à jour
- [ ] Fichier `render.yaml` présent à la racine
- [ ] `backend/Dockerfile` existe et fonctionne
- [ ] `frontend/Dockerfile` existe et fonctionne
- [ ] `Dockerfile` à la racine (placeholder)
- [ ] Variables d'environnement configurées
- [ ] Base de données PostgreSQL créée

## 🆘 Support

Si les problèmes persistent :

1. **Vérifiez les logs** dans le dashboard Render
2. **Testez localement** avec Docker Compose
3. **Vérifiez la documentation** Render
4. **Contactez le support** Render si nécessaire

## 🔗 Liens Utiles

- [Documentation Render](https://render.com/docs)
- [Guide des Blueprints](https://render.com/docs/blueprint-spec)
- [Dépannage Docker](https://render.com/docs/troubleshooting) 