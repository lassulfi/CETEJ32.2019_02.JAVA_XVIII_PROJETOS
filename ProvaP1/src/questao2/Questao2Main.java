package questao2;

import java.io.File;

public class Questao2Main {

	public static void main(String[] args) {
		boolean result = Questao2.writeToFile(new File("questao2.txt"), "Deu certo novamente!!");
		
		System.out.println("Questão 2: " + result);
	}
}
