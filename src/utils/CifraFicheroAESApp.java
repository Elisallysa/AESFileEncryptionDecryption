package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class containing a method that encrypts a file.
 * 
 * @author elisa
 *
 */
public class CifraFicheroAESApp {

	private static final String SALT = "aBuggyRideAndAPigfoot";

	/**
	 * Alternative method to get an Array of bytes of the File's content to encrypt.
	 * 
	 * @param fileToEncrypt - File containing the message to encrypt
	 * @return - Array of bytes of the File's content
	 */
	public static byte[] readFileToEncrypt(File fileToEncrypt) {
		try {
			return Files.readAllBytes(Paths.get(fileToEncrypt.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method that encrypts File and returns an initialization vector to be used in
	 * decryption on the same file.
	 * 
	 * @param encryptionKey - Encryption key to get the KeySpec.
	 * @param inputFile     - File to encrypt
	 * @param outputFile    - File generated with the encrypted content.
	 * @return - Initialization vector to be used in decryption.
	 */
	public IvParameterSpec encryptFile(String encryptionKey, File inputFile, File outputFile) {

		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// IV is a pseudo-random value and has the same size as the block that is
			// encrypted. SecureRandom class generates a random IV.
			SecureRandom randomSecureRandom = new SecureRandom();
			byte[] iv = new byte[cipher.getBlockSize()];
			randomSecureRandom.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			// SecretKeyFactory class with the PBKDF2WithHmacSHA256 algorithm generates a
			// key from a given password or encryption key
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

			FileInputStream inputStream = new FileInputStream(inputFile);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			byte[] buffer = new byte[64];
			int bytesRead;
			
			// Converts content to bytes and encrypts it and writes it in outputFile
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

			return ivspec;

		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;

	}

}
