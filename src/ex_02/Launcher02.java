package ex_02;



public class Launcher02 {

	public static void main (String [] args) {
		Customer [] customers = new Customer[20];
		SushiMonitor_02 monitor = new SushiMonitor_02();
		
		for (int i=0; i<customers.length; i++) {
			customers[i] = new Customer(i, monitor);
			customers[i].start();
		}
		
		System.out.println ("Launching v2 C(0000)");
		System.out.println();
		
		for (int i=0; i<customers.length; i++) {
			try {customers[i].join();} catch (InterruptedException e) {}
		}
		
	}
}
