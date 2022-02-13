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
 * Controller da tela da aplicação
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
	 * Property que indica se o modo de edição de senha de serviço está ativo
	 */
	private BooleanProperty editMode = new SimpleBooleanProperty();

	/**
	 * Property que indica se os resultados da tabela estão filtrados
	 */
	private BooleanProperty resultsFiltered = new SimpleBooleanProperty();

	/**
	 * Senha de serviço ativa no momento (selecionada ou em edição)
	 */
	private SenhaServico currentSenhaServico;

	/**
	 * Objeto DAO para manipulação dos dados
	 */
	private SenhaServicoDAO dao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Obtém o objeto DAO
		this.dao = DAOFactory.getSenhaServicoDAO();

		// Carrega os dados sem filtragem
		this.loadData(false);

		// Adiciona um listener para ser notificado quando o usuário seleciona um item
		// na tabela.
		// Dessa forma é possível definir os bindings corretamente.
		this.table.getSelectionModel().selectedItemProperty().addListener((event, oldValue, newValue) -> {
			this.unbindData(oldValue);
			this.bindData(newValue);
		});

		// Define os bindings gerais do modelo com a interface gráfica
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
	 * Método chamado quando o usuário usa o menu Arquivo > Sair
	 */
	@FXML
	public void exit() {
		// Encerra a aplicação
		Platform.exit();
	}

	/**
	 * Método chamado quando uma filtragem é realizada
	 */
	@FXML
	public void search() {
		// Carrega os dados com a filtragem habilitada
		this.loadData(true);
		
		// Define os dados como estando filtrados
		this.resultsFiltered.set(true);
	}

	/**
	 * Método chamado quando uma filtragem é cancelada
	 */
	@FXML
	public void clearSearch() {
		// Carrega os dados sem filtragem
		this.loadData(false);
		
		// Limpa o campo de busca
		this.txtSearch.clear();
		
		// Define os dados como não filtrados
		this.resultsFiltered.set(false);
	}

	/**
	 * Método chamado na criação de um novo registro de senha de serviço
	 */
	@FXML
	public void onNew() {
		// Desmarca qualquer registro que esteja selecionado na tabela 
		this.table.getSelectionModel().clearSelection();
		
		// Habilita o modo de edição
		this.editMode.set(true);
		
		// Define a senha de serviço corrente como um novo objeto
		this.currentSenhaServico = new SenhaServico();
		
		// Faz o binding dos dados com o formulário
		this.bindData(currentSenhaServico);
		
		// Coloca o foco da interface no campo de serviço
		this.txtServico.requestFocus();
	}

	/**
	 * Método chamado na alteração de um registro de senha de serviço
	 */
	@FXML
	public void onEdit() {
		// Habilita o modo de edição
		this.editMode.set(true);
		
		// Define a senha de serviço corrente como a senha de serviço selecionada na tabela
		this.currentSenhaServico = table.getSelectionModel().getSelectedItem();
	}

	/**
	 * Método chamado na exclusão de um registro de senha de serviço
	 */
	@FXML
	public void onDelete() {
		// Remove o registro selecionado da lista de itens que compõem a tabela
		this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
		
		// Faz com que essa mudança seja refletida nos dados armazenados 
		storeData();
	}


	/**
	 * Método chamado quando o usuário confirma a inserção ou alteração de uma senha de serviço
	 */
	@FXML
	public void onConfirm() {
		// Valida se os dados do formulário estão ok
		String errorMessage = this.validateForm();
		if (!errorMessage.isEmpty()) {
			// Caso não estejam, mostra o erro de validação e retorna
			this.showValidationError(errorMessage);
			return;
		}

		// Desabilita o modo de edição
		this.editMode.set(false);

		// Caso seja a inserção de um novo registro
		if (this.currentSenhaServico.getId() == 0) {
			// Gera um novo ID para o registro
			int newId = this.dao.generateId();
			this.currentSenhaServico.setId(newId);
			
			// Adiciona ele na lista de itens referentes à tabela
			this.table.getItems().add(this.currentSenhaServico);
			
			// Desfaz os bindings do formulário
			unbindData(this.currentSenhaServico);
			
			// Limpa o formulário
			this.clearForm();
			
			// Seleciona o registro recém criado na tabela
			this.table.getSelectionModel().select(this.currentSenhaServico);
		}

		// Faz com que essa mudança seja refletida nos dados armazenados
		this.storeData();
	}

	/**
	 * Método chamado quando o cancelamento da edição ou criação é realizado
	 */
	@FXML
	public void onCancel() {
		// Desabilita o modo de edição
		this.editMode.set(false);

		// Se era uma criação, desfaz o biding e limpa o formulário
		if (this.currentSenhaServico.getId() == 0) {
			unbindData(this.currentSenhaServico);
			this.clearForm();
		}
	}

	/**
	 * Carrega os dados na tela
	 * 
	 * @param filter
	 *            true se é preciso filtrar os dados; false, caso contrário
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
	 * Faz o binding do objeto de senha de serviço com os campos do formulário
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
	 * Desfaz o binding do objeto de senha de serviço com os campos do formulário
	 * 
	 * @param senhaServico
	 */
	private void unbindData(SenhaServico senhaServico) {
		if (senhaServico != null) {
			this.txtServico.textProperty().unbindBidirectional(senhaServico.servicoProperty());
			this.txtLogin.textProperty().unbindBidirectional(senhaServico.loginProperty());
			this.txtSenha.textProperty().unbindBidirectional(senhaServico.senhaProperty());
			this.txtObservacoes.textProperty().unbindBidirectional(senhaServico.observacoesProperty());

			// Limpa os campos do formulário
			this.clearForm();
		}
	}

	/**
	 * Limpa os campos do formulário
	 */
	private void clearForm() {
		this.txtServico.clear();
		this.txtLogin.clear();
		this.txtSenha.clear();
		this.txtObservacoes.clear();
	}
	
	/**
	 * Valida o formulário
	 * @return true se os dados são válidos; false, caso contrário
	 */
	private String validateForm() {
		StringBuilder errorMessage = new StringBuilder();

		// Verifica se o serviço foi preenchido
		if (StringUtils.isEmpty(this.currentSenhaServico.getServico())) {
			errorMessage.append("Preencha o site/serviço").append(StringUtils.newLine());
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
	 * Exibe um erro de validação na tela
	 * @param message
	 */
	private void showValidationError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro de validação");
		alert.setHeaderText("Informação incorreta");
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
