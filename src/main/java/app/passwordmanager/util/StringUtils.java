package app.passwordmanager.util;

/**
 * Classe com m�todos utilit�rios para manipular Strings.
 */
public class StringUtils {

	/**
	 * Verifica se uma String � vazia ou composta apenas por espa�os em branco.
	 * 
	 * @param str
	 *            String a ser analizada
	 * @return True caso a String seja vazia, false caso contr�rio.
	 */
	public static final boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.trim().length() <= 0;
	}

	/**
	 * Retorna o quebra/separador de linha correspondente ao sistema operacional que
	 * a JVM esta sendo executada.
	 * 
	 * @return Os caracteres que representa a quebra de linha no sistema
	 *         operacional.
	 */
	public static final String newLine() {
		return System.getProperty("line.separator");
		/*
		 * Quando voc� executa um programa voc� "inicia" uma inst�ncia da JVM e esta
		 * inst�ncia possui suas pr�prias vari�veis de ambientes, algumas propriedades
		 * sendo inicializadas de forma nativa (m�todo nativo initProperties). Na
		 * implementa��o de System voc� percebe que existe uma vari�vel est�tica de
		 * Properties, que � o objeto que cont�m todas as propriedades de sistema e �
		 * acess�vel por qualquer aplica��o/biblioteca rodando sobre a inst�ncia da JVM.
		 * 
		 * O m�todo System.setProperty(String key, String value)
		 * 
		 * Sets the system property indicated by the specified key.
		 * 
		 * Ou seja, este m�todo cria ou altera o valor de uma propriedade de sistema que
		 * s�o compartilhadas por todas as aplica��es/bibliotecas executando sobre a
		 * mesma inst�ncia da JVM.
		 * 
		 * O m�todo System.getProperty(String key)
		 * 
		 * Retorna uma propriedade do sistema.
		 * 
		 */
	}
}
