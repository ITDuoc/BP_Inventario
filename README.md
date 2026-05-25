API Inventario Caso BookPoint
 SpringBoot + JPA + MYSQL


Query para crear Base de Datos requerida:
CREATE DATABASE inventario_db;


Endpoints de los metodos existentes

Libros:
Crear:
POST /api/v1/libros
http://localhost:8083/api/v1/libros

{
    "titulo":"El Hobbit",
    "autor":"J.R.R Tolkien",
    "editorial":"Minotauro",
    "genero":"Fantasia",
    "precio":19990,
    "ubicacionBodega":"A-12",
    "stockBodega":100,
    "mostrarCatalogo":true
}

Listar:
GET /api/v1/libros
http://localhost:8083/api/v1/libros

Buscar por ID:
GET /api/v1/libros/{id}
http://localhost:8083/api/v1/libros/1

Modificar:
PUT /api/v1/libros/{id}
http://localhost:8083/api/v1/libros/1

Eliminar:
DELETE /api/v1/libros/{id}
http://localhost:8083/api/v1/libros/1


Articulos:
Crear:
POST /api/v1/articulos
http://localhost:8083/api/v1/articulos

{
    "nombre":"Cuaderno Universitario",
    "marca":"Torre",
    "precio":2990,
    "ubicacionBodega":"B-04",
    "stockBodega":200,
    "mostrarCatalogo":true
}

Listar:
GET /api/v1/articulos
http://localhost:8083/api/v1/articulos

Buscar por ID:
GET /api/v1/articulos/{id}
http://localhost:8083/api/v1/articulos/1

Modificar:
PUT /api/v1/articulos/{id}
http://localhost:8083/api/v1/articulos/1

Eliminar:
DELETE /api/v1/articulos/{id}
http://localhost:8083/api/v1/articulos/1




Stock Libros en la sucursal:

Listar stock de libros en TODAS las sucursales:
GET /api/v1/stock-libros
http://localhost:8083/api/v1/stock-libros

Listar libros POR SUCURSAL:
GET /api/v1/stock-libros/sucursal/{id}
http://localhost:8083/api/v1/stock-libros/sucursal/1

Distribuir libros desde bodega central:
POST /api/v1/stock-libros/distribuir
http://localhost:8083/api/v1/stock-libros/distribuir

{
    "libroId":1,
    "sucursalId":1,
    "cantidad":20
}

Ver alertas de stock minimo de libros:
GET /api/v1/stock-libros/alertas/{sucursalId}
http://localhost:8083/api/v1/stock-libros/alertas/1


Stock articulos en la sucursal:

Listar stock de articulos en TODAS las sucursales:
GET /api/v1/stock-articulos
http://localhost:8083/api/v1/stock-articulos

Listar articulos POR SUCURSAL:
GET /api/v1/stock-articulos/sucursal/{id}
http://localhost:8083/api/v1/stock-articulos/sucursal/1

Distribuir articulos desde bodega central:
POST /api/v1/stock-articulos/distribuir
http://localhost:8083/api/v1/stock-articulos/distribuir

{
    "articuloId":1,
    "sucursalId":2,
    "cantidad":50
}

Ver alertas de stock minimo de articulos:
GET /api/v1/stock-articulos/alertas/{sucursalId}
http://localhost:8083/api/v1/stock-articulos/alertas/1


Pasos a seguir:
-Crear base de datos con el query
-Correr la aplicacion
-Crear productos
