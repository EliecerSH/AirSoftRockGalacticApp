# AirSoft Rock Galactic - App

Bienvenido a la app de **AirSoft Rock Galactic**, una tienda de comercio electrónico para Android completamente funcional, construida con las últimas tecnologías de Jetpack Compose.

Esta aplicación simula una tienda de equipamiento táctico y réplicas de airsoft, permitiendo a los usuarios registrarse, explorar productos, añadirlos al carrito, realizar compras, gestionar su perfil y participar en una sección de comentarios.

## ✨ Características Principales

-   **Autenticación de Usuarios**: Sistema completo de registro e inicio de sesión.
-   **Catálogo de Productos**: Explora una lista de armas con un buscador integrado.
-   **Carrito de Compras**: Añade productos al carrito, visualiza el desglose de precios (subtotal, IVA y total) y vacía el carrito.
-   **Flujo de Pago Completo**: Un formulario de pago detallado que guarda la transacción en una base de datos local.
-   **Perfil de Usuario Personalizable**:
    -   Edita tu **alias**.
    -   Cambia tu **avatar** seleccionando una imagen de la galería.
    -   Consulta tu **historial de compras**.
-   **Sección de Comentarios (API)**: Una sección de comentarios que consume una API REST externa.
-   **Tema Dinámico (Modo Claro/Oscuro)**: Un interruptor en la pantalla de "Cuenta" permite cambiar entre el modo claro y oscuro, y la aplicación recuerda tu elección.
-   **Interfaz Moderna y Unificada**: Un diseño homogéneo y profesional en toda la aplicación.
-   **Almacenamiento Local**: La información de usuarios, productos, carrito y pagos se gestiona a través de bases de datos **SQLite** locales.

## 🛠️ Tecnologías Utilizadas

-   **Lenguaje**: [Kotlin](https://kotlinlang.org/)
-   **Interfaz de Usuario**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Navegación**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Comunicación con API**: [Retrofit](https://square.github.io/retrofit/) (para peticiones a la API REST).
-   **Almacenamiento de Preferencias**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (para el modo oscuro).
-   **Bases de Datos**: [SQLite](https://developer.android.com/training/data-storage/sqlite) a través de `SQLiteOpenHelper`.
-   **Carga de Imágenes**: [Coil](https://coil-kt.github.io/coil/) (para cargar el avatar desde la galería).
-   **Pruebas (Testing)**: [JUnit](https://junit.org/junit5/), [Mockito](https://site.mockito.org/) y [Espresso](https://developer.android.com/training/testing/espresso).

## 🌐 Integración con API

La aplicación incluye una sección de comentarios que se comunica con una API RESTful externa para obtener y enviar datos. Esta funcionalidad está gestionada por el `MainViewModel`.

-   **Tecnología**: Se utiliza **Retrofit** para gestionar las peticiones de red de forma eficiente.
-   **URL Base de la API**: `https://cliente-service-arg.onrender.com/`

## 🧪 Pruebas (Testing)

El proyecto cuenta con una base sólida de tests unitarios y de interfaz de usuario para garantizar la calidad y el correcto funcionamiento de las características clave:

-   `MainViewModelTest`: Test unitario que verifica la lógica de carga de comentarios desde la API, utilizando **Mockito** para simular el servicio de red en casos de éxito y error.
-   `LoginScreenTest`: Test de UI que valida el flujo de autenticación, cubriendo tanto el inicio de sesión exitoso como el fallido.
-   `CartScreenTest`: Test de UI que prueba la funcionalidad del carrito de la compra, incluyendo el estado de carrito vacío, la adición de productos, el cálculo de totales, y los botones de "Vaciar" y "Pagar".
-   `PaymentScreenTest`: Test de UI que asegura el correcto funcionamiento de la pantalla de pago, validando que los campos vacíos impiden el pago y que un formulario completo guarda los datos en la base de datos y navega a la pantalla final.

## 📂 Estructura del Proyecto

El proyecto está organizado en las siguientes pantallas y componentes clave:

-   `MainActivity.kt`: Punto de entrada de la aplicación.
-   **/screen**: Contiene todas las pantallas de la aplicación (`LoginScreen`, `WeaponsScreen`, `CartScreen`, `PaymentScreen`, etc.).
-   **/data**: Incluye las clases `*DbHelper` que gestionan las bases de datos SQLite.
-   **/network**: Contiene la interfaz de `ApiComentarioService` para Retrofit.
-   `MainViewModel.kt`: Gestiona la lógica de la sección de comentarios.
-   `/src/androidTest`: Contiene los tests de UI (`LoginScreenTest`, `CartScreenTest`, `PaymentScreenTest`).
-   `/src/test`: Contiene los tests unitarios (`MainViewModelTest`).

## 🚀 Cómo Empezar

Para ejecutar este proyecto en tu entorno local, sigue estos pasos:

1.  **Clona el repositorio**:
    ```sh
    https://github.com/EliecerSH/AirSoftRockGalacticApp.git
    ```
2.  **Abre el proyecto** en Android Studio.
3.  **Sincroniza Gradle**: Espera a que Android Studio descargue y sincronice todas las dependencias.
4.  **Ejecuta la aplicación**: Pulsa el botón "Run" (▶️) y selecciona un emulador o un dispositivo físico.

¡Y listo! Ya puedes empezar a probar la aplicación.
