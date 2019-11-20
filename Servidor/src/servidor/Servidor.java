package servidor;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import clientes.TrataCliente;

public class Servidor {
	
	private int porta;
	private List<Socket> clientes;
		
	private static final String NOME_ARQUIVO = "recebido.txt";
	private static final int TAMANHO_MAXIMO_ARQUIVO = 6022386;
	
	public Servidor(int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<Socket>();
	}
	
	public void executa() throws IOException {
		ServerSocket servidor = new ServerSocket(this.porta);
		System.out.println("Servidor iniciado. Porta " + this.porta + " aberta!");
		
		while(true) {
			Socket cliente = servidor.accept();
			System.out.println("Nova conexão com cliente " 
					+ cliente.getInetAddress().getHostAddress());
			clientes.add(cliente);
			
			TrataCliente tc = new TrataCliente(cliente, this);
			new Thread(tc).start();	
			
			if(cliente.isInputShutdown()) {
				clientes.remove(cliente);
				cliente.close();
				this.distribuirMensagem("Cliente " + cliente.getInetAddress() + " desconectado.");
			}
		}
	}
	
	public void distribuirMensagem(String msg) {
		for(Socket cliente: this.clientes) {
			PrintStream ps;
			try {
				ps = new PrintStream(cliente.getOutputStream());
				ps.println(msg);
			} catch (IOException e) {
				System.out.println("Erro ao enviar mensagem ao cliente " + cliente.getInetAddress().getHostAddress());
				e.printStackTrace();
			}
			
		}
	}
	
	public void fazerDownloadArquivo(InputStream cliente) throws IOException {
		int bytesLidos;
		int leituraAtual = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(NOME_ARQUIVO);
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
			System.out.println("Arquivo " + NOME_ARQUIVO + " recebido com sucesso! (" + leituraAtual +" bytes lidos)");
		} catch (FileNotFoundException e) {
			System.out.println("Erro: arquivo " + NOME_ARQUIVO + " não encontrado.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erro ao ler os dados do arquivo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(fos != null) fos.close();
			if(bos != null) bos.close();
		}
	}
	
	public void distribuirArquivo(Socket emissor) {
		for(Socket cliente : clientes) {
			if(cliente.getInetAddress() != emissor.getInetAddress()) {
				try {
					this.enviarArquivo(cliente);
				} catch (IOException e) {
					System.out.println("Erro ao enviar aquivo para o cliente " + cliente.getInetAddress().getHostAddress());
					e.printStackTrace();
				}
			}
		}
	}
	
	private void enviarArquivo(Socket cliente) throws IOException {
		File file = new File(NOME_ARQUIVO);
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			bis.read(bytes, 0, bytes.length);
			System.out.println("Enviando arquivo " + NOME_ARQUIVO + " (" + 	bytes.length + " bytes)");
			os = cliente.getOutputStream();
			os.write(bytes, 0, bytes.length);
			os.flush();
			System.out.println("Arquivo " + NOME_ARQUIVO + " enviado para o cliente.");
			//Envia uma mensagem ao cliente, informando que ele receberá um arquivo.
			PrintStream ps = new PrintStream(cliente.getOutputStream());
			ps.println("--file");
		} catch (FileNotFoundException e) {
			System.out.println("Erro: arquivo " + NOME_ARQUIVO + " não encontrado.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erro ao ler dados do arquivo " + NOME_ARQUIVO);
			e.printStackTrace();
		} finally {
			if(fis != null) fis.close();
			if(bis != null) bis.close();
			if(os != null) os.close();
		}	
	}
}
