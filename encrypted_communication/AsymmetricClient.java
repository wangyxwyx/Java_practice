

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import java.security.interfaces.RSAPublicKey;

public class AsymmetricClient {
        
        RSAPublicKey pubKey;
        
        public AsymmetricClient(byte[] keyBytes)throws Exception{
                 X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                 KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                 pubKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        }
        
        public String encrypt(String data) throws Exception{
                 Cipher cipher = Cipher.getInstance("RSA");
                 cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                 
                 byte[] p = data.getBytes("UTF-8");
	         byte[] result = cipher.doFinal(p);
	         String encoded = encodeBase64(result);
	         return encoded;  
        }
	
         public String encodeBase64(byte[] binaryData) {
                return Base64.getEncoder().encodeToString(binaryData);
        }

        public byte[] decodeBase64(String encoded) throws Exception {
                return Base64.getDecoder().decode(encoded);
        }
        
        public static void main(String[] args) throws Exception{
		BufferedReader inkey = new BufferedReader(new FileReader("publicKey.txt"));
                byte[] decodedKey = Base64.getDecoder().decode(inkey.readLine());
                AsymmetricClient key1 = new AsymmetricClient(decodedKey);
                String	message;
		String	returnmessage;
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//server has to be listening to this port
		Socket mysock = new Socket("localhost", 19000); 
		DataOutputStream out = new DataOutputStream( mysock.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
                message = keyboard.readLine();
                out.writeBytes(key1.encrypt(message)+ "\n");
                //message = "";
		returnmessage = in.readLine();
		System.out.println("Server replied: " + returnmessage);
		mysock.close();
	}
	

}
