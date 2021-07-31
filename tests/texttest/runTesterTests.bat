set TEXTTEST_HOME=%~dp0
set TESTER_BINARY=java -classpath optionslib.jar %CD%\..\..\bin\optionslib.jar de.dks.examples.options.Tester
SET TEXTTESTPY=texttest.exe
start %TEXTTESTPY% -a tester
