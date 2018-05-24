import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class SymmetricClient {
        
        private SecretKey key;
        
        public SymmetricClient( byte[] decodedKey){
                //String str = new String(decodedKey);
                //key = new SecretKeySpec(decodedKey.getBytes(), "AES"); 
               key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        }
        
        public String encrypt(String data) throws Exception{
                 Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                 cipher.init(Cipher.ENCRYPT_MODE, key);
                 byte[] p = data.getBytes("UTF-8");
	         byte[] result = cipher.doFinal(p);
	         String encoded = encodeBase64(result);
	         return encoded;  
        }
	
        public String decrypt(String data) throws Exception{
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);
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

        public static void main(String[] args) throws Exception{
		BufferedReader inkey = new BufferedReader(new FileReader("key.txt"));
                byte[] decodedKey = Base64.getDecoder().decode(inkey.readLine());
                //String decodedKey = inkey.readLine();
                SymmetricClient key1 = new SymmetricClient(decodedKey);
                String	message;
		String	returnmessage;
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//server has to be listening to this port
		Socket mysock = new Socket("localhost", 19000); 
		DataOutputStream out = new DataOutputStream( mysock.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
		message = keyboard.readLine();
		out.writeBytes(key1.encrypt(message) + "\n");
                returnmessage = in.readLine();
                System.out.println("Server replied: " + key1.decrypt(returnmessage));
                mysock.close();
	}
	

}
