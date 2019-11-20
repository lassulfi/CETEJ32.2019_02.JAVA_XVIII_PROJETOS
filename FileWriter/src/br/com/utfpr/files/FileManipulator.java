package br.com.utfpr.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManipulator {

	public void writeToFile(File file, String[] args, Scanner in) {
		//Check if the file already exists
		if (file.exists()) {
			System.out.println("File already exists. Type [o] to overwrite its content or "
					+ "[a] to append the data to the file.");
			char option = 0;
			do {
				System.out.print("Option: ");				
				option = in.nextLine().charAt(0);
				if(option != 'o' && option != 'a') {
					System.out.println("Invalid option. Please type [o] to overwrite its content or "
							+ "[a] to append the data to the file");
				}
			} while (option != 'o' && option != 'a');
			switch (option) {
			case 'o': //overwrites the data in the existing file
				writeDataToFile(file, args, false);
				break;
			case 'a': //append data to the existing file
				writeDataToFile(file, args, true);
				break;
			default:
				break;
			}
		} else {
			writeDataToFile(file, args, true);;
		}
	}
		
	private static void writeDataToFile(File file, String[] args, boolean option) {
		//Writes the args in the file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, option));
			for(String arg: args) {
				writer.append(arg + "\n");
			}
			writer.close();
			if(option == true) {
				System.out.println("data append to file");
			} else {
				System.out.println("data overwritten to file");
			}
		} catch (IOException e) {
			//If an error occurs during writing, catchs an exception
			System.out.println("Error while writing data to file " + e.getLocalizedMessage());
		}		
	}
}
