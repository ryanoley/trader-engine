<<<<<<< HEAD
# Crankshaft
Trading engine

### Importing into Eclipse
1. git clone the project to a preferred directory
2. In Eclipse, right-click in Package Explorer window Import
3. Select: Git/Projects from Git
4. Select: Existing local repository
5. Select: crankshaft
6. Select: Import existing Eclipse projects
7. Finish

### FIX Engine external library
Reference: https://github.com/quickfix-j/quickfixj
=======

###Initial Setup Steps

1. Setup Maven via this tutorial: https://www.mkyong.com/maven/how-to-install-maven-in-windows/

2. Rename Chad's crankshaft directory to crankshaftOLD

3. From GitHub direcotry, create Maven project with:
	mvn archetype:generate -DgroupId=com.roundaboutam.app -DartifactId=trader-engine -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

4. Create Git Repo via: https://help.github.com/articles/adding-an-existing-project-to-github-using-the-command-line/

5. Create .gitignore file in diretory include "target", ".project", ".classpath", ".settings"

6. Initial build from directory with: 	mvn package

7. Test jar file: java -cp target\trader-engine-1.0-SNAPSHOT.jar com.roundaboutam.app.App

8. Import project into Eclipse: File > Import > Existing Maven Project > Browse... trader-engine directory
>>>>>>> d9acd24d627a46d2582beea2a381b882a7213f87
