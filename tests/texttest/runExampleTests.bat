set TEXTTEST_HOME=%~dp0
set EXAMPLE_BINARY=java -classpath optionslib.jar %CD%\..\..\bin\optionslib.jar de.dks.examples.options.Example
SET TEXTTESTPY=texttest.exe
start %TEXTTESTPY% -a example 

