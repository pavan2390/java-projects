1. Download and install java latest version and set the environment variables. (refer YouTube for installation process)
2. Download and install Spring Tools Suite or Eclipse Ide for Java-Springboot projects. (refer YouTube for installation process)
3. We will be using different type of databases here.(mysql/sql server/postgresql..)
4. Download and install MySql shell and workbench both. (refer YouTube for installation process)
5. Download and install PostGres psql shell and pgAdmin4 both. (refer YouTube for installation process)
6. Download and install Sql Server Express Edition and also Sql Server Management Studio. (refer YouTube for installation process)
7. After installing Sql Server configure the TCP/IP Port of SQL Server using SQL-Server-Configuration-Manager. default port 1433
8. Service restart sql service automatic
9. launch eclipse or sts. Select work directory
10. If prompted, Microsoft defender - exclude eclipse
11. Inside File menu of Eclips/STS, click import-> Maven-> Existing Maven Projects -> browse and select your project folder. The POM file should appear in the dropdown list.
12. Set DB Configuration details in APPLICATION.PROPERTIES files
13. Open ssms security -logins create db user and password (-39 min reference for me alone)
14. For ssl security create keyStore file using java KeyTool. (refer youtube video)
15. After project is imported in STS/Eclipse, right click on project name->Maven->Update project->force update ...
16. Run the project as maven build(clean install) - refer YouTube if needed
17. Run as Springboot application or java application
18. Use Postman and signUp to create and login users. using POST methods (https://localhost:8443/auth/signup , https://localhost:8443/auth/login)
19. For IPTraceExample, hit in Postman using GET method -> http://localhost:8443/api/client-ip (make it https)
20. For StockExample, hit in Postman using POST method -> http://localhost:8443/stock/loader/uploadcanada (make it https) and in the body pass your file path.
21.