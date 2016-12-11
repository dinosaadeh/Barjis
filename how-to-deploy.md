# Étapes pour installer le jeu

Pour déployer le jeu, veuillez suiver les étapes suivantes:
* Téléchargez le code depuis ce lien (https://github.com/dinosaadeh/Barjis.git)
* La plateforme libgdx permet de générer le jeu pour plusieurs plateforme. Si vous souhaitez utiliser la version Android, compiler le projet Android. Ensuite, vous pourrez prendre le package APK et l'installer sur votre machine Android.
* Si vous voulez démarer l'application sur l'ordinateur, démarer le projet Desktop.

**Pour jouer en mode réseaux:**
* Vérifiez que [node.js](https://nodejs.org/en/) est installé sur votre machine serveur.
* Télécharger l'application serveur depuis (https://github.com/dinosaadeh/BarjisGameServer.git)
* Dans un terminal, naviguez au répertoire du serveur (ex.: *path-to-BarjisGameServer*/game-server/).
* Lancer cette commande: `pomelo start`
* Afin que le jeu se connecte au serveur du jeu, vous devez changer le fichier configuration.xml qui réside sous *path-to-Barjis*/Barjis/android/assets/
* Changez l'addresse IP du clef GameServer (juste l'attribut PomeloGateHost)
