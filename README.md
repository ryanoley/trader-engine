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
