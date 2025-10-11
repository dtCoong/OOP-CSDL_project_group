\
    @echo off
    set BASE_DIR=%~dp0..
    set OUT_DIR=%BASE_DIR%\out
    set LIB_DIR=%BASE_DIR%\libs
    set CP=%OUT_DIR%;%LIB_DIR%\*
    echo Running MainDemo ...
    java -cp "%CP%" com.mycompany.ehr.model.MainDemo
