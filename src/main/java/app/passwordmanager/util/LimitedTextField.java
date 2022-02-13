package app.passwordmanager.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

/**
 * Classe que estende do controle JavaFX {@link TextField}</br>
 * Representa um campo TextField com limita��o de caracteres a serem inseridos
 * no campo.
 */
public class LimitedTextField extends TextField {

	/**
	 * Propriedade que define o limite m�ximo de caracteres
	 */
	private IntegerProperty limit = new SimpleIntegerProperty();
	
	/**
	 * Sobrescrita do m�todo replaceText(int, int, String) que atualiza o campo de
	 * texto cada vez que o usu�rio digita uma nova entrada. Acrescenta a valida��o
	 * nescess�ria.
	 * 
	 * @see TextField#replaceText(int, int, String)
	 * */
	@Override
	public void replaceText(int start, int end, String text) {
		if (this.validate(text)) {
			super.replaceText(start, end, text);
		}
	}
	
	/**
	 * Sobrescrita do m�todo replaceSelection(String replacement) que atualiza uma
	 * sele��o de texto no campo de texto e realiza a substitui��o do texto
	 * selecionado. Acrescenta a valida��o nescess�ria.
	 * 
	 * @see TextField#replaceSelection(String)
	 * */
	@Override
	public void replaceSelection(String replacement) {
		if (this.validate(replacement)) {
			super.replaceSelection(replacement);
		}
	}
	
	/**
	 * Valida se o texto passado como par�metro esta dento do limite v�lido.
	 * 
	 * @param text
	 *            Texto a ser validado.
	 * @return True caso o texto seja v�lido, false caso contr�rio.
	 */
	private boolean validate(String text) {
//		if (text.length() <= this.limit.get()) {
//			return true;
//		}
//		return false;
		return this.lengthProperty().lessThan(this.limit).get();
	}

	public int getLimit() {
		return this.limit.get();
	}

	public void setLimit(int limit) {
		this.limit.set(limit);
	}
	
	public IntegerProperty limitProperty() {
		return this.limit;
	}
	
}
