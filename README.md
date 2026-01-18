# 🏎️ DriveN'Dodge – Backend REST API

![Platform](https://img.shields.io/badge/Platform-Java-blue)
![API](https://img.shields.io/badge/API-JAX--RS%20%2F%20Jersey-orange)
![Database](https://img.shields.io/badge/Database-MariaDB-lightblue)
![Server](https://img.shields.io/badge/Server-Grizzly-green)
![Docs](https://img.shields.io/badge/Docs-Swagger-brightgreen)

> **DriveN'Dodge Backend** es el servidor REST del ecosistema DriveN’Dodge, responsable de la lógica de negocio, la persistencia de datos y la comunicación con el cliente Android y la interfaz web.

---

## 🌐 Descripción del Proyecto

Este repositorio contiene el **backend del proyecto DriveN'Dodge**, desarrollado en **Java** utilizando **JAX-RS (Jersey)** y **MariaDB**.

El backend expone una **API REST versionada (`/v1`)** que permite a los clientes autenticarse, gestionar usuarios, clanes, inventario, eventos, ranking y resultados de partidas, además de servir una interfaz web estática y documentación Swagger.

---

### ✨ Características Principales

* **🔐 Autenticación de Usuarios:** Registro y login contra base de datos MariaDB.
* **👤 Gestión de Perfil:** Datos personales, imagen de perfil, monedas y puntuación.
* **🛒 Tienda (Shop):** Compra de ítems y gestión de monedas.
* **🎒 Inventario Persistente:** Relación usuario–ítems con cantidades.
* **🏰 Sistema de Clanes:** Creación, unión y ranking de clanes.
* **📅 Eventos:** Consulta e información de eventos.
* **🏆 Ranking Global:** Clasificación de usuarios por puntuación.
* **🎮 Gestión de Partidas:** Recepción de resultados desde el cliente Unity/Android.
* **🤖 Chat e Integración IA:** Servicio de chat accesible desde web y Android.
* **📖 Documentación Automática:** API documentada mediante Swagger.
* **🌍 Interfaz Web:** Servido de HTML, CSS y JavaScript desde el propio backend.

---

## 🛠️ Stack Tecnológico

La arquitectura está basada en un **patrón de capas**, separando claramente API, lógica de negocio y acceso a datos.

| Tecnología | Uso |
| :--- | :--- |
| **Java** | Lenguaje principal del backend |
| **JAX-RS (Jersey)** | Implementación de servicios REST |
| **Grizzly HTTP Server** | Servidor embebido |
| **MariaDB** | Base de datos relacional |
| **JDBC + DAO** | Acceso a datos |
| **Swagger** | Documentación de la API |
| **Maven** | Gestión de dependencias |
| **IntelliJ IDEA** | Entorno de desarrollo |

---

## 📂 Estructura del Proyecto

El código está organizado por capas para facilitar la mantenibilidad y escalabilidad:

```text
src/main/java
├── 📁 services        # Endpoints REST (AuthService, ShopService, ClanService…)
│   └── 📁 DTOs        # Objetos de transferencia de datos
├── 📁 manager         # Lógica de negocio
├── 📁 db
│   └── 📁 orm
│       ├── 📁 dao     # Interfaces DAO e implementaciones
│       ├── 📁 model   # Entidades (Usuario, Clan, Item, Evento…)
│       └── 📁 util    # Gestión de sesiones y conexión a BD
├── 📄 Main.java       # Arranque del servidor
```

---

## 🔗 Enlaces del Proyecto
Este repositorio trabaja en conjunto con el servidor Backend:

🌐 Android Repository: https://github.com/pol-p/dsa-driveNdodge-android.git

---

## 👥 Autores

Proyecto desarrollado por el equipo de **DSA - UPC**:

* **Pablo Casado**
* **Pablo Santamaría**
* **Arnau Munté**
* **Paula Tolosa**
* **Pol Puig**
