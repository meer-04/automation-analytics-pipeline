@echo off
setlocal enabledelayedexpansion

cd automation-java

call mvn clean test %*

IF %ERRORLEVEL% NEQ 0 (
    echo Java automation failed
    exit /b 1
)

cd ..\analytics-python
python analysis.py