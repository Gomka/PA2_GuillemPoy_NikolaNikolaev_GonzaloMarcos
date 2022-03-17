package ex_03_vips;

import java.io.*;
import java.util.*;

public class Analyser03 {
	
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
		boolean isVip;
		Queue<Integer> queue = new LinkedList<Integer>();
		Set<Integer> vipsInside = new HashSet<Integer>();
		
		System.out.println ("ANALYSER 03");
		System.out.println ("VIP customer behaviour and FIFO-policy for non VIPS analysed");
		System.out.println();
		
		while (provider.hasLine()) {
			line = provider.getLine().toUpperCase();
			numLine++;
			// skip blank lines
			if (line.isBlank()) continue;
			
			customerID = getCustomerID (line);
			isVip = isVIP(line);
			if (line.contains("ENTERING")) {
				if (isVip) vipsInside.add(customerID); 
				else queue.add(customerID);
			}
			switch (state) {
			case OPEN:
				if (line.contains("SIT")) {
					inBar++;
					if (inBar==5) state=FULL;
					
					if (isVip) {
						if (!vipsInside.contains(customerID)) 
							throw new RuntimeException("inconsistent state. VIP Customer seating without having entered. Line: "+numLine);
						else vipsInside.remove(customerID);
					}
					else {
						if (queue.isEmpty())
							throw new RuntimeException("inconsistent state. Customer seating without having entered. Line: "+numLine);
						
						if (!vipsInside.isEmpty())
							throw new RuntimeException("Vip preference violation. Normal customer sitting when vip waiting. Line: "+numLine);	
						
						int validID = queue.remove();
						if (customerID!=validID) 
							throw new RuntimeException("Order violation. Sitting: "+customerID+" Expencting: "+validID+" Line: "+numLine);	
					}
				}
				else if (line.contains("LEAVING")) {
					inBar--;
					if (inBar<0) 
						throw new RuntimeException ("inconsistent state. Customer leaving an empty bar. Line: "+numLine);
				}
				else if (line.contains("WAIT")) {
					if (inBar<5)
					System.out.println("------ suspicious wait -------. Line: "+ numLine);
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
					if (inBar==0) {state = OPEN;
					// System.out.println("REOPENING: "+numLine);
					}
					
				}
				else if (line.contains("SIT")) 
					if (isVip) {
						if (!vipsInside.contains(customerID))
							throw new RuntimeException("inconsistent state. VIP Customer seating without having entered. Line: "+numLine);
						else if (inBar!=5) {
							inBar++; 
							state=OPEN;
							vipsInside.remove(customerID);
							//System.out.println("VIP REOPENING with "+(inBar-1)+"in line: "+numLine);
						}
						else throw new RuntimeException ("Error. Sitting when bar is full. Line: "+numLine);
					}
					else throw new RuntimeException ("Error. Sitting when party in bar (no vip). Line: "+numLine);
					
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
	
	static boolean isVIP (String line) {
		return line.contains("VIP");
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
