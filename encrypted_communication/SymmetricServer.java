/* Simple Chat Server */

import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricServer {

        private SecretKey key;
        
        public SymmetricServer( byte[] decodedKey){
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

	public static void main(String args[]) throws Exception
	{
		BufferedReader inkey = new BufferedReader(new FileReader("key.txt"));
                byte[] decodedKey = Base64.getDecoder().decode(inkey.readLine());
                //String decodedKey = inkey.readLine();
                SymmetricServer key1 = new SymmetricServer(decodedKey);
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
                        messagereturn = key1.encrypt("\"You send me ->" +key1.decrypt(message) +  "\"\n");
			outstr.writeBytes(messagereturn);
                        connsock.close();
		}
	}

}
