BIBLIOTHÈQUE UNIVERSITAIRE — Instructions d'installation
=========================================================

Prérequis :
- UWamp (Apache Tomcat + MySQL)
- Java 17+
- Maven

Étapes :
1. Importer bibliodb.sql dans phpMyAdmin
2. Vérifier persistence.xml (root/root MySQL)
3. mvn clean package
4. Copier target/matteoh.war dans le dossier webapps/ de Tomcat
5. Démarrer Tomcat
6. Accéder à http://localhost:8080/matteoh

Comptes de test :
- Admin        : 	admin / Rz9uTdYYuteUksp3dQPKxvR9
- Bibliothécaire : 	bibliothecaire / H7ivMLPtsartstCh9lUdKHuh
- Professeur : 		professeur / 4KqnEIBggSXYRX23hfqcAEJM
- Étudiant :		etudiant / gM8Jo1PW4HBXXes6oZOqu6As
			ou [créer via le formulaire d'inscription]