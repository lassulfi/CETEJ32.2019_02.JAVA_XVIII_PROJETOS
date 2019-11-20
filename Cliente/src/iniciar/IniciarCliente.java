package iniciar;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import cliente.Cliente;
import tela.Tela;

public class IniciarCliente {
	private static final String host = "127.0.0.1";
	private static final int port = 12345;
	private static Tela tela;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		tela = new Tela(in);
		
		String nickname = tela.lerNickname(); 
		
		Cliente cliente = new Cliente(host, port, nickname);
		
		try {
			cliente.executa();
		} catch (UnknownHostException e) {
			System.out.println("Erro: host "+ host + " não encontrado");
		} catch (IOException e) {
			System.out.println("Erro ao enviar mensagem");
		}
		in.close();
	}
}
