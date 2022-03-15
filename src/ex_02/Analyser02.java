package ex_02;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Analyser02 {
	
	public static void main (String [] args) {
		try {
			analyser();
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public static void analyser () throws Exception{
		
		final int OPEN = 0;
		final int FULL = 1;
		final int CLOSED = 2;
		
		LineProvider provider = new LineProvider();
		
		int state = OPEN;
		int inBar = 0;
		String line;
		int numLine = 0;
		int customerID;
		Queue<Integer> queue = new LinkedList<Integer>();
		
		System.out.println ("ANALYSER 02");
		System.out.println ("FIFO-policy analysed");
		System.out.println();
		
		while (provider.hasLine()) {
			line = provider.getLine().toUpperCase();
			numLine++;
			// skip blank lines
			if (line.isBlank()) continue;
			
			customerID = getCustomerID (line); 
			if (line.contains("ENTERING")) {
				queue.add(customerID);
			}
			switch (state) {
			case OPEN:
				if (line.contains("SIT")) {
					inBar++;
					if (inBar==5) state=FULL;
					if (queue.isEmpty())
						throw new RuntimeException("inconsistent state. Customer seating without having entered. Line: "+numLine);
					int validID = queue.remove();
					if (customerID!=validID) 
						throw new RuntimeException("Order violation. Sitting id: "+customerID+" Expencting id: "+validID+" Line: "+numLine);
				}
				else if (line.contains("LEAVING")) {
					inBar--;
					if (inBar<0) 
						throw new RuntimeException ("inconsistent state. Customer leaving an empty bar. Line: "+numLine);
				}
				break;
			case FULL: 
				if (line.contains("LEAVING")) {
					inBar--;
					state = OPEN;
				}
				if (line.contains("SIT")) 
					throw new RuntimeException ("Error. Sitting when bar is full. Line: "+numLine);
				if (line.contains("WAIT")) {
					state = CLOSED;
					// System.out.println("CLOSING: "+numLine);
				}
				break;
			case CLOSED: 
				if (line.contains("LEAVING")) {
					inBar--;
					if (inBar==0) {
						state = OPEN;
						//System.out.println("REOPENING: "+numLine);
					}
					
				}
				else if (line.contains("SIT")) 
					throw new RuntimeException ("Error. Sitting when party in bar. Line: "+numLine);
					
				break;
				
			} // switch ends here
		} // while ends here
		
		System.out.println();
		System.out.println("Trace fully analised. "+numLine+" lines read");
		System.out.println("Normal termination, no incorrect behaviours detected");
		
	} // main ends here
	
	
	static int getCustomerID (String line) {
		int result;
		int pos = line.lastIndexOf("C(");
		pos+=2;
		String d1 = ""+line.charAt(pos);
		char d2 = line.charAt(pos+1);
		result = Integer.parseInt(d1);
		if (Character.isDigit(d2)) {
			result = 10*result + Integer.parseInt(d2+"");
		}
		return result;
	}
}

class LineProvider {
	
	BufferedReader buf = null;
	String currentLine;
	
	public LineProvider () {
		try {
			buf = new BufferedReader(new FileReader("Trace.txt"));
			currentLine = buf.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public boolean hasLine() {
		return currentLine!=null;
	}
	public String getLine() throws IOException {
		String result = currentLine;
		currentLine = buf.readLine();
		return result;
	}
}
