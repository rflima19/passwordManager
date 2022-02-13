package app.passwordmanager.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa os dados de um serviço ou site.</br>
 * Classe contento propertys que possuem binding com os controles da aplicação
 * JavaFX. Alterações nessas propertys resultam em alterações nos controles da
 * aplicação.
 */
public class SenhaServico {
	/**
	 * ID único
	 */
	private IntegerProperty id = new SimpleIntegerProperty();

	/**
	 * Nome do serviço ou site
	 */
	private StringProperty servico = new SimpleStringProperty();

	/**
	 * Login do serviço ou site
	 */
	private StringProperty login = new SimpleStringProperty();

	/**
	 * Senha do serviço ou site
	 */
	private StringProperty senha = new SimpleStringProperty();

	/**
	 * Observações complementares
	 */
	private StringProperty observacoes = new SimpleStringProperty();

	public int getId() {
		return this.id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getServico() {
		return this.servico.get();
	}

	public void setServico(String servico) {
		this.servico.set(servico);
	}

	public String getLogin() {
		return this.login.get();
	}

	public void setLogin(String login) {
		this.login.set(login);
	}

	public String getSenha() {
		return this.senha.get();
	}

	public void setSenha(String senha) {
		this.senha.set(senha);
	}

	public String getObservacoes() {
		return this.observacoes.get();
	}

	public void setObservacoes(String observacoes) {
		this.observacoes.set(observacoes);
	}

	public StringProperty servicoProperty() {
		return this.servico;
	}

	public StringProperty loginProperty() {
		return this.login;
	}

	public StringProperty senhaProperty() {
		return this.senha;
	}

	public StringProperty observacoesProperty() {
		return this.observacoes;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SenhaServico other = (SenhaServico) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
}
