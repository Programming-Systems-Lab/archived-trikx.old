@echo off
ECHO BUILDING TRIKX JAR...
cd ..\..\..
jar cvf psl\trikx\impl\trikx.jar psl\trikx\impl\*.class
cd psl\trikx\impl