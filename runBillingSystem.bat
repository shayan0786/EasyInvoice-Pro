@echo off
title Billing System Invoice 💻

echo 🔄 Compiling your Java files...
javac -cp ".;lib\jlayer-1.0.1.jar" BillingSystemInvoice.java LoginSignupPanel.java

echo ✅ Compilation Done!
echo 🎬 Starting your billing app...

java -cp ".;lib\jlayer-1.0.1.jar" BillingSystemInvoice

echo 🔚 Exited Successfully!
pause
