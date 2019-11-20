package clientes;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import servidor.Servidor;

public class TrataCliente implements Runnable {
	
//	private InputStream cliente;
	private Socket cliente;
	private Servidor servidor;
	
	public TrataCliente(Socket cliente, Servidor servidor) {
		this.cliente = cliente;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(this.cliente.getInputStream());
			while(scanner.hasNextLine()) {
				String message = scanner.nextLine();			
				if(message.equals("--file")) {
					try {
						servidor.fazerDownloadArquivo(cliente.getInputStream());
						servidor.distribuirArquivo(cliente);
					} catch (IOException e) {
						servidor.distribuirMensagem("Erro ao receber arquivo: " + e.getLocalizedMessage());
						e.printStackTrace();
					}
				} else {
					servidor.distribuirMensagem(message);	
				}
			}
		} catch (IOException e) {
			System.out.println("Erro ao receber dados do cliente " + cliente.getInetAddress().getHostAddress());
			e.printStackTrace();
		} finally {
			if(scanner != null) scanner.close();
		}
	}
}
