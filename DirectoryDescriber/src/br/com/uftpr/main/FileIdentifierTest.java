package br.com.uftpr.main;

import java.io.File;

import br.com.uftpr.files.FileIdentifier;

public class FileIdentifierTest {

	public static void main(String[] args) {
		//List the available filesystem roots
		File[] root = File.listRoots();
		
		FileIdentifier identifier = new FileIdentifier();
		
		//Iterate the entire filesystem roots
		for(File file : root) {
			System.out.println("Root: " + file.getAbsolutePath());
			identifier.describe(file);
		}
		System.out.println("Application execution finished");
	}
}
