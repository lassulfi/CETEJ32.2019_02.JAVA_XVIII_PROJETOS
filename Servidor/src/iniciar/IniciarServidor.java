package iniciar;

import java.io.IOException;

import servidor.Servidor;

public class IniciarServidor {

	private static final int porta = 12345;
	
	public static void main(String[] args) {
		Servidor servidor = new Servidor(porta);
		
		try {
			servidor.executa();
		} catch (IOException e) {
			System.out.println("Erro ao executar o servidor na porta " + porta);
		}
	}
}
