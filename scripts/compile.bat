\
    @echo off
    set BASE_DIR=%~dp0..
    set SRC_DIR=%BASE_DIR%\src\main\java
    set LIB_DIR=%BASE_DIR%\libs
    set OUT_DIR=%BASE_DIR%\out
    if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"
    set CP=.;%LIB_DIR%\*
    echo Compiling...
    javac -encoding UTF-8 -cp "%CP%" -d "%OUT_DIR%" -Xlint:deprecation -Xlint:unchecked @("%TEMP%\sources.txt")
    if %errorlevel% neq 0 exit /b %errorlevel%
    echo Done. Classes in %OUT_DIR%
