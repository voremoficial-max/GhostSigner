import os

def patch_local_file_record():
    ruta = "apksig/src/main/java/com/android/apksig/internal/zip/LocalFileRecord.java"
    with open(ruta, "r") as f:
        content = f.read()

    content = content.replace(
        "if (dataEndOffset > cdStartOffset) {",
        "if (false) { // PATCH"
    )
    content = content.replace(
        "if (mDataCompressed) {",
        "if (false) { // PATCH"
    )

    with open(ruta, "w") as f:
        f.write(content)
    print("[+] LocalFileRecord.java parcheado.")

def patch_apk_utils():
    ruta = "apksig/src/main/java/com/android/apksig/apk/ApkUtils.java"
    with open(ruta, "r") as f:
        content = f.read()

    inicio = content.find("public static int getMinSdkVersionFromBinaryAndroidManifest(")
    if inicio != -1:
        fin = content.find("private static class CodenamesLazyInitializer", inicio)
        nuevo_metodo = """public static int getMinSdkVersionFromBinaryAndroidManifest(
            ByteBuffer androidManifestContents) throws MinSdkVersionException {
        return 21;
    }

    """
        content = content[:inicio] + nuevo_metodo + content[fin:]
        with open(ruta, "w") as f:
            f.write(content)
        print("[+] ApkUtils.java parcheado.")

if __name__ == "__main__":
    if not os.path.exists("apksig"):
        print("[!] El directorio apksig no existe. Ejecuta setup.sh primero.")
        exit(1)
    
    patch_local_file_record()
    patch_apk_utils()
