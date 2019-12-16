package tela;

import java.util.Scanner;

public class Tela {

	Scanner scanner;
	
	public Tela(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public String lerNickname() {
		System.out.println("Bem vindo ao chat UTFPR!\nInforme seu apelido.");
		System.out.print("Seu apelido: ");
		String nickname = this.scanner.nextLine(); 
		
		return nickname;
	}
	
	public void exibirComandos() {
		System.out.println("Opções do chat - digite os seguintes comandos e pressione [ENTER] para executar");
		System.out.println("- Enviar arquivo: --enviar_arquivo");
		System.out.println("- Desconectar: --desconectar");
	}
}
