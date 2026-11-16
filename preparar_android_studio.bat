@echo off
setlocal EnableExtensions
chcp 65001 >nul

set "NO_OPEN="
set "NO_PAUSE="
for %%A in (%*) do (
    if /i "%%~A"=="--no-open" set "NO_OPEN=1"
    if /i "%%~A"=="--no-pause" set "NO_PAUSE=1"
)

title Preparar ReLoop para Android Studio
cd /d "%~dp0"

echo.
echo ============================================================
echo   ReLoop - Preparación automática para Android Studio
echo ============================================================
echo.

if not exist "gradlew.bat" (
    echo [ERROR] No se encontró gradlew.bat.
    echo Ejecuta este archivo desde la carpeta raíz del proyecto.
    goto :fallo
)

if not exist "settings.gradle.kts" (
    echo [ERROR] No se encontró settings.gradle.kts.
    echo El ZIP parece incompleto o este archivo no está en la raíz.
    goto :fallo
)

rem Preferir el Java incluido con Android Studio.
set "STUDIO_JAVA=C:\Program Files\Android\Android Studio\jbr"
if exist "%STUDIO_JAVA%\bin\java.exe" (
    set "JAVA_HOME=%STUDIO_JAVA%"
    set "PATH=%STUDIO_JAVA%\bin;%PATH%"
) else if defined JAVA_HOME (
    if not exist "%JAVA_HOME%\bin\java.exe" (
        echo [ERROR] JAVA_HOME no apunta a una instalación válida de Java.
        goto :sin_java
    )
) else (
    where java.exe >nul 2>&1
    if errorlevel 1 goto :sin_java
)

echo [OK] Java disponible.
java -version 2>&1 | findstr /i "version"
echo.

rem Detectar el SDK de Android sin guardar rutas personales en el ZIP.
set "ANDROID_SDK_DIR="
if defined ANDROID_SDK_ROOT if exist "%ANDROID_SDK_ROOT%\platform-tools" set "ANDROID_SDK_DIR=%ANDROID_SDK_ROOT%"
if not defined ANDROID_SDK_DIR if defined ANDROID_HOME if exist "%ANDROID_HOME%\platform-tools" set "ANDROID_SDK_DIR=%ANDROID_HOME%"
if not defined ANDROID_SDK_DIR if exist "%LOCALAPPDATA%\Android\Sdk\platform-tools" set "ANDROID_SDK_DIR=%LOCALAPPDATA%\Android\Sdk"

if not defined ANDROID_SDK_DIR (
    echo No se encontró automáticamente el SDK de Android.
    echo Ejemplo: C:\Users\TuUsuario\AppData\Local\Android\Sdk
    set /p "ANDROID_SDK_DIR=Escribe la ruta completa del SDK: "
)

if not exist "%ANDROID_SDK_DIR%\platform-tools" (
    echo [ERROR] La ruta indicada no parece contener el SDK de Android:
    echo %ANDROID_SDK_DIR%
    goto :fallo
)

set "SDK_GRADLE=%ANDROID_SDK_DIR:\=/%"
> "local.properties" echo sdk.dir=%SDK_GRADLE%
echo [OK] local.properties configurado para este equipo.
echo.

echo [1/3] Comprobando Gradle Wrapper...
call gradlew.bat --version
if errorlevel 1 goto :gradle_error

echo.
echo [2/3] Descargando dependencias y sincronizando el proyecto...
call gradlew.bat help --no-daemon --no-configuration-cache
if errorlevel 1 goto :gradle_error

echo.
echo [3/3] Verificando la compilación Debug...
call gradlew.bat :app:assembleDebug --no-daemon --no-configuration-cache
if errorlevel 1 goto :gradle_error

echo.
echo ============================================================
echo   LISTO: proyecto sincronizado y compilado correctamente.
echo ============================================================
echo APK: app\build\outputs\apk\debug\app-debug.apk
echo.

set "STUDIO_EXE=C:\Program Files\Android\Android Studio\bin\studio64.exe"
if defined NO_OPEN (
    echo Apertura de Android Studio omitida por parámetro.
) else if exist "%STUDIO_EXE%" (
    echo Abriendo el proyecto en Android Studio...
    start "" "%STUDIO_EXE%" "%CD%"
) else (
    echo Android Studio no está en la ruta predeterminada.
    echo Abre Android Studio y selecciona esta carpeta:
    echo %CD%
)

echo.
if not defined NO_PAUSE pause
exit /b 0

:sin_java
echo [ERROR] No se encontró Java.
echo Instala Android Studio o configura JAVA_HOME con JDK 17 o superior.
goto :fallo

:gradle_error
echo.
echo [ERROR] Gradle no pudo sincronizar o compilar el proyecto.
echo Revisa tu conexión a Internet y el mensaje mostrado arriba.
goto :fallo

:fallo
echo.
echo La preparación no se completó.
if not defined NO_PAUSE pause
exit /b 1
