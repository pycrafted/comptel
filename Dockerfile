# Dockerfile principal pour le déploiement sur Render
# Ce fichier sert de point d'entrée pour le déploiement

# Utiliser une image de base légère
FROM alpine:latest

# Installer curl pour les health checks
RUN apk add --no-cache curl

# Exposer le port par défaut
EXPOSE 8080

# Commande par défaut qui indique que ce Dockerfile est un placeholder
CMD ["echo", "This is a placeholder Dockerfile. Please use the specific Dockerfiles in backend/ and frontend/ directories."] 