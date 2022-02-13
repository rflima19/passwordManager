package app.passwordmanager.util;

/**
 * Classe com métodos utilitários para manipular Strings.
 */
public class StringUtils {

	/**
	 * Verifica se uma String é vazia ou composta apenas por espaços em branco.
	 * 
	 * @param str
	 *            String a ser analizada
	 * @return True caso a String seja vazia, false caso contrário.
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
		 * Quando você executa um programa você "inicia" uma instância da JVM e esta
		 * instância possui suas próprias variáveis de ambientes, algumas propriedades
		 * sendo inicializadas de forma nativa (método nativo initProperties). Na
		 * implementação de System você percebe que existe uma variável estática de
		 * Properties, que é o objeto que contém todas as propriedades de sistema e é
		 * acessível por qualquer aplicação/biblioteca rodando sobre a instância da JVM.
		 * 
		 * O método System.setProperty(String key, String value)
		 * 
		 * Sets the system property indicated by the specified key.
		 * 
		 * Ou seja, este método cria ou altera o valor de uma propriedade de sistema que
		 * são compartilhadas por todas as aplicações/bibliotecas executando sobre a
		 * mesma instância da JVM.
		 * 
		 * O método System.getProperty(String key)
		 * 
		 * Retorna uma propriedade do sistema.
		 * 
		 */
	}
}
