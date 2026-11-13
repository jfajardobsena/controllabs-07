# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.2/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.1.2/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.2/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

## Persistencia de ControlLabs

El proyecto utiliza Spring Data JPA y PostgreSQL. Crea primero la base de datos `controllabs` y ejecuta el script [`controllabs.sql`](./controllabs.sql) contra ella. Hibernate usa `ddl-auto=validate`, por lo que comprueba las tablas y columnas existentes sin alterarlas.

### Configurar la conexión a PostgreSQL

Spring Boot lee la conexión desde variables de entorno para no guardar la contraseña en el código:

* `DB_URL` (por defecto `jdbc:postgresql://localhost:5432/controllabs`)
* `DB_USERNAME` (por defecto `postgres`)
* `DB_PASSWORD` (sin valor predeterminado; debes configurarla)

En Windows, abre una terminal **PowerShell** en la carpeta del proyecto y define las variables en esa misma terminal. El valor de `DB_PASSWORD` debe ser la contraseña real de tu usuario de PostgreSQL:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/controllabs"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "tu contraseña de PostgreSQL"
```

Estas variables solo permanecen disponibles en esa ventana de PowerShell. Si abres otra terminal o ejecutas la aplicación desde otra sesión de VS Code, configúralas allí también. No escribas la contraseña en `application.properties` ni la guardes en Git.

### Ejecutar y probar

Con PostgreSQL iniciado, la base de datos `controllabs` creada y el script SQL aplicado, ejecuta la aplicación desde la misma terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

Cuando Spring Boot indique que inició correctamente, prueba el API en `http://localhost:8080/api/v1/rooms`. Para detenerlo, presiona `Ctrl+C` en la terminal.

Ejecuta `.\mvnw.cmd test` para verificar entidades, repositorios, servicios y el API de salas. Las pruebas utilizan H2 en memoria; no requieren PostgreSQL.

Para probar la conexión real a PostgreSQL y validar las tablas con Hibernate, configura las variables anteriores y ejecuta:

```powershell
.\mvnw.cmd '-Dtest=PostgresIntegrationTest' '-Dpostgres.integration=true' test
```

Esta prueba verifica la conexión a `controllabs`, valida el esquema sin modificarlo y realiza un CRUD de sala dentro de una transacción que se revierte al terminar.

El API de salas está bajo `/api/v1/rooms`. La creación recibe `name`, `locationBlock` y `totalPcs`; el identificador de sala es UUID.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
