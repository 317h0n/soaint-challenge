# SOAINT Java Test / 20-01-2021

El desarrollo de una API básica para un carrito de compras.

## Getting Started

Estas instrucciones te permitirán correr el programa de manera local para propósitos de testeo.

Esta API realiza:
- Login y retorno de token
- Toda la gestión de productos
- Registrar y listar clientes
- Registrar y listar ventas

### Prerequisites

Necesistas tener instalado JDK 11, y tener configurado el plugin de Lombok en tu IDE.

### Installing

- Tienes que clonar el repositorio y abrirlo en tu IDE de preferencia.
- Necesitarás Postman u otra herramienta para testear Apis, hay una [collection de Postman](https://github.com/317h0n/soaint-challenge/blob/main/SOAINT%20-%20TEST.postman_collection.json) en el repositorio para que puedas testear la API.

### API Docs

Se utilizó la libreria de Swagger para llevar acabo la documentación de la API, para poder verla haga [click aquí](http://localhost:8090/swagger-ui.html) cuando el proyecto este levantado.

## Testing

- La API utiliza un token del tipo BEARER
- Como usuario de prueba puede utilizarse: echocano / 12345
- Puedes usar la [collection de Postman](https://github.com/317h0n/soaint-challenge/blob/main/SOAINT%20-%20TEST.postman_collection.json).

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The java web framework used.
* [Maven](https://maven.apache.org/) - Dependency Management.
* [Lombok](https://projectlombok.org/) - Java library that automatically plugs into your editor and build tools, spicing up your java.
* [H2](https://www.h2database.com/html/main.html) - In-memory database.
* [Swagger](https://swagger.io/) - A professional toolset that can help you design and document your APIs.

## To Improve

Para mejorar la seguridad, recomendaría hacer el envío de las credenciales del usuario cifradas, y en el servicio del login descifrar y hacer el login.

## Authors

* **Elthon Chocano** - *Initial work* - (https://www.elthonchocano.me/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
