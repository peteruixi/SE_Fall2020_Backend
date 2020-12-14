# SE_Fall2020_Backend
@Author: Ruixi Li ruixi.li@rutgers.edu

Spring boot backend for SE Fall 2020 Health Monitoring Project

### Maven Dependencies
- Spring boot (Java Version: "1.8.0_251" )
- JDBC (mySQL 5.7.29)
- JWS Token
- Swagger IO
### SwaggerIO API Interface After Build
{your ip}:5000/api/swagger-ui.html#/
> Currently still connects to local DB

### IDE

This was developed using Intellij IDE. For setting up the development environment, load the project into the workspace and build the maven dependencies using Intellij's Maven tool. After complete downloading all required Java packages, it should allow you to run from the main function.

### Token and Testing

After the addition of token, using swagger for API testing is no longer feasiable. I would suggest using API testing tools like postman.  Token could be obtain through login, and for every API that requires user info, insert the token in the header field.

In the directory, you should be able to see a .json file. The file is an example of how testing was done for the demo. Also here is a linke for collection of the test cases, by following the link, it should input all the test cases to postman.

>  https://www.getpostman.com/collections/ef321b41883ddc06b1ee