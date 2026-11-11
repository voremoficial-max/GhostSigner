import com.android.apksig.ApkSigner;
import com.android.apksig.ApkSigner.SignerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;

public class Firmar {
    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.out.println("Uso: java Firmar <keystore> <password> <alias> <apk_entrada> <apk_salida>");
            System.exit(1);
        }

        String keystorePath = args[0];
        String password = args[1];
        String alias = args[2];
        String apkInput = args[3];
        String apkOutput = args[4];

        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            ks.load(fis, password.toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        SignerConfig signerConfig = new SignerConfig.Builder(
                "CERT", 
                privateKey, 
                Collections.singletonList(cert)
        ).build();

        ApkSigner signer = new ApkSigner.Builder(Collections.singletonList(signerConfig))
                .setInputApk(new File(apkInput))
                .setOutputApk(new File(apkOutput))
                .setV1SigningEnabled(false)
                .setV2SigningEnabled(true)
                .setV3SigningEnabled(true)
                .build();

        signer.sign();
        System.out.println("[✓] APK firmado con éxito: " + apkOutput);
    }
}
