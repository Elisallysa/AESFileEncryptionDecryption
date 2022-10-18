package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CifraFicheroAESApp {

	private static final String SALT = "aBuggyRideAndAPigfoot";

	
	public static void main (String[] args) {
		File inputFile = new File("src/assets/textfile.txt");
		File encryptedFile  = new File("src/assets/encrypted.txt");
		
		encryptFile(inputFile, "lalalalal", encryptedFile);
		
	}
	
	public static byte[] readFileToEncrypt(File fileToEncrypt) {
		try {
			return Files.readAllBytes(Paths.get(fileToEncrypt.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public File writeEncryptedFile(String content) {
		File f = new File("file.txt");
		return f;
	}
	
	public static void encryptFile(File inputFile, String password, File outputFile) {
		
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			String encryptionKey = "hola";
			SecureRandom randomSecureRandom = new SecureRandom();
			byte[] iv = new byte[cipher.getBlockSize()];
			randomSecureRandom.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

			FileInputStream inputStream = new FileInputStream(inputFile);
		    FileOutputStream outputStream = new FileOutputStream(outputFile);
		    byte[] buffer = new byte[64];
		    int bytesRead;
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
