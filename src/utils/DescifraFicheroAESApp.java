package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class containing a method that decrypts an encrypted file.
 * 
 * @author elisa
 *
 */
public class DescifraFicheroAESApp {

	private static final String SALT = "aBuggyRideAndAPigfoot"; // Random String used to generate a KeySpec

	/**
	 * Decrypts a File and creates a new File with the decrypted message.
	 * 
	 * @param encryptionKey - String with encryption key
	 * @param inputFile     - File containing encrypted message to be decrypted
	 * @param password      - String with password
	 * @param outputFile    - new File where decrypted message will be saved
	 */
	public void decryptFile(String encryptionKey, File inputFile, File outputFile, IvParameterSpec ivspec) {

		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// SecretKeyFactory class with the PBKDF2WithHmacSHA256 algorithm generates a
			// key from a given password or encryption key
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

			FileInputStream inputStream = new FileInputStream(inputFile);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			byte[] buffer = new byte[64];
			int bytesRead;
			
			// Reads bytes and decrypts message and writes it in output file
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byte[] output = cipher.update(buffer, 0, bytesRead);
				if (output != null) {
					outputStream.write(output);
				}
			}
			byte[] outputBytes = cipher.doFinal();
			if (outputBytes != null) {
				outputStream.write(outputBytes);
			}
			inputStream.close();
			outputStream.close();

		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}

	}
}
