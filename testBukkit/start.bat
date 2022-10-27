@echo off
set JAVA_HOME=C:\Users\NewestAF\.jdks\corretto-17.0.4.1
set Path=%JAVA_HOME%\bin;%Path%
echo JRE Activated
java -Xms2G -Xmx8G -jar paper-1.17.1-411.jar nogui
pause
