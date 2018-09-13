
@echo off

SETLOCAL ENABLEEXTENSIONS

set TRADERENGINE_APP=%GITHUB%\trader-engine\target

CLS

:MENU
ECHO.
ECHO.
ECHO ---------------------------------------------------------------
ECHO --------------- Trader Engine Application Manager -------------
ECHO ---------------------------------------------------------------
ECHO.
ECHO ------------------------ INSTRUCTIONS -------------------------
ECHO Select an item from the menu.
ECHO.
ECHO -------------------TRADE IMPLEMENTAION TASKS---------------------
ECHO.
ECHO 1.  PROD Trader Engine: This launches the Trader Engine in
ECHO.		 Production configuration.  Do not Launch if already Running.
ECHO.
ECHO 2.  TEST Trader Engine:  This launches the Trader Engine in Test
ECHO.		 configuration.
ECHO.
ECHO 3.  Exit this application
ECHO ----------------------------------------------------------------
ECHO.
ECHO.

:GETINPUT
set /p M="Type 1-3 and press ENTER to Run that process: "
IF %M%==1 GOTO RUNPROD
IF %M%==2 GOTO RUNTEST
IF %M%==3 GOTO EOF
GOTO EOF


:RUNPROD
ECHO.
ECHO.
ECHO --LAUNCHING PROD--
ECHO.
start java -jar %TRADERENGINE_APP%\trader-engine-1.0.jar -prod
GOTO SPACING

:RUNTEST
ECHO.
ECHO.
ECHO --LAUNCHING TEST--
ECHO.
start java -jar %TRADERENGINE_APP%\trader-engine-1.0.jar -uat
GOTO SPACING

:SPACING
ECHO.
ECHO.
ECHO.
ECHO ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
ECHO.
ECHO.
ECHO.
ECHO.
set M='Q'
GOTO GETINPUT

:EOF
CLS
