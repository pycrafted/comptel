🌳 Flux de travail Git
Ce guide explique comment utiliser Git pour collaborer sur Comptel. Nous utilisons un flux basé sur des branches pour garder le code organisé et sécurisé. Suivez ces étapes pour contribuer efficacement ! 🚀

🌟 Aperçu du flux

Branches principales :
main : Code stable, prêt pour la production.
develop : Intégration des fonctionnalités en cours.


Branches de fonctionnalités :
Ex. : feature/authentification, feature/authentification-backend, feature/authentification-frontend.
Créez une branche pour chaque tâche (ex. : feature/<nom-tache>).


Règles de protection :
Les commits directs sur main et develop sont interdits.
Une pull request (PR) avec 1 approbation est requise.




🛠️ Étapes pour contribuer

Mettre à jour votre dépôt local :
git checkout develop
git pull origin develop


Créer une branche pour votre tâche :
git checkout -b feature/<nom-tache>

Exemple : feature/authentification-backend.

Faire vos modifications :

Modifiez le code dans backend/ ou frontend/.
Testez localement avec :.\scripts\start.ps1  # Windows
./scripts/start.sh   # Mac/Linux




Commiter vos changements :
git add .
git commit -m "Description claire de la tâche"


Pousser votre branche :
git push origin feature/<nom-tache>


Créer une pull request :

Allez sur https://github.com/pycrafted/comptel.
Cliquez sur New Pull Request.
Sélectionnez votre branche (feature/<nom-tache>) vers develop.
Ajoutez une description et demandez une revue.


Fusionner après approbation :

Un coéquipier approuvera votre PR.
Fusionnez dans develop via GitHub.
Supprimez la branche :git push origin --delete feature/<nom-tache>






🐛 Dépannage

Conflits de merge :
Résolvez localement :git checkout feature/<nom-tache>
git merge develop


Modifiez les fichiers en conflit, puis :git add <fichier>
git commit




Erreur de push :
Vérifiez votre authentification (HTTPS/SSH).
Forcez la mise à jour si nécessaire (avec prudence) :git push --force






📜 Bonnes pratiques

Messages de commit clairs : Ex. : “Ajout endpoint /api/login”.
Petites PR : Travaillez sur une tâche à la fois.
Testez avant de pousser : Utilisez les scripts pour vérifier.

Besoin d’aide ? Demandez à l’équipe !
