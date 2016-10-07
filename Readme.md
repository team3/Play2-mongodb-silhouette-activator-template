Play 2 Mongo Silhouette seed
=============================

An Activator template which shows the configuration and basic usage examples of [Play Framework 2.5] (https://www.playframework.com), [MongoDb](https://www.mongodb.com), [Silhouette 4.0] (http://silhouette.mohiva.com/docs/authenticator).

##Endpoints

Search companies by term
`GET         /api/companies/:term`

Display all companies
`GET         /api/companies`

Save/update company (Authentication required using [JWT](https://jwt.io) )
`POST        /api/companies/:id`

New user registration
`POST        /auth/signup`

##Examples

* New user registration

`$ curl http://127.0.0.1:9000/auth/signup -X POST -H 'Content-Type: application/json' -d '{"email" :"user1@company.com", "password": "password"}' -v`

Response

```json
*   Trying 127.0.0.1...
* Connected to 127.0.0.1 (127.0.0.1) port 9000 (#0)
> POST /auth/signup HTTP/1.1
> Host: 127.0.0.1:9000
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 54
>
< X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleUp3Y205MmFXUmxja2xFSWpvaVkzSmxaR1Z1ZEdsaGJITWlMQ0p3Y205MmFXUmxja3RsZVNJNkluVnpaWEl4UUdOdmJYQmhibmt1WTI5dEluMD0iLCJpc3MiOiJwbGF5LXNpbGhvdWV0dGUiLCJleHAiOjE0NzU5MTE1NTEsImlhdCI6MTQ3NTg2ODM1MSwianRpIjoiZGRhNGNiYjcyYzZmZTJjM2UyMDc2MmNmNjg1Mzk5YmNhZTllYmFhMGQ4OWY1NzVlN2RlNDI2NzEyZTEwMjEwMDY5ZjVmM2FiZDE0MTc4M2M1ZDI0MTFiYWQ2ZmNmMGJmMTAzMGM5YzdhNjQzMDkzZThjOGJjOWRmYTExOWUyMDI4ZmE5YzUyYzQxYjFhOTI2NTQxZjc1ZGYwNDZhNTY1NzJiYTI5MzMzM2ZjZGRmOGI2ZTliMzFhMGQ5YzViZWI4YmJkY2QzNTI1NDY2YzQ5MjFmYjQ1NThjOWMwNGQyNzQzZmE4MjdhODk5NjdiNjE4MDQ1MWMyNDRmY2M4NTYzOSJ9.r60YtTwubAl2FFpnfDNU-pLLhLveEPyVDiCx3r6AHd4
* upload completely sent off: 54 out of 54 bytes
< HTTP/1.1 200 OK
< Content-Length: 639
< Content-Type: application/json
< Date: Fri, 07 Oct 2016 19:25:51 GMT
<
* Connection #0 to host 127.0.0.1 left intact
```

* Authorized request

`$ curl http://127.0.0.1:9000/api/companies/57f0f0a79e6e4aa2c730de25 -X POST -H 'Content-Type: application/json' -H 'X-Auth-Token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleUp3Y205MmFXUmxja2xFSWpvaVkzSmxaR1Z1ZEdsaGJITWlMQ0p3Y205MmFXUmxja3RsZVNJNkluVnpaWEl4UUdOdmJYQmhibmt1WTI5dEluMD0iLCJpc3MiOiJwbGF5LXNpbGhvdWV0dGUiLCJleHAiOjE0NzU5MTE1NTEsImlhdCI6MTQ3NTg2ODM1MSwianRpIjoiZGRhNGNiYjcyYzZmZTJjM2UyMDc2MmNmNjg1Mzk5YmNhZTllYmFhMGQ4OWY1NzVlN2RlNDI2NzEyZTEwMjEwMDY5ZjVmM2FiZDE0MTc4M2M1ZDI0MTFiYWQ2ZmNmMGJmMTAzMGM5YzdhNjQzMDkzZThjOGJjOWRmYTExOWUyMDI4ZmE5YzUyYzQxYjFhOTI2NTQxZjc1ZGYwNDZhNTY1NzJiYTI5MzMzM2ZjZGRmOGI2ZTliMzFhMGQ5YzViZWI4YmJkY2QzNTI1NDY2YzQ5MjFmYjQ1NThjOWMwNGQyNzQzZmE4MjdhODk5NjdiNjE4MDQ1MWMyNDRmY2M4NTYzOSJ9.r60YtTwubAl2FFpnfDNU-pLLhLveEPyVDiCx3r6AHd4'  -d '{"name" : "company1", "address": "New York", "email": "company1@companies.com", "phone": 123}' -v`

Response

```json
*   Trying 127.0.0.1...
* Connected to 127.0.0.1 (127.0.0.1) port 9000 (#0)
> POST /api/companies/57f0f0a79e6e4aa2c730de25 HTTP/1.1
> Host: 127.0.0.1:9000
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Type: application/json
> X-Auth-Token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleUp3Y205MmFXUmxja2xFSWpvaVkzSmxaR1Z1ZEdsaGJITWlMQ0p3Y205MmFXUmxja3RsZVNJNkluVnpaWEl4UUdOdmJYQmhibmt1WTI5dEluMD0iLCJpc3MiOiJwbGF5LXNpbGhvdWV0dGUiLCJleHAiOjE0NzU5MTE1NTEsImlhdCI6MTQ3NTg2ODM1MSwianRpIjoiZGRhNGNiYjcyYzZmZTJjM2UyMDc2MmNmNjg1Mzk5YmNhZTllYmFhMGQ4OWY1NzVlN2RlNDI2NzEyZTEwMjEwMDY5ZjVmM2FiZDE0MTc4M2M1ZDI0MTFiYWQ2ZmNmMGJmMTAzMGM5YzdhNjQzMDkzZThjOGJjOWRmYTExOWUyMDI4ZmE5YzUyYzQxYjFhOTI2NTQxZjc1ZGYwNDZhNTY1NzJiYTI5MzMzM2ZjZGRmOGI2ZTliMzFhMGQ5YzViZWI4YmJkY2QzNTI1NDY2YzQ5MjFmYjQ1NThjOWMwNGQyNzQzZmE4MjdhODk5NjdiNjE4MDQ1MWMyNDRmY2M4NTYzOSJ9.r60YtTwubAl2FFpnfDNU-pLLhLveEPyVDiCx3r6AHd4
> Content-Length: 93
>
* upload completely sent off: 93 out of 93 bytes
< HTTP/1.1 200 OK
< Content-Length: 16
< Content-Type: application/json
< Date: Fri, 07 Oct 2016 19:29:58 GMT
<
* Connection #0 to host 127.0.0.1 left intact
{"success":true}
```

* All companies

$ `curl http://127.0.0.1:9000/api/companies`

Response

```json
MapLike("57f0f0a79e6e4aa2c730de25", "company1", "New York", "company1@companies.com", 123)
```
