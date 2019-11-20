package br.com.utfpr.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import br.com.utfpr.files.FileManipulator;

public class FileWriterTest {
	
	private static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Invalid number of arguments");
		} else {
			int size = args.length - 1;
			File file = new File(args[0]);
			if (size > 0) { //If there is more than one arg, append to file 
				String[] words = Arrays.copyOfRange(args, 1, args.length);
				FileManipulator fileManipulator = new FileManipulator();
				fileManipulator.writeToFile(file, words, in);
			} else { //creates an empty file
				System.out.println("No data found to write to file. Creating an empty file.");
				try {
					file.createNewFile();
					System.out.println("Empty file created");
				} catch (IOException e) {
					System.out.println("Error while creating file " + file.getName());
				}
			}
		}
		System.out.println("Application execution finished");
	}
}
