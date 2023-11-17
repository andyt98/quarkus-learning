token=$(http GET http://localhost:8080/jwt/admin)
http GET http://localhost:8080/admin/expenses "Authorization:Bearer $token"
