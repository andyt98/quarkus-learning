SERVER="http://localhost:8888/realms/quarkus/protocol/openid-connect/token"
SECRET_ID="backend-service"
SECRET_PW="secret"

TOKEN=$(curl --insecure -s -X POST "$SERVER" \
--user ${SECRET_ID}:${SECRET_PW} \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "username=user" \
-d "password=redhat" \
-d 'grant_type=password' \
| jq --raw-output '.access_token'
)

curl -s http://localhost:8080/oidc -H "Authorization: Bearer $TOKEN" | jq

curl -s http://localhost:8080/expense -H "Authorization: Bearer $TOKEN" | jq
