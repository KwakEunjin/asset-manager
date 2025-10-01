@echo off
cd /d "%~dp0"
javac -cp "src/main/java" -d "target/classes" src/main/java/com/assetmanager/*.java src/main/java/com/assetmanager/*/*.java
java -cp "target/classes" com.assetmanager.AssetManagerApplication