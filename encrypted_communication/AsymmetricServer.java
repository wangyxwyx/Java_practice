/* Simple Chat Server */

import java.security.interfaces.RSAPrivateKey;
import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class AsymmetricServer {

        RSAPrivateKey priKey;
        private final String RSA_ALGORITHM = "RSA";        

        public AsymmetricServer( byte[] keyBytes)throws Exception{
                 PKCS8EncodedKeySpec  keySpec = new PKCS8EncodedKeySpec(keyBytes);
                 KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
                 priKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        }
        
        public String decrypt(String data) throws Exception{
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, priKey);
		byte[] c = decodeBase64(data);
		byte[] result = cipher.doFinal(c);
		String plainText = new String(result, "UTF-8");
		return plainText;
        }
        
        public String encodeBase64(byte[] binaryData) {
                return Base64.getEncoder().encodeToString(binaryData);
        }

        public byte[] decodeBase64(String encoded) throws Exception {
                return Base64.getDecoder().decode(encoded);
        }
        
	public static void main(String args[]) throws Exception
	{
		BufferedReader inkey = new BufferedReader(new FileReader("privateKey.txt"));
                byte[] decodedKey = Base64.getDecoder().decode(inkey.readLine());
                AsymmetricServer key1 = new AsymmetricServer(decodedKey);
                String message;
                String messagereturn;
		ServerSocket serversock = new ServerSocket(19000); //can be any port
		while(true)
		{
                        Socket connsock = serversock.accept();
			InputStreamReader instr =  new InputStreamReader(connsock.getInputStream());
			DataOutputStream outstr = new DataOutputStream(connsock.getOutputStream());
			BufferedReader in = new BufferedReader(instr);
                        message = in.readLine();
                        System.out.println("Client sent: " + key1.decrypt(message));
	                messagereturn = "\"You send me ->" +key1.decrypt(message) + "->Goodbye!" + "\"\n"; 
			outstr.writeBytes(messagereturn);
                        //messagereturn = "";
		}
	}

}
