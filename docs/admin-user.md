# 👤 Utilisateur Administrateur par Défaut

## 📋 Informations de Connexion

Après le déploiement de l'application, un utilisateur administrateur est automatiquement créé avec les identifiants suivants :

- **Username**: `admin`
- **Password**: `admin`
- **Rôle**: Administrateur (accès complet)

## 🔧 Comment ça fonctionne

### 1. Création Automatique

L'utilisateur admin est créé automatiquement par :

1. **DataInitializer.java** : Classe Spring Boot qui vérifie si l'utilisateur admin existe au démarrage
2. **data.sql** : Script SQL d'initialisation comme fallback
3. **Configuration** : `spring.jpa.defer-datasource-initialization=true` dans `application.properties`

### 2. Logs de Création

Lors du démarrage, vous verrez dans les logs :

```
✅ Default admin user created successfully!
📝 Username: admin
🔑 Password: admin
⚠️  IMPORTANT: Change this password after first login!
```

## 🔍 Vérification

### Script de Vérification

Après le déploiement, vous pouvez vérifier que l'utilisateur admin fonctionne :

#### Linux/Mac
```bash
./scripts/verify-admin-user.sh https://comptel-backend.onrender.com
```

#### Windows PowerShell
```powershell
.\scripts\verify-admin-user.ps1 https://comptel-backend.onrender.com
```

### Test Manuel

Vous pouvez aussi tester manuellement avec curl :

```bash
curl -X POST https://comptel-backend.onrender.com/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'
```

## 🔒 Sécurité

### ⚠️ IMPORTANT : Changement de Mot de Passe

**Il est CRUCIAL de changer le mot de passe admin après votre première connexion !**

### Recommandations de Sécurité

1. **Changement immédiat** : Changez le mot de passe dès la première connexion
2. **Mot de passe fort** : Utilisez un mot de passe complexe (majuscules, minuscules, chiffres, caractères spéciaux)
3. **Authentification à deux facteurs** : Activez la 2FA si disponible
4. **Surveillance** : Surveillez les tentatives de connexion

## 🛠️ Dépannage

### L'utilisateur admin n'est pas créé

1. **Vérifiez les logs** du backend pour voir les erreurs
2. **Vérifiez la base de données** : L'utilisateur existe-t-il ?
3. **Redémarrez l'application** : Parfois nécessaire après la première initialisation

### Erreur de connexion

1. **Vérifiez l'URL** : Assurez-vous que l'URL du backend est correcte
2. **Vérifiez les logs** : Regardez les erreurs d'authentification
3. **Testez l'endpoint** : Vérifiez que `/login` répond correctement

### Réinitialisation de l'utilisateur admin

Si vous devez recréer l'utilisateur admin :

```sql
-- Supprimer l'utilisateur admin existant
DELETE FROM users WHERE username = 'admin';

-- Redémarrer l'application pour recréer l'utilisateur
```

## 📞 Support

En cas de problème avec l'utilisateur admin :

1. Vérifiez les logs de l'application
2. Testez la connexion avec les scripts fournis
3. Consultez la documentation de l'API
4. Contactez l'équipe de support si nécessaire 