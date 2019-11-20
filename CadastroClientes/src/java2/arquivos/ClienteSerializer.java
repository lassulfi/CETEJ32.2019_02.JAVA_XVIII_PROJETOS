package java2.arquivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Classe serializadora do objeto cliente
 * 
 * @author lassulfi
 *
 */
public class ClienteSerializer {

	private File file;
	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;

	public ClienteSerializer() {
		file = null;
		fileOutputStream = null;
		objectOutputStream = null;
		fileInputStream = null;
		objectInputStream = null;
	}

	/**
	 * Cria um arquivo para persistir os dados da classe {@link Cliente}. Caso o
	 * arquivo exista persiste adiciona os novos dados ao arquivo existente, sem
	 * sobrescreve-lo.
	 */
	public void criarArquivoDeGravacao() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = fileChooser.showSaveDialog(null);// posiciona a janela no centro da tela
		// usando o this a janela é centrada na janela da aplicação. O FileChooser é
		// modal
		if (result == JFileChooser.CANCEL_OPTION) {
			return;// finaliza a execuçao do metodo
		}
		file = fileChooser.getSelectedFile();
		System.out.println(fileChooser.getName());

		if (file == null || file.getName().equals("")) {
			JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de Arquivo Inválido",
					JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				fileOutputStream = new FileOutputStream(file, false); // false(rewrite).
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
			} catch (IOException ioException) {
				JOptionPane.showMessageDialog(null, "Erro ao Abrir Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Abre o arquivo para gravação, caso ele tenha sido fechado por alguma operaçao
	 */
	public void abrirArquivoParaGravacao() {
		try {
			fileOutputStream = new FileOutputStream(file, false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao Abrir Arquivo para Gravação", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
		try {
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao Acessar Arquivo para Gravação", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Fecha os arquivos de gravação e leitura.
	 */
	public void fecharArquivo() {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
			if (objectOutputStream != null) {
				objectOutputStream.flush();
				objectOutputStream.close();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (objectInputStream != null) {
				objectInputStream.close();
			}
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error ao Fechar Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Abre o arquivo de dados para leitura
	 */
	public void abrirArquivoParaLeitura() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = fileChooser.showSaveDialog(null);// posiciona a janela no centro da tela
		// usando o this a janela é centrada na janela da aplicação. O FileChooser é
		// modal
		if (result == JFileChooser.CANCEL_OPTION) {
			return;// finaliza a execuçao do metodo
		}
		file = fileChooser.getSelectedFile();
		System.out.println(fileChooser.getName());
		if (file == null || file.getName().equals("")) {
			JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de Arquivo Inválido",
					JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				fileInputStream = new FileInputStream(file);
				objectInputStream = new ObjectInputStream(fileInputStream);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao tentar abrir o arquivo", "Erro de Leitura",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Grava um registro de um cliente no arquivo de persistencia de dados
	 * 
	 * @param cliente
	 */
	public void gravarClientes(List<Cliente> clientes) {

		if (file == null) {
			this.criarArquivoDeGravacao();
		} else {
			this.abrirArquivoParaGravacao();
		}
		try {
			for (Cliente cliente : clientes) {
				objectOutputStream.writeObject(cliente);
				objectOutputStream.flush();
				fileOutputStream.flush();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro durante a leitura do arquivo", "Erro de Leitura",
					JOptionPane.ERROR_MESSAGE);
			this.fecharArquivo();
		} finally {
			this.fecharArquivo();			
		}
	}

	/**
	 * Le um registro de um cliente do arquivo de persistencia de dados
	 * 
	 * @return instancia da classe {@link Cliente}
	 */
	public List<Cliente> lerRegistros() {

		this.abrirArquivoParaLeitura();
		List<Cliente> clientes = new ArrayList<>();
		boolean cont = true;
		try {
			while(cont) {
				if(fileInputStream.available() != 0) {
					Cliente cliente = (Cliente) objectInputStream.readObject();
					clientes.add(cliente);
				} else {
					cont = false;
				}
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Nenhum objeto encontrado", "Erro de Leitura",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro na leitura do arquivo", "Erro de Leitura",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			this.fecharArquivo();
		}

		return clientes;
	}

}
