package main;

import java.io.File;

import javax.crypto.spec.IvParameterSpec;

import utils.CifraFicheroAESApp;
import utils.DescifraFicheroAESApp;

/**
 * Classed used to test encryption and decryption classes in the project.
 * @author elisa
 *
 */
public class MainApp {

	public static void main (String[]args) {
	
		// Initializes Encryption & Decryption Apps
		CifraFicheroAESApp cfaes = new CifraFicheroAESApp();
		DescifraFicheroAESApp dfaes = new DescifraFicheroAESApp();
		
		// Text files to encrypt and decrypt
		File inputFile = new File("src/assets/textfile.txt");
		File encryptedFile  = new File("src/assets/encrypted.txt");
		File decryptedFile  = new File("src/assets/decrypted.txt");
		
		// encryptFile() returns the same iv that will be used in the decryption method decryptFile()
		IvParameterSpec iv = cfaes.encryptFile("lalalalal", inputFile,  encryptedFile);
		// Decrypts the encrypted File generated in the previous method encryptFile()
		dfaes.decryptFile("lalalalal", encryptedFile, decryptedFile, iv);
	
	}
	
}
