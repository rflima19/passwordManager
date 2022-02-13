package app.passwordmanager.dao;

import java.util.Base64;
import java.util.List;

import app.passwordmanager.model.SenhaServico;
import app.passwordmanager.util.CryptoException;
import app.passwordmanager.util.CryptoUtils;

/**
 * Interface de DAO, para acesso e armazenamento das informa��es da
 * aplica��o.</br>
 * Interface que define as opera��es sobre {@link SenhaServico}.
 */
public interface SenhaServicoDAO {
	/**
	 * Chave sim�trica para ser usada na criptografia
	 */
	final byte[] SECRET_KEY = "LDJGOGDLKJFSDYFK".getBytes();

	/**
	 * Retorna todas as senhas de servi�o armazenadas no reposit�rio.
	 * 
	 * @return Todas as senhas de servi�o cadastradas
	 */
	public List<SenhaServico> load();

	/**
	 * Armazena todas as senhas de servi�o passadas como par�metro no reposit�rio.
	 * Este m�todo substitui senhas de servi�o j� cadastradas pelas novas
	 * fornecidas.
	 * 
	 * @param senhasServico
	 *            Senhas de servi�o a serem armazenadas
	 */
	public void store(List<SenhaServico> senhasServico);

	/**
	 * Retorna senhas de servi�o cujo nome do servi�o ou site ou login contenham o
	 * texto passado como par�metro
	 * 
	 * @param text
	 *            Texto para filtrar os resultados da lista
	 * @return Senhas de servi�o filtradas
	 */
	public List<SenhaServico> filter(String text);

	/**
	 * Gera um novo ID para uma senha de servi�o
	 * 
	 * @return ID gerado
	 */
	public int generateId();

	/**
	 * Criptografa uma senha
	 * 
	 * @param senha
	 *            Senha a ser criptografada
	 * @return Senha criptografada
	 * @throws CryptoException
	 */
	public default String encrypt(String senha) {
		try {
			// Criptografa
			byte[] encBytes = CryptoUtils.encryptAES(SenhaServicoDAO.SECRET_KEY, senha.getBytes());

			// Converte para codifica��o Base64
			// A criptografia retorna um array de bytes, ou seja, utilizando esse array
			// trabalhamos com informa��o binaria. Porem queremos gravar esse dado binario
			// da senha criptografada como formato texto, tanto no arquivo xml, como no
			// banco de dados, e alguns desses bytes podem n�o ser representados em
			// caracteres. Para isso temos que converter o array de
			// bytes em uma String codificada em caracteres que possam ser armazenados em
			// formato texto e a
			// codifica��o em base 64 bytes faz o trabalho de convers�o.
			// Base64 � um m�todo para codifica��o de dados para transfer�ncia na Internet
			// (codifica��o MIME para transfer�ncia de conte�do). � utilizado frequentemente
			// para transmitir dados bin�rios por meios de transmiss�o que lidam apenas com
			// texto. A codifica��o base64 � frequentemente utilizada quando existe uma
			// necessidade de transfer�ncia e armazenamento de dados bin�rios para um
			// dispositivo designado para trabalhar com dados textuais. Esta codifica��o �
			// amplamente utilizada por aplica��es em conjunto com a linguagem de marca��o
			// XML, possibilitando o armazenamento de dados bin�rios em forma de texto.
			// O m�todo de Base 64 � constitu�do por 64 caracteres, sendo eles [A-Za-z0-9, /
			// e + que deram origem ao seu nome.
			byte[] base64Bytes = Base64.getEncoder().encode(encBytes);

			// Retorna como String
			return new String(base64Bytes);

		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * Descriptografa um senha
	 * 
	 * @param senha
	 *            Senha a ser descriptografada
	 * @return Senha descriptografada
	 * @throws CryptoException
	 */
	default String decrypt(String senha) {
		try {
			// L� os bytes da String senha que estava armazenada no reposit�rio no formato
			// base64
			byte[] base64Bytes = senha.getBytes();

			// Decodifica o padr�o Base64
			byte[] encBytes = Base64.getDecoder().decode(base64Bytes);

			// Descriptografa
			byte[] decBytes = CryptoUtils.decryptAES(SenhaServicoDAO.SECRET_KEY, encBytes);

			// Retorna como String
			return new String(decBytes);

		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

}