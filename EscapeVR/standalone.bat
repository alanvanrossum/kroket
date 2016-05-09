@echo off
set basedir=%~d0%~p0
echo %basedir%
"java.exe" -Dfile.encoding=UTF-8 -classpath %basedir%\jme_vr_libs\JMonkeyVR.jar;%basedir%\jme_vr_libs\j-ogg-all.jar;%basedir%\jme_vr_libs\jme3-blender.jar;%basedir%\jme_vr_libs\jme3-core.jar;%basedir%\jme_vr_libs\jme3-desktop.jar;%basedir%\jme_vr_libs\jme3-effects.jar;%basedir%\jme_vr_libs\jme3-jogg.jar;%basedir%\jme_vr_libs\jme3-lwjgl3.jar;%basedir%\jme_vr_libs\jme3-plugins.jar;%basedir%\jme_vr_libs\jna-4.2.1.jar;%basedir%\jme_vr_libs\lwjgl-3.0.0-natives.jar;%basedir%\jme_vr_libs\lwjgl-3.0.0.jar;%basedir%\build\classes;%basedir%\assets nl.tudelft.kroket.escape.Main
pause
