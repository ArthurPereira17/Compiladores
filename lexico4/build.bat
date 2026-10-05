@echo off
setlocal enabledelayedexpansion

echo Limpando build anterior...
if exist build rmdir /s /q build
if exist dist\compilador.jar del /q dist\compilador.jar
mkdir build 2>nul
mkdir dist 2>nul

echo Compilando (target Java 17, compativel com Windows 8+)...
javac --release 17 -d build src\compilador\*.java
if errorlevel 1 (
    echo.
    echo ERRO: falha na compilacao. Corrija os erros acima antes de continuar.
    exit /b 1
)

echo Empacotando .jar...
jar cfe dist\compilador.jar compilador.CompilerInterface -C build .
if errorlevel 1 (
    echo.
    echo ERRO: falha ao gerar o jar.
    exit /b 1
)

echo.
echo Build OK -^> dist\compilador.jar
endlocal