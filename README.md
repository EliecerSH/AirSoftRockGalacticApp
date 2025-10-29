# AirSoft Rock Galactic - App

Bienvenido a la app de **AirSoft Rock Galactic**, una tienda de comercio electrónico para Android completamente funcional, construida con las últimas tecnologías de Jetpack Compose.

Esta aplicación simula una tienda de equipamiento táctico y réplicas de airsoft, permitiendo a los usuarios registrarse, explorar productos, añadirlos al carrito, realizar compras y gestionar su perfil.


## ✨ Características Principales

-   **Autenticación de Usuarios**: Sistema completo de registro e inicio de sesión.
-   **Catálogo de Productos**: Explora una lista de armas con un buscador integrado.
-   **Carrito de Compras**: Añade productos al carrito, visualiza el desglose de precios (subtotal, IVA y total) y vacía el carrito.
-   **Flujo de Pago Completo**: Un formulario de pago detallado que guarda la transacción en una base de datos local.
-   **Perfil de Usuario Personalizable**:
    -   Edita tu **alias**.
    -   Cambia tu **avatar** seleccionando una imagen de la galería.
    -   Consulta tu **historial de compras**.
-   **Tema Dinámico (Modo Claro/Oscuro)**: Un interruptor en la pantalla de "Cuenta" permite cambiar entre el modo claro y oscuro, y la aplicación recuerda tu elección.
-   **Interfaz Moderna y Unificada**: Un diseño homogéneo y profesional en toda la aplicación.
-   **Almacenamiento Local**: Toda la información (usuarios, productos, carrito, pagos) se gestiona a través de bases de datos **SQLite** locales.

## 🛠️ Tecnologías Utilizadas

-   **Lenguaje**: [Kotlin](https://kotlinlang.org/)
-   **Interfaz de Usuario**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Navegación**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Almacenamiento de Preferencias**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (para el modo oscuro)
-   **Bases de Datos**: [SQLite](https://developer.android.com/training/data-storage/sqlite) a través de `SQLiteOpenHelper`.
-   **Carga de Imágenes**: [Coil](https://coil-kt.github.io/coil/) (para cargar el avatar desde la galería).

## 📂 Estructura del Proyecto

El proyecto está organizado en las siguientes pantallas y componentes clave:

-   `MainActivity.kt`: El punto de entrada de la aplicación. Configura el tema y el `NavHost` principal.
-   `LoginScreen.kt` y `RegisterScreen.kt`: Gestionan la autenticación del usuario.
-   `HomeScreen.kt`: Actúa como la pantalla principal, conteniendo la barra de navegación inferior y el menú lateral deslizable.
-   `WeaponsScreen.kt`: Muestra la lista de productos con una barra de búsqueda funcional.
-   `CartScreen.kt`: Muestra los productos añadidos al carrito y el desglose de precios.
-   `AccountScreen.kt`: Permite al usuario gestionar su perfil, cambiar el tema y ver su historial de compras.
-   `PaymentScreen.kt`: El formulario de pago final.
-   `*DbHelper.kt`: Clases (`UserDbHelper`, `ProductDbHelper`, `CartDbHelper`, `PaymentDbHelper`) que gestionan la creación y las operaciones de las bases de datos SQLite.
-   `ThemeDataStore.kt`: Clase que gestiona el guardado y la lectura de la preferencia del modo oscuro.

## 🚀 Cómo Empezar

Para ejecutar este proyecto en tu entorno local, sigue estos pasos:

1.  **Clona el repositorio**:
    ```sh
    https://github.com/EliecerSH/AirSoftRockGalacticApp.git
    ```
2.  **Abre el proyecto** en Android Studio.
3.  **Sincroniza Gradle**: Espera a que Android Studio descargue y sincronice todas las dependencias del proyecto.
4.  **Ejecuta la aplicación**: Pulsa el botón "Run" (▶️) y selecciona un emulador o un dispositivo físico.

¡Y listo! Ya puedes empezar a probar la aplicación.
