package app.passwordmanager.dao.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import app.passwordmanager.dao.DAOException;
import app.passwordmanager.dao.SenhaServicoDAO;
import app.passwordmanager.model.SenhaServico;

/**
 * Classe de DAO para leitura e armazenamento em arquivo XML
 */
public class SenhaServicoXMLDAO implements SenhaServicoDAO {

	/**
	 * Caminho do arquivo XML com os dados. Cria o arquivo no diretório home do
	 * usuário.
	 */
	private static final Path STORAGE_FILE = Paths.get(System.getProperty("user.home"), "senhas.xml");

	/**
	 * @see br.com.softblue.bluekeeper.dao.SenhaServicoDAO#load()
	 */
	@Override
	public List<SenhaServico> load() {
		List<SenhaServico> senhasServico = new ArrayList<>();

		// Se não existe um arquivo de dados, retorna a lista vazia
		if (!Files.exists(SenhaServicoXMLDAO.STORAGE_FILE)) {
			return senhasServico;
		}

		try {
			// Faz o parse do documento XML, criando os objetos SenhaServico e adicionando
			// na lista
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			Document doc = db.parse(Files.newInputStream(SenhaServicoXMLDAO.STORAGE_FILE));

			NodeList senhaServicoTags = doc.getElementsByTagName("SenhaServico");

			for (int i = 0; i < senhaServicoTags.getLength(); i++) {
				Element senhaServicoTag = (Element) senhaServicoTags.item(i);
				SenhaServico senhaServico = new SenhaServico();

				senhaServico.setId(Integer.parseInt(senhaServicoTag.getAttribute("id")));
				senhaServico.setServico(senhaServicoTag.getElementsByTagName("Servico").item(0).getTextContent());
				senhaServico.setLogin(senhaServicoTag.getElementsByTagName("Login").item(0).getTextContent());
				senhaServico.setSenha(this.decrypt(senhaServicoTag.getElementsByTagName("Senha").item(0).getTextContent()));
				senhaServico.setObservacoes(senhaServicoTag.getElementsByTagName("Observacoes").item(0).getTextContent());

				senhasServico.add(senhaServico);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new DAOException(e);
		}

		return senhasServico;
	}
	
	/**
	 * @see br.com.softblue.bluekeeper.dao.SenhaServicoDAO#store(java.util.List)
	 */
	@Override
	public void store(List<SenhaServico> senhasServico) {
		try {
			// Cria o documento XML com base na lista de objetos passada como parâmetro
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			Document doc = db.newDocument();
			
			Element rootTag = doc.createElement("PassworlManager");
			doc.appendChild(rootTag);
			
			senhasServico.forEach(senhaServico -> {
				Element senhaServicoTag = doc.createElement("SenhaServico");
				senhaServicoTag.setAttribute("id", String.valueOf(senhaServico.getId()));
				
				Element servicoTag = doc.createElement("Servico");
				servicoTag.appendChild(doc.createTextNode(senhaServico.getServico()));
				senhaServicoTag.appendChild(servicoTag);
				
				Element loginTag = doc.createElement("Login");
				loginTag.appendChild(doc.createTextNode(senhaServico.getLogin()));
				senhaServicoTag.appendChild(loginTag);
				
				Element senhaTag = doc.createElement("Senha");
				senhaTag.appendChild(doc.createTextNode(this.encrypt(senhaServico.getSenha())));
				senhaServicoTag.appendChild(senhaTag);
				
				Element observacoes = doc.createElement("Observacoes");
				observacoes.appendChild(doc.createCDATASection(senhaServico.getObservacoes() == null ? "" : senhaServico.getObservacoes()));
				senhaServicoTag.appendChild(observacoes);
				
				rootTag.appendChild(senhaServicoTag);
			});
			
			// Gera o documento XML de saída
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(Files.newOutputStream(SenhaServicoXMLDAO.STORAGE_FILE));
			t.transform(source, result);
			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/**
	 * @see br.com.softblue.bluekeeper.dao.SenhaServicoDAO#filter(java.lang.String)
	 */
	@Override
	public List<SenhaServico> filter(String text) {
		// Carrega todas as senhas de serviço
		List<SenhaServico> itens = this.load();
		
		// Converte o texto de filtragem para maiúsculo para que a filtragem funcione
		// tanto para caracteres em minúsculo como em maiúsculo
		String textUpper = text.toUpperCase();
		
		// Faz a filtragem
		return itens.stream()
			.filter(s -> s.getLogin().toUpperCase().contains(textUpper) || s.getServico().toUpperCase().contains(textUpper))
			.collect(Collectors.toList());
	}
	
	/**
	 * @see br.com.softblue.bluekeeper.dao.SenhaServicoDAO#generateId()
	 */
	@Override
	public int generateId() {
		// Retorna o maior ID + 1, ou 1 se não existirem elementos na coleção
		return this.load().stream()
			.mapToInt(s -> s.getId() + 1)
			.max()
			.orElse(1);
	}
}
