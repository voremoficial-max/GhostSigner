# GhostSigner

Firmador APK personalizado (v2/v3) basado en el código fuente oficial de `apksig` de AOSP. 

## ¿Qué es lo que puede hacer? (Confirmado)

Esta herramienta no rompe la criptografía de Android. Lo que hace es saltarse las validaciones estructurales que el firmador oficial (`apksigner`) aplica a los archivos ZIP antes de firmar.

Confirmado en laboratorio:
- Firmar APKs con estructura ZIP malformada: Permite firmar APKs que tienen discrepancias en los tamaños de descompresión, solapamiento de directorios o Data Descriptors inválidos. El firmador oficial de Google rechaza estos archivos; GhostSigner los firma con éxito.
- Firmar APKs con recursos malformados: Permite firmar APKs cuyo `AndroidManifest.xml` o `resources.arsc` hayan sido modificados a nivel binario para inyectar vulnerabilidades, omitiendo las validaciones de lectura de recursos.
- Firma V2 y V3: Mantiene la compatibilidad con dispositivos modernos generando los bloques de firma APK Signature Scheme v2 y v3, omitiendo la firma v1 (JAR).
- Instalación en Android: Los APKs firmados con esta herramienta pasan la verificación de certificados del instalador de Android (Package Manager).

Lo que NO hace:
- No evade la detección de Google Play Protect a nivel de análisis estático.
- No permite instalar APKs que el propio sistema operativo rechace por errores de parseo fatales (ej. un AndroidManifest.xml completamente ilegible).

## Requisitos del sistema

- Java Development Kit (JDK) 17 o superior.
- Git y Gradle (se instalan vía terminal).
- Python 3 (para aplicar los parches automáticos).

## Instalación paso a paso

Sigue estos comandos en tu terminal (Linux, macOS, Termux o WSL en Windows).

Paso 1. Clonar el repositorio y entrar a la carpeta:
git clone https://github.com/voremoficial-max/GhostSigner.git
cd GhostSigner

Paso 2. Descargar el código fuente base de Google:
git clone https://android.googlesource.com/platform/tools/apksig

Paso 3. Aplicar los parches de GhostSigner:
python3 patch.py
(Deberías ver un mensaje indicando que LocalFileRecord.java y ApkUtils.java fueron parcheados)

Paso 4. Compilar el firmador parcheado:
cd apksig
gradle build -x test

Paso 5. Preparar el ejecutable:
cd ..
cp apksig/build/libs/apksig.jar .
javac -cp apksig.jar Firmar.java

## Uso

Para firmar un APK, ejecuta el siguiente comando reemplazando las rutas por las tuyas:

java -cp apksig.jar:. Firmar <ruta_keystore> <password> <alias> <apk_entrada> <apk_salida>

Ejemplo:
java -cp apksig.jar:. Firmar mi_keystore.jks mi_clave mi_alias app_malformada.apk app_firmada.apk

Si todo sale bien, verás el mensaje de éxito en la terminal.

## Aviso de uso y responsabilidad

Esta herramienta fue creada con fines de investigación de seguridad, auditoría de firmas digitales y análisis de vulnerabilidades en entornos de laboratorio controlados. Al utilizarla, aceptas que eres el único responsable del uso que le des y de las consecuencias legales que de ello deriven. No debes utilizarla para firmar software malicioso destinado a infectar o dañar dispositivos de terceros sin su consentimiento explícito. Los autores no se hacen responsables del mal uso de la herramienta ni de los daños que esta pueda causar. Esta herramienta se proporciona "tal cual", sin garantías de ningún tipo.
