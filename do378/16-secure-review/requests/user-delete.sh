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

curl -vX DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8080/expense/3f1817f2-3dcf-472f-a8b2-77bfe25e79d1
