package questao2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Questao2 {

	public static final boolean writeToFile(File file, String text) {

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		boolean success = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(text);
			bufferedWriter.flush();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return success;
	}
}
