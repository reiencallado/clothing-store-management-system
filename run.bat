@echo off
cd src
javac -cp "../lib/mysql-connector-j-9.3.0.jar;." *.java
cd ..
java -cp "lib/mysql-connector-j-9.3.0.jar;src" Driver