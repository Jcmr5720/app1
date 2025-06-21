# Hola Mundo Android App

Este proyecto es un ejemplo mínimo de una aplicación Android que muestra
"Hola Mundo :)" en pantalla.

## Cómo compilar

1. Asegúrate de tener [Android Studio](https://developer.android.com/studio) o Gradle instalado.
2. Si el archivo `gradle/wrapper/gradle-wrapper.jar` no está presente, ejecuta `gradle wrapper` para generarlo.
3. Este proyecto usa AndroidX. Si es la primera vez que lo clonas, crea el archivo `gradle.properties` con:

   ```
   android.useAndroidX=true
   android.enableJetifier=true
   ```
4. Luego puedes compilar el proyecto con:

```bash
./gradlew assembleDebug
```

