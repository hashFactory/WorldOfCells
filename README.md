# WorldOfCells

A template java projet for creating your own 3D environment with agents and cellular automata

support de code pour l'UE projet du L2 Sorbonne Universite (LU2IN013)
contact: nicolas.bredeche(at)sorbonne-universite.fr


# historique 

- 2013-00-00: premiere version mise a disposition des etudiants (L2, UE "projet", UPMC / SU)
- 2020-02-13: mise a jour, correction bug arbre, ajout tuto compilation en ligne de commande
- 2020-02-20: mise a jour tutorial Eclipse
- 2021-02-02: mise a jour avec la derniere version de JOGL + upload sur github

# prise en main

- lancer applications.simpleworld.MyEcosystem
- pendant l'execution, appuyer sur "h" pour afficher l'aide dans la console
- etudier les codes source du package applications.simpleworld

Ressources:
- la classe World et WorldOfTrees contiennent l'essentiel des elements pour creer votre monde
- le package objects contient la definition de quelques objets presents dans l'environnement
- la classe PerlinNoiseLandscapeGenerator est a ecrire

# DEPENDANCES

- Java JDK
- JOGL

# INSTALLATION

1. installer la version developpement d'openJDK (exemple avec openjdk ver.8): 
    sudo apt-get install openjdk-8-jdk

2. installer Jogl: 
    sudo apt-get install libjogl2-java

3. compiler depuis le répertoire WorldOfCells

    javac -classpath "/usr/share/java/gluegen2-rt.jar:/usr/share/java/jogl2.jar:." applications/simpleworld/*.java

4. executer
    java -classpath "/usr/share/java/gluegen2-rt.jar:/usr/share/java/jogl2.jar:." applications/simpleworld/MyEcosystem

# AUTRES INFORMATIONS

- pour verifier votre version de java et javac: "update-alternatives --config java" (ou javac)
- Probleme possible sur certaines machines: les arbres ne s'affichent pas sur certaines machines
	Solution: dans src/Objects/Tree.java, il y a 8 lignes commençant par "gl.glVertex3f(...)". Il suffit d'inverser les lignes paires et impaires (1 et 2, 3 et 4, etc.)
- OpenJDK: https://openjdk.java.net/install/
- package Ubuntu JOGL: https://packages.ubuntu.com/search?suite=default&section=all&arch=any&keywords=libjogl2-java&searchon=names
