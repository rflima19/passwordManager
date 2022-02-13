package app.passwordmanager.dao;

import java.util.Base64;
import java.util.List;

import app.passwordmanager.model.SenhaServico;
import app.passwordmanager.util.CryptoException;
import app.passwordmanager.util.CryptoUtils;

/**
 * Interface de DAO, para acesso e armazenamento das informações da
 * aplicação.</br>
 * Interface que define as operações sobre {@link SenhaServico}.
 */
public interface SenhaServicoDAO {
	/**
	 * Chave simétrica para ser usada na criptografia
	 */
	final byte[] SECRET_KEY = "LDJGOGDLKJFSDYFK".getBytes();

	/**
	 * Retorna todas as senhas de serviço armazenadas no repositório.
	 * 
	 * @return Todas as senhas de serviço cadastradas
	 */
	public List<SenhaServico> load();

	/**
	 * Armazena todas as senhas de serviço passadas como parâmetro no repositório.
	 * Este método substitui senhas de serviço já cadastradas pelas novas
	 * fornecidas.
	 * 
	 * @param senhasServico
	 *            Senhas de serviço a serem armazenadas
	 */
	public void store(List<SenhaServico> senhasServico);

	/**
	 * Retorna senhas de serviço cujo nome do serviço ou site ou login contenham o
	 * texto passado como parâmetro
	 * 
	 * @param text
	 *            Texto para filtrar os resultados da lista
	 * @return Senhas de serviço filtradas
	 */
	public List<SenhaServico> filter(String text);

	/**
	 * Gera um novo ID para uma senha de serviço
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

			// Converte para codificação Base64
			// A criptografia retorna um array de bytes, ou seja, utilizando esse array
			// trabalhamos com informação binaria. Porem queremos gravar esse dado binario
			// da senha criptografada como formato texto, tanto no arquivo xml, como no
			// banco de dados, e alguns desses bytes podem não ser representados em
			// caracteres. Para isso temos que converter o array de
			// bytes em uma String codificada em caracteres que possam ser armazenados em
			// formato texto e a
			// codificação em base 64 bytes faz o trabalho de conversão.
			// Base64 é um método para codificação de dados para transferência na Internet
			// (codificação MIME para transferência de conteúdo). É utilizado frequentemente
			// para transmitir dados binários por meios de transmissão que lidam apenas com
			// texto. A codificação base64 é frequentemente utilizada quando existe uma
			// necessidade de transferência e armazenamento de dados binários para um
			// dispositivo designado para trabalhar com dados textuais. Esta codificação é
			// amplamente utilizada por aplicações em conjunto com a linguagem de marcação
			// XML, possibilitando o armazenamento de dados binários em forma de texto.
			// O método de Base 64 é constituído por 64 caracteres, sendo eles [A-Za-z0-9, /
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
			// Lê os bytes da String senha que estava armazenada no repositório no formato
			// base64
			byte[] base64Bytes = senha.getBytes();

			// Decodifica o padrão Base64
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