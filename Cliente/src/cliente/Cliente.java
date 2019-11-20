package cliente;

import static recebedor.enums.Comando.DESCONECTAR;
import static recebedor.enums.Comando.ENVIAR_ARQUIVO;
import static recebedor.enums.Comando.EXIBIR_OPCOES;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import recebedor.Recebedor;
import tela.Tela;

public class Cliente {
		
	private String host;
	private int port;
	private String nickname;
	
	private static final String NOME_ARQUIVO_ENVIADO = "arquivo.txt";
	private static final String NOME_ARQUIVO_RECEBIDO = "recebido.txt";
	private static final int TAMANHO_MAXIMO_ARQUIVO = 6022386;	
	
	public Cliente(String host, int port, String nickname) {
		this.host = host;
		this.port = port;
		this.nickname = nickname;
	}
	
	public void executa() throws UnknownHostException, IOException {
		Socket cliente = new Socket(this.host, this.port);
		System.out.println("O cliente " + this.nickname + " se conectou ao servidor");
		System.out.println("Digite --opcoes e pressione [ENTER] para exibir os comandos do chat ou"
				+ " digite sua mensagem e pressione [ENTER] para enviar.");
		
		Recebedor recebedor = new Recebedor(cliente.getInputStream(), this);
		new Thread(recebedor).start();
		
		Scanner teclado = new Scanner(System.in);
		PrintStream saida = new PrintStream(cliente.getOutputStream());
		
		while(teclado.hasNextLine()) {
			Tela tela = new Tela(teclado);
			String message = teclado.nextLine();
			if(message.contains(EXIBIR_OPCOES.getComando())) {
				tela.exibirComandos();
			} else if(message.contains(ENVIAR_ARQUIVO.getComando())) {
				saida.println("--file");
				this.enviarArquivo(cliente.getOutputStream());
			} else if(message.contains(DESCONECTAR.getComando())) {
				System.out.println("Desconectando...");
				cliente.close();
				saida.println("Cliente " + this.nickname + " desconectou-se.");
			} else if(message.equals("--file")) {
				this.fazerDownloadArquivo(cliente.getInputStream());
			} else {
				saida.println("[" + this.nickname + " - enviou]: " + message);
			}
			
		}
		
		saida.close();
		teclado.close();
		cliente.close();
	}
	
	private void enviarArquivo(OutputStream outputStream) throws IOException {
		File file = new File(NOME_ARQUIVO_ENVIADO);
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			bis.read(bytes, 0, bytes.length);
			System.out.println("Enviando arquivo " + NOME_ARQUIVO_ENVIADO 
					+ " (" + 	bytes.length + " bytes)");
			os = outputStream;
			os.write(bytes, 0, bytes.length);
			os.flush();
			System.out.println("Arquivo " + NOME_ARQUIVO_ENVIADO 
					+ " enviado para o servidor.");
		} catch (FileNotFoundException e) {
			System.out.println("Erro: arquivo " + NOME_ARQUIVO_ENVIADO 
					+ " não encontrado.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erro ao ler dados do arquivo " + NOME_ARQUIVO_ENVIADO);
			e.printStackTrace();
		} finally {
			if(fis != null) fis.close();
			if(bis != null) bis.close();
			if(os != null) os.close();
		}	
	}
	
	public void fazerDownloadArquivo(InputStream cliente) throws IOException {
		int bytesLidos;
		int leituraAtual = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(NOME_ARQUIVO_RECEBIDO);
			bos = new BufferedOutputStream(fos);
			byte[] bytes = new byte[TAMANHO_MAXIMO_ARQUIVO];
			bytesLidos = cliente.read(bytes, 0, bytes.length);
			leituraAtual = bytesLidos;
			do {
				bytesLidos = cliente.read(bytes, leituraAtual, (bytes.length - leituraAtual));
				if(bytesLidos >= 0) leituraAtual += bytesLidos;
			} while (bytesLidos > -1);
			bos.write(bytes, 0,leituraAtual);
			bos.flush();
			System.out.println("Arquivo " + NOME_ARQUIVO_RECEBIDO + " recebido com sucesso! (" 
					+ leituraAtual +" bytes lidos)");
		} catch (FileNotFoundException e) {
			System.out.println("Erro: arquivo " + NOME_ARQUIVO_RECEBIDO + " não encontrado.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erro ao ler os dados do arquivo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(fos != null) fos.close();
			if(bos != null) bos.close();
		}
	}
}
