# 2ProyectoPP - Sistema de Microservicios

Desarrollado por Kevin Beita , Greivin Narvaez, Yutaro Cubero, Francisco Araya para el curso Paradigmas de Programacion.
Sistema de gestión de agenda basado en microservicios con Spring Boot, Eureka Server, API Prolog y Frontend.

## 📋 Arquitectura del Sistema

El proyecto consta de los siguientes componentes:

- **Eureka Server** (Puerto 8761): Service Discovery
- **MySQL** (Puerto 3306): Base de datos
- **Prolog API** (Puerto 8080): API de lógica con Prolog
- **Agenda API** (Puerto 8081): API REST principal
- **Frontend**: Interfaz de usuario (React/Angular)

## 🔧 Prerequisitos

Antes de comenzar, asegúrate de tener instalado:

- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Git](https://git-scm.com/downloads)
- PowerShell (Windows) o Terminal (Mac/Linux)

## 🚀 Instalación y Ejecución

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/KevinBeitaM/2ProyectoPP.git
cd 2ProyectoPP
```

### 2️⃣ Crear la Red de Docker

```bash
docker network create red_paradigmas
```

### 3️⃣ Levantar los Servicios (EN ORDEN)

**IMPORTANTE:** Debes ejecutar los comandos en este orden específico.

#### A. Levantar MySQL
```bash
docker run -d \
  --name mysql \
  --network red_paradigmas \
  -e MYSQL_ROOT_PASSWORD=12345678 \
  -e MYSQL_DATABASE=paradigmas \
  -p 3306:3306 \
  mysql:8.0
```

**PowerShell (Windows):**
```powershell
docker run -d `
  --name mysql `
  --network red_paradigmas `
  -e MYSQL_ROOT_PASSWORD=12345678 `
  -e MYSQL_DATABASE=paradigmas `
  -p 3306:3306 `
  mysql:8.0
```

#### B. Levantar Eureka Server
```bash
cd eureka-server
docker build -t eureka .
docker run -d --name eureka --network red_paradigmas -p 8761:8761 eureka
cd ..
```

⏱️ **Espera 30-40 segundos** para que Eureka inicie completamente.

#### C. Levantar Prolog API
```bash
cd prologAPI-master
docker build -t prolog_api:latest .
docker run -d --name prologapi --network red_paradigmas -p 8080:8080 prolog_api:latest
cd ..
```

⏱️ **Espera 20-30 segundos** para que Prolog API se registre en Eureka.

#### D. Levantar Agenda API
```bash
cd agenda
docker build -t agenda_api .
docker run -d --name agenda_api --network red_paradigmas -e SPRING_PROFILES_ACTIVE=docker -p 8081:8081 agenda_api
cd ..
```

## ✅ Verificar que Todo Está Corriendo

```bash
# Ver todos los contenedores activos
docker ps

# Ver logs de cada servicio
docker logs eureka
docker logs prologapi
docker logs agenda_api
docker logs mysql
```

## 🌐 Acceder a los Servicios

Una vez que todos los servicios estén corriendo:

- **Eureka Dashboard**: http://localhost:8761
- **Prolog API**: http://localhost:8080
- **Agenda API**: http://localhost:8081
- **MySQL**: localhost:3306 (usuario: root, password: 12345678)

## 🛑 Detener los Servicios

```bash
docker stop agenda_api prologapi eureka mysql
docker rm agenda_api prologapi eureka mysql
```

## 🔄 Reiniciar Todo desde Cero

Si necesitas limpiar todo y empezar de nuevo:

```bash
# Detener y eliminar contenedores
docker stop agenda_api prologapi eureka mysql
docker rm agenda_api prologapi eureka mysql

# Eliminar imágenes (opcional)
docker rmi agenda_api prolog_api:latest eureka

# Eliminar la red
docker network rm red_paradigmas
```

## 🐛 Solución de Problemas

### Error: "network with name red_paradigmas already exists"
**Solución:** La red ya existe. Puedes ignorar este error o eliminarla con:
```bash
docker network rm red_paradigmas
```

### Error: "port is already allocated"
**Solución:** Ya hay un contenedor usando ese puerto. Detén el contenedor existente:
```bash
docker stop <nombre_del_contenedor>
```

### Ver logs en tiempo real
```bash
docker logs -f <nombre_del_contenedor>
```

### Contenedor no inicia
```bash
# Ver por qué falló
docker logs <nombre_del_contenedor>

# Reconstruir la imagen
docker build -t <nombre_imagen> . --no-cache
```

## 📦 Estructura del Proyecto

```
2ProyectoPP/
├── eureka-server/          # Servidor de descubrimiento
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── agenda/                 # API REST principal
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── prologAPI-master/       # API de Prolog
│   ├── Dockerfile
│   ├── pom.xml
│   ├── operaciones.pl
│   └── src/
└── paradigmas-front0.0.2/  # Frontend
    └── paradigmas-front/
```

## 🔑 Credenciales

### MySQL
- **Host:** localhost:3306
- **Usuario:** root
- **Password:** 12345678
- **Base de datos:** paradigmas

## 🛠️ Tecnologías Utilizadas

- **Java 21** - Lenguaje de programación
- **Spring Boot** - Framework backend
- **Eureka** - Service Discovery
- **MySQL 8.0** - Base de datos
- **SWI-Prolog** - Motor de lógica
- **Docker** - Contenedorización
- **Maven** - Gestión de dependencias


Este proyecto es para fines educativos.

---

**Nota:** Asegúrate de que Docker Desktop esté corriendo antes de ejecutar cualquier comando.
