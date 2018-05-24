
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;



public class KeyPairCreator {
    private Map<String, Object> keyMap;
    private final String RSA_ALGORITHM = "RSA";
    private final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private String RSA_PUBLIC_KEY = "RSAPublicKey";
    private String RSA_PRIVATE_KEY = "RSAPrivateKey";
    
    public String encodeBase64(byte[] binaryData) {
        return Base64.encode(binaryData);
    }

    public byte[] decodeBase64(String encoded) throws Exception {
        return Base64.decode(encoded);
    }
    public KeyPairCreator() throws Exception {
        keyMap = new HashMap<String, Object>(2);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        keyMap.put(RSA_PUBLIC_KEY, publicKey);
        keyMap.put(RSA_PRIVATE_KEY, privateKey);
    }
      public String getPublicKey() {
        Key key = (Key) keyMap.get(RSA_PUBLIC_KEY);
        return encodeBase64(key.getEncoded());
    }
      public String getPrivateKey() {
        Key key = (Key) keyMap.get(RSA_PRIVATE_KEY);
        return encodeBase64(key.getEncoded());
    }
    
    public static void main (String[] args) throws Exception {
        KeyPairCreator key1 = new KeyPairCreator();
        String privatekey = key1.getPrivateKey();
        String publickey = key1.getPublicKey();
        System.out.print(privatekey);
        System.out.print(publickey);
        FileWriter out = new FileWriter("privateKey.txt");
        out.write(privatekey);
        out.close();
        FileWriter out1 = new FileWriter("publicKey.txt");
        out1.write(publickey);
        out1.close();
    }
    
}
