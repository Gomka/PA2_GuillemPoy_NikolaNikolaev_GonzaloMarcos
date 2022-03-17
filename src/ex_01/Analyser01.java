package ex_01;

import java.io.*;

public class Analyser01 {

	public static void main(String[] args) {
		try {
			analyser();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	private static void analyser() throws Exception {

		final int OPEN = 0;
		final int FULL = 1;
		final int CLOSED = 2;

		LineProvider provider = new LineProvider();

		int state = OPEN;
		int inBar = 0;
		String line;
		int numLine = 0;

		System.out.println("ANALYSER 01");
		System.out.println("Rigth ordering of customers not analysed");
		System.out.println();

		while (provider.hasLine()) {
			line = provider.getLine().toUpperCase();
			numLine++;
			switch (state) {
				case OPEN:
					if (line.contains("SIT")) {
						inBar++;
						if (inBar == 5)
							state = FULL;
					} else if (line.contains("LEAVING")) {
						inBar--;
						if (inBar < 0)
							throw new RuntimeException(
									"inconsistent state. Customer leaving an empty bar. Line: " + numLine);
					}
					break;
				case FULL:
					if (line.contains("LEAVING")) {
						inBar--;
						state = OPEN;
					}
					if (line.contains("SIT"))
						throw new RuntimeException("Error. Sitting when bar is full. Line: " + numLine);
					if (line.contains("WAIT")) {
						state = CLOSED;
						// System.out.println("CLOSING: "+numLine);
					}
					break;
				case CLOSED:
					if (line.contains("LEAVING")) {
						inBar--;
						if (inBar == 0) {
							state = OPEN;
							// System.out.println("REOPENING: "+numLine);
						}
					} else if (line.contains("SIT"))
						throw new RuntimeException("Error. Sitting when party in bar. Line: " + numLine);

					break;

			} // switch ends here
		} // while ends here

		System.out.println();
		System.out.println("Trace fully analised. " + numLine + " lines read");
		System.out.println("Normal termination, no incorrect behaviours detected");

	} // main ends here
}

class LineProvider {

	BufferedReader buf = null;
	String currentLine;

	public LineProvider() {
		try {
			buf = new BufferedReader(new FileReader("Trace.txt"));
			currentLine = buf.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public boolean hasLine() {
		return currentLine != null;
	}

	public String getLine() throws IOException {
		String result = currentLine;
		currentLine = buf.readLine();
		return result;
	}
}
