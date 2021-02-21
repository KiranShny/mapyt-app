# Mapyt App

Con esta app podrás encontrar y guardar puntos de interés de manera rápida y sencilla.

## Descargar 

[Obtener APK](resources/app-debug.apk)

## Tech Stack

Kotlin, XML, Google Play Services, Timber, LiveData, ViewModel, GSON, Retrofit, Picasso, JUnit, Mockk...

## Arquitectura

- Se realizó una aproximación básica de Clean Architecture aplicando intrinsecamente principios SOLID (WIP)
- Se implementaron patrones de diseño como Repository, Singleton, MVVM.
- Por ahora la organización de los elementos está por componente y no por feature/funcionalidad. Esto sería un punto de mejora.

### Tests

Debido a la falta de conocimiento en el funcionamiento de la API de Google, opté por enfocarme en esta capa: 

- Desarrollar automated tests en Postman (test scripts) para explorar y asimilar la estructura de los endpoints. La colección y pruebas están disponibles en `/api-testing`

- Desarrollar automated tests con `JUnit` y `Mockk` para validar que tareas como estructuras de datos y deserialización funcionaran correctamente. Utilicé `MockWebServer` para no depender de la API real al correr los tests. Ver en [`test/core/domain`](https://github.com/soyjimmysaenz/mapyt-app/tree/main/mapyt/core/src/test/java/me/mapyt/app/core/domain)

## Configuración

En la raíz del proyecto crear un archivo `app_settings.properties` e incluir las llaves de Google Platform requeridas:

```
GPLACES_KEY="LLAVE PARA GOOGLE PLACES"
GMAPS_KEY={LLAVE PARA GOOGLE MAPS}
```

> **Nota**: separé las llaves debido a que la llave que disponía de Google Places no tenía habilitado Google Maps. Para efectos prácticos se podría usar la misma llave en ambas propiedades.

## Proceso

- Crear Wireframes para plantear un modelo mental rápido de la solución y una idea base de la UI.
- Organizar Repositorio Git y Project Board (issues based) en GitHub.
- Explorar API de Google Places (Postman) debido a la falta de conocimiento en ella sin el SDK.
- Desarrollar organizacion y arquitectura base de proyecto.
- Desarrollar automated tests.
- Programar las características secuencialmente.

## TODO

- [ ] Guardar y listar Favoritos
- [ ] Cargar reseñas de usuarios en detalle
- [ ] Mejorar UI de Detalle

--

- [ ] Validar conexión a internet
- [ ] Mejorar UI de markers en mapa
- [ ] Mejorar organización (por feature)
- [ ] Soportar Dark Mode

## Screenshots

![Screenshot 1](https://github.com/soyjimmysaenz/mapyt-app/blob/main/resources/sc1.jpg)

---

:coffee: + :blue_heart: -> [SoSafe](https://sosafeapp.com/)
