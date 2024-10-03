SET TempVBSFile=%temp%\~tmpSendKeysTemp.vbs
IF EXIST "%TempVBSFile%" DEL /F /Q "%TempVBSFile%"
ECHO Set WshShell = WScript.CreateObject("WScript.Shell") >>"%TempVBSFile%"
ECHO Wscript.Sleep 50                                    >>"%TempVBSFile%"
ECHO WshShell.SendKeys "{F11}"                            >>"%TempVBSFile%
ECHO Wscript.Sleep 50                                    >>"%TempVBSFile%"
CSCRIPT //nologo "%TempVBSFile%"
chcp 65001
reg query HKCU\Console /v VirtualTerminalLevel >nul 2>&1

if %errorlevel% neq 0 (
    echo Activation des séquences ANSI...
    reg add HKCU\Console /v VirtualTerminalLevel /t REG_DWORD /d 1
    echo Séquences ANSI activées avec succès. Veuillez relancer le script
) else (
    echo Les séquences ANSI sont déjà activées
)

cls
java BatailleLANvale
