
###Initial Setup Steps

1. Setup Maven via this tutorial: https://www.mkyong.com/maven/how-to-install-maven-in-windows/

2. Rename Chad's crankshaft directory to crankshaftOLD

3. From GitHub direcotry, create Maven project with:
	mvn archetype:generate -DgroupId=com.roundaboutam.app -DartifactId=crankshaft -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

4. Initial build from directory with: 	mvn package

5. Test jar file: java -cp target\crankshaft-1.0-SNAPSHOT.jar com.roundaboutam.app.App

6. Import project into Eclipse: File > Import > Existing Maven Project > Browse... crankshaft directory

7. Create .gitignore file in diretory include "target", ".project", ".classpath"

