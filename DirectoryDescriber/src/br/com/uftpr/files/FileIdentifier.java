package br.com.uftpr.files;

import java.io.File;

public class FileIdentifier {

	public FileIdentifier() {
	}
	
	public void describe(File root) {
		File[] files = root.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				System.out.println(file.getPath() + " is a directory");
			} else if(file.isFile()) {
				System.out.println(file.getPath() + " is a file");
			} else {
				System.out.println("It was not possible to indentify");
			}
		}
	}
}
