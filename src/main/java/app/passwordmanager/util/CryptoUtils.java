package app.passwordmanager.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Classe com m�todos utilit�rios de criptografia de dados.
 */
public class CryptoUtils {

	/**
	 * Criptografa texto usando o algoritmo Advanced Encryption Standard (AES).</b>
	 * O algoritmo AES � um algoritmo de chave sim�trica, ou seja, a mesma chave �
	 * usada para criptografar e descriptografar os dados.
	 * 
	 * @param keyBytes
	 *            Bytes da chave sim�trica.
	 * @param dataBytes
	 *            Bytes dos dados a serem criptografados.
	 * @return Bytes criptografados.
	 * @throws CryptoException
	 */
	public static byte[] encryptAES(byte[] keyBytes, byte[] dataBytes) {
		return CryptoUtils.handlerAES(keyBytes, dataBytes, Cipher.ENCRYPT_MODE);
	}

	/**
	 * Descriptografa texto usando o algoritmo AES
	 * 
	 * @param keyBytes
	 *            Bytes da chave sim�trica
	 * @param dataBytes
	 *            Bytes dos dados
	 * @return Bytes descriptografados
	 * @throws CryptoException
	 */
	public static byte[] decryptAES(byte[] keyBytes, byte[] dataBytes) {
		return CryptoUtils.handlerAES(keyBytes, dataBytes, Cipher.DECRYPT_MODE);
	}

	/**
	 * Criptografa ou descriptografa texto usando o algoritmo AES
	 * 
	 * @param keyBytes
	 *            Bytes da chave sim�trica
	 * @param dataBytes
	 *            Bytes dos dados
	 * @param mode
	 *            Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE
	 * @return Bytes criptografados ou descriptografados
	 * @throws CryptoException
	 */
	private static byte[] handlerAES(byte[] keyBytes, byte[] dataBytes, int mode) {
		// verifica se a chave � v�lida. Uma chave v�lida deve ter 16 bytes.
		if ((keyBytes == null) || (keyBytes.length != 16)) {
			throw new CryptoException("Chave simetrica invalida!");
		}

		// verifica se os bytes a serem manipulados s�o nulos
		if (dataBytes == null) {
			throw new CryptoException("Dados nulos!");
		}

		try {
			// cria o objeto da chave sim�trica
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

			// obtem uma inst�ncia do AES
			Cipher cipher = Cipher.getInstance("AES");

			// inicializa o algoritmo
			cipher.init(mode, key);

			// Executa a opera��o de acordo com o mode escolhido
			return cipher.doFinal(dataBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new CryptoException(e);
		}
	}

}
