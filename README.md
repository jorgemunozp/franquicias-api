# API de Franquicias

API REST desarrollada con **Java 21 + Spring Boot + Spring Data JPA + MySQL** para administrar franquicias, sucursales y productos.

El proyecto incluye:

- CRUD parcial orientado a los casos de uso solicitados.
- Validaciones de entrada con Jakarta Validation.
- Manejo global de errores.
- Persistencia en MySQL.
- Tests unitarios para servicios y controladores.
- Por practicidad, la `URL` de la BD y las credenciales estan `hardcoded` en las propiedades de la aplicación

## 1. Arquitectura

La aplicación sigue una arquitectura por capas:

```text
Cliente HTTP
    │
    ▼
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
MySQL
```

Paquetes principales:

```text
com.franquicias
├── controller
├── dto
├── exception
├── model
├── repository
└── service
```


## 2. Modelo de datos

La relación entre las entidades es:

```text
Franquicia 1 ─────── N Sucursal 1 ─────── N Producto
```

### Franquicia

- `id`
- `nombre`

### Sucursal

- `id`
- `nombre`
- `id_franquicia`

### Producto

- `id`
- `nombre`
- `stock`
- `id_sucursal`

Las restricciones de unicidad actuales son:

- No puede existir más de una franquicia con el mismo nombre.
- No puede existir más de una sucursal con el mismo nombre dentro de una franquicia.
- No puede existir más de un producto con el mismo nombre dentro de una sucursal.

El stock no puede ser negativo.

---

## 3. Requisitos

### Para ejecución local con Maven

- Java 21
- Maven 3.9+
- MySQL accesible desde el equipo


## 4. Ejecutar localmente con Maven

Desde la raíz del proyecto:

```bash
mvn clean test
```

Para ejecutar la aplicación:

```bash
mvn spring-boot:run
```

El proyecto tiene configurada explícitamente la clase principal:

```text
com.franquicias.FranquiciasApplication
```

La API estará disponible en:

```text
http://localhost:8080
```

# 5. Contratos REST

Base URL:

```text
http://localhost:8080/api
```

## 5.1 Crear franquicia

```http
POST /api/franquicias
Content-Type: application/json
```

Request:

```json
{
  "nombre": "McDonalds"
}
```

Respuesta esperada: `201 Created`

```json
{
  "id": 1,
  "nombre": "McDonalds"
}
```

cURL:

```bash
curl -i -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"McDonalds"}'
```

---

## 5.2 Actualizar nombre de franquicia

```http
PATCH /api/franquicias/{id}
Content-Type: application/json
```

Request:

```json
{
  "nombre": "McDonald's"
}
```

Respuesta: `200 OK`.

cURL:

```bash
curl -i -X PATCH http://localhost:8080/api/franquicias/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"McDonalds Colombia"}'
```

---

## 5.3 Crear sucursal

```http
POST /api/franquicias/{franquiciaId}/sucursales
Content-Type: application/json
```

Request:

```json
{
  "nombre": "Zona T"
}
```

Respuesta: `201 Created`.

cURL:

```bash
curl -i -X POST http://localhost:8080/api/franquicias/1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Zona T"}'
```

---

## 5.4 Actualizar nombre de sucursal

```http
PATCH /api/sucursales/{id}
Content-Type: application/json
```

Request:

```json
{
  "nombre": "Centro"
}
```

cURL:

```bash
curl -i -X PATCH http://localhost:8080/api/sucursales/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Centro"}'
```

---

## 5.5 Crear producto

```http
POST /api/sucursales/{sucursalId}/productos
Content-Type: application/json
```

Request:

```json
{
  "nombre": "Big Mac",
  "stock": 50
}
```

Respuesta: `201 Created`.

cURL:

```bash
curl -i -X POST http://localhost:8080/api/sucursales/1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Big Mac","stock":50}'
```

---

## 5.6 Actualizar stock

```http
PATCH /api/sucursales/{sucursalId}/productos/{productoId}/stock
Content-Type: application/json
```

Request:

```json
{
  "stock": 100
}
```

cURL:

```bash
curl -i -X PATCH http://localhost:8080/api/sucursales/1/productos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":100}'
```

---

## 5.7 Eliminar producto

```http
DELETE /api/sucursales/{sucursalId}/productos/{productoId}
```

Respuesta: `204 No Content`.

cURL:

```bash
curl -i -X DELETE http://localhost:8080/api/sucursales/1/productos/1
```

---

## 5.8 Actualizar nombre de producto

```http
PATCH /api/productos/{productoId}
Content-Type: application/json
```

Request:

```json
{
  "nombre": "Big Mac Grande"
}
```

cURL:

```bash
curl -i -X PATCH http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Big Mac Grande"}'
```

---

# 6. Resumen de endpoints

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| POST | `/api/franquicias` | Crear franquicia | 201 |
| PATCH | `/api/franquicias/{id}` | Actualizar nombre | 200 |
| POST | `/api/franquicias/{franquiciaId}/sucursales` | Crear sucursal | 201 |
| PATCH | `/api/sucursales/{id}` | Actualizar nombre | 200 |
| POST | `/api/sucursales/{sucursalId}/productos` | Crear producto | 201 |
| PATCH | `/api/sucursales/{sucursalId}/productos/{productoId}/stock` | Actualizar stock | 200 |
| DELETE | `/api/sucursales/{sucursalId}/productos/{productoId}` | Eliminar producto | 204 |
| PATCH | `/api/productos/{productoId}` | Actualizar nombre | 200 |


## Licencia

Proyecto desarrollado con fines técnicos y de demostración.
