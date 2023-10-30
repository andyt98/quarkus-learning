keytool -noprompt -genkeypair -keyalg RSA -keysize 2048 \
  -validity 365 \
  -dname "CN=myapp,OU=myorgunit, O=myorg, L=myloc, ST=state, C=country" \
  -ext "SAN:c=DNS:myserver,IP:127.0.0.1"\
  -alias myapp \
  -storetype JKS \
  -storepass mypass -keypass mypass \
  -keystore myapp.keystore 
