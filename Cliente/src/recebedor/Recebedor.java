package recebedor;

import java.io.InputStream;
import java.util.Scanner;

import cliente.Cliente;

public class Recebedor implements Runnable {

	private InputStream servidor;
	private Cliente cliente;	
	
	public Recebedor(InputStream servidor, Cliente cliente) {
		this.servidor = servidor;
		this.cliente = cliente;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		scanner = new Scanner(this.servidor);
		while(scanner.hasNextLine()) {
			System.out.println(scanner.nextLine());
		}
		scanner.close();
	}
	
	
}
