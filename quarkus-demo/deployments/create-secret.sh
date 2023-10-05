kubectl create secret generic customerdb-credentials \
--from-literal=username=myuser \
--from-literal=password=mypassword

#kubectl create secret docker-registry dockerhub-secret \
#  --docker-username=... \
#  --docker-password=...