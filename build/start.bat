@echo off 
:: 文件编码 请用 ANSI

title 发送邮件
::@mode con lines=18 cols=40

::	  0 = 黑色       8 = 灰色
::    1 = 蓝色       9 = 淡蓝色
::    2 = 绿色       A = 淡绿色
::    3 = 湖蓝色     B = 淡浅绿色
::    4 = 红色       C = 淡红色
::    5 = 紫色       D = 淡紫色
::    6 = 黄色       E = 淡黄色
::    7 = 白色       F = 亮白色

:: 例:2f 第一个为背景，第二个则为前景

echo     【-m】: 发送邮件
echo     【-e】: 加密
echo     【 q】: 退出
echo -----------------------------


:sendEmile
color 02
echo.
set txt=
set /p txt="发送邮件:"
call:pub
java -jar "%~dp0lib\email.jar" %txt%
goto sendEmile

:encrypt
color 04
echo.
set txt=
set /p txt="加密:"
call:pub
java -jar "%~dp0lib\aes.jar" "-e" %txt%
goto encrypt


:decrypt
color 04
echo.
set txt=
set /p txt="解密:"
call:pub
java -jar "%~dp0lib\aes.jar" "-d" %txt%
goto decrypt


:pub
if "%txt%" == "q"  exit
if "%txt%" == "-q" exit
if "%txt%" == "-e" goto encrypt
if "%txt%" == "-d" goto decrypt
if "%txt%" == "-m" cls && goto sendEmile

