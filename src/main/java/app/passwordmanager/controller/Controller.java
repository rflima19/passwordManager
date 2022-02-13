package app.passwordmanager.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.passwordmanager.dao.DAOFactory;
import app.passwordmanager.dao.SenhaServicoDAO;
import app.passwordmanager.model.SenhaServico;
import app.passwordmanager.util.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller da tela da aplica��o
 */
public class Controller implements Initializable {

	@FXML
	private TableView<SenhaServico> table;
	@FXML
	private TextField txtServico;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtSenha;
	@FXML
	private TextArea txtObservacoes;
	@FXML
	private Button btnNovo;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnExcluir;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnConfirm;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnClearSearch;

	/**
	 * Property que indica se o modo de edi��o de senha de servi�o est� ativo
	 */
	private BooleanProperty editMode = new SimpleBooleanProperty();

	/**
	 * Property que indica se os resultados da tabela est�o filtrados
	 */
	private BooleanProperty resultsFiltered = new SimpleBooleanProperty();

	/**
	 * Senha de servi�o ativa no momento (selecionada ou em edi��o)
	 */
	private SenhaServico currentSenhaServico;

	/**
	 * Objeto DAO para manipula��o dos dados
	 */
	private SenhaServicoDAO dao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Obt�m o objeto DAO
		this.dao = DAOFactory.getSenhaServicoDAO();

		// Carrega os dados sem filtragem
		this.loadData(false);

		// Adiciona um listener para ser notificado quando o usu�rio seleciona um item
		// na tabela.
		// Dessa forma � poss�vel definir os bindings corretamente.
		this.table.getSelectionModel().selectedItemProperty().addListener((event, oldValue, newValue) -> {
			this.unbindData(oldValue);
			this.bindData(newValue);
		});

		// Define os bindings gerais do modelo com a interface gr�fica
		this.btnNovo.disableProperty().bind(this.editMode);
		this.btnEditar.disableProperty().bind(this.table.getSelectionModel().selectedItemProperty().isNull().or(this.editMode));
		this.btnExcluir.disableProperty().bind(this.table.getSelectionModel().selectedItemProperty().isNull().or(this.editMode));
		this.btnCancel.disableProperty().bind(this.editMode.not());
		this.btnConfirm.disableProperty().bind(this.editMode.not());
		this.btnSearch.disableProperty().bind(this.txtSearch.textProperty().isEmpty());
		this.btnClearSearch.disableProperty().bind(this.resultsFiltered.not());

		this.txtServico.editableProperty().bind(this.editMode);
		this.txtLogin.editableProperty().bind(this.editMode);
		this.txtSenha.editableProperty().bind(this.editMode);
		this.txtObservacoes.editableProperty().bind(this.editMode);

		this.table.disableProperty().bind(this.editMode);
	}

	/**
	 * M�todo chamado quando o usu�rio usa o menu Arquivo > Sair
	 */
	@FXML
	public void exit() {
		// Encerra a aplica��o
		Platform.exit();
	}

	/**
	 * M�todo chamado quando uma filtragem � realizada
	 */
	@FXML
	public void search() {
		// Carrega os dados com a filtragem habilitada
		this.loadData(true);
		
		// Define os dados como estando filtrados
		this.resultsFiltered.set(true);
	}

	/**
	 * M�todo chamado quando uma filtragem � cancelada
	 */
	@FXML
	public void clearSearch() {
		// Carrega os dados sem filtragem
		this.loadData(false);
		
		// Limpa o campo de busca
		this.txtSearch.clear();
		
		// Define os dados como n�o filtrados
		this.resultsFiltered.set(false);
	}

	/**
	 * M�todo chamado na cria��o de um novo registro de senha de servi�o
	 */
	@FXML
	public void onNew() {
		// Desmarca qualquer registro que esteja selecionado na tabela 
		this.table.getSelectionModel().clearSelection();
		
		// Habilita o modo de edi��o
		this.editMode.set(true);
		
		// Define a senha de servi�o corrente como um novo objeto
		this.currentSenhaServico = new SenhaServico();
		
		// Faz o binding dos dados com o formul�rio
		this.bindData(currentSenhaServico);
		
		// Coloca o foco da interface no campo de servi�o
		this.txtServico.requestFocus();
	}

	/**
	 * M�todo chamado na altera��o de um registro de senha de servi�o
	 */
	@FXML
	public void onEdit() {
		// Habilita o modo de edi��o
		this.editMode.set(true);
		
		// Define a senha de servi�o corrente como a senha de servi�o selecionada na tabela
		this.currentSenhaServico = table.getSelectionModel().getSelectedItem();
	}

	/**
	 * M�todo chamado na exclus�o de um registro de senha de servi�o
	 */
	@FXML
	public void onDelete() {
		// Remove o registro selecionado da lista de itens que comp�em a tabela
		this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
		
		// Faz com que essa mudan�a seja refletida nos dados armazenados 
		storeData();
	}


	/**
	 * M�todo chamado quando o usu�rio confirma a inser��o ou altera��o de uma senha de servi�o
	 */
	@FXML
	public void onConfirm() {
		// Valida se os dados do formul�rio est�o ok
		String errorMessage = this.validateForm();
		if (!errorMessage.isEmpty()) {
			// Caso n�o estejam, mostra o erro de valida��o e retorna
			this.showValidationError(errorMessage);
			return;
		}

		// Desabilita o modo de edi��o
		this.editMode.set(false);

		// Caso seja a inser��o de um novo registro
		if (this.currentSenhaServico.getId() == 0) {
			// Gera um novo ID para o registro
			int newId = this.dao.generateId();
			this.currentSenhaServico.setId(newId);
			
			// Adiciona ele na lista de itens referentes � tabela
			this.table.getItems().add(this.currentSenhaServico);
			
			// Desfaz os bindings do formul�rio
			unbindData(this.currentSenhaServico);
			
			// Limpa o formul�rio
			this.clearForm();
			
			// Seleciona o registro rec�m criado na tabela
			this.table.getSelectionModel().select(this.currentSenhaServico);
		}

		// Faz com que essa mudan�a seja refletida nos dados armazenados
		this.storeData();
	}

	/**
	 * M�todo chamado quando o cancelamento da edi��o ou cria��o � realizado
	 */
	@FXML
	public void onCancel() {
		// Desabilita o modo de edi��o
		this.editMode.set(false);

		// Se era uma cria��o, desfaz o biding e limpa o formul�rio
		if (this.currentSenhaServico.getId() == 0) {
			unbindData(this.currentSenhaServico);
			this.clearForm();
		}
	}

	/**
	 * Carrega os dados na tela
	 * 
	 * @param filter
	 *            true se � preciso filtrar os dados; false, caso contr�rio
	 */
	private void loadData(boolean filter) {
		List<SenhaServico> items;
		if (!filter) {
			items = this.dao.load();
		} else {
			items = this.dao.filter(this.txtSearch.getText());
		}

		ObservableList<SenhaServico> list = FXCollections.observableArrayList(items);
		this.table.setItems(list);
	}

	/**
	 * Faz o binding do objeto de senha de servi�o com os campos do formul�rio
	 * 
	 * @param senhaServico
	 */
	private void bindData(SenhaServico senhaServico) {
		if (senhaServico != null) {
			this.txtServico.textProperty().bindBidirectional(senhaServico.servicoProperty());
			this.txtLogin.textProperty().bindBidirectional(senhaServico.loginProperty());
			this.txtSenha.textProperty().bindBidirectional(senhaServico.senhaProperty());
			this.txtObservacoes.textProperty().bindBidirectional(senhaServico.observacoesProperty());
		}
	}

	/**
	 * Desfaz o binding do objeto de senha de servi�o com os campos do formul�rio
	 * 
	 * @param senhaServico
	 */
	private void unbindData(SenhaServico senhaServico) {
		if (senhaServico != null) {
			this.txtServico.textProperty().unbindBidirectional(senhaServico.servicoProperty());
			this.txtLogin.textProperty().unbindBidirectional(senhaServico.loginProperty());
			this.txtSenha.textProperty().unbindBidirectional(senhaServico.senhaProperty());
			this.txtObservacoes.textProperty().unbindBidirectional(senhaServico.observacoesProperty());

			// Limpa os campos do formul�rio
			this.clearForm();
		}
	}

	/**
	 * Limpa os campos do formul�rio
	 */
	private void clearForm() {
		this.txtServico.clear();
		this.txtLogin.clear();
		this.txtSenha.clear();
		this.txtObservacoes.clear();
	}
	
	/**
	 * Valida o formul�rio
	 * @return true se os dados s�o v�lidos; false, caso contr�rio
	 */
	private String validateForm() {
		StringBuilder errorMessage = new StringBuilder();

		// Verifica se o servi�o foi preenchido
		if (StringUtils.isEmpty(this.currentSenhaServico.getServico())) {
			errorMessage.append("Preencha o site/servi�o").append(StringUtils.newLine());
		}

		// Verifica se o login foi preenchido
		if (StringUtils.isEmpty(this.currentSenhaServico.getLogin())) {
			errorMessage.append("Preencha o login").append(StringUtils.newLine());
		}

		// Verifica se a senha foi preenchida
		if (StringUtils.isEmpty(this.currentSenhaServico.getSenha())) {
			errorMessage.append("Preencha a senha").append(StringUtils.newLine());
		}

		return errorMessage.toString();
	}
	
	/**
	 * Exibe um erro de valida��o na tela
	 * @param message
	 */
	private void showValidationError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro de valida��o");
		alert.setHeaderText("Informa��o incorreta");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
	 * Grava os dados no meio de armazenamento definido pelo DAO
	 */
	private void storeData() {
		this.dao.store(this.table.getItems());
	}
}
