package app.passwordmanager.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Classe com métodos utilitários de criptografia de dados.
 */
public class CryptoUtils {

	/**
	 * Criptografa texto usando o algoritmo Advanced Encryption Standard (AES).</b>
	 * O algoritmo AES é um algoritmo de chave simétrica, ou seja, a mesma chave é
	 * usada para criptografar e descriptografar os dados.
	 * 
	 * @param keyBytes
	 *            Bytes da chave simétrica.
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
	 *            Bytes da chave simétrica
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
	 *            Bytes da chave simétrica
	 * @param dataBytes
	 *            Bytes dos dados
	 * @param mode
	 *            Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE
	 * @return Bytes criptografados ou descriptografados
	 * @throws CryptoException
	 */
	private static byte[] handlerAES(byte[] keyBytes, byte[] dataBytes, int mode) {
		// verifica se a chave é válida. Uma chave válida deve ter 16 bytes.
		if ((keyBytes == null) || (keyBytes.length != 16)) {
			throw new CryptoException("Chave simetrica invalida!");
		}

		// verifica se os bytes a serem manipulados são nulos
		if (dataBytes == null) {
			throw new CryptoException("Dados nulos!");
		}

		try {
			// cria o objeto da chave simétrica
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

			// obtem uma instância do AES
			Cipher cipher = Cipher.getInstance("AES");

			// inicializa o algoritmo
			cipher.init(mode, key);

			// Executa a operação de acordo com o mode escolhido
			return cipher.doFinal(dataBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new CryptoException(e);
		}
	}

}
