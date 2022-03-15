package ex_03_vips;



public class Launcher03 {

	public static void main (String [] args) {
		Customer [] customers = new Customer[20];
		CustomerVip [] customersVip = new CustomerVip[3];
		SushiMonitor_03 monitor = new SushiMonitor_03();
		
		for (int i=0; i<customers.length; i++) {
			customers[i] = new Customer(i, monitor);
			customers[i].start();
		}
		
		for (int i=0; i<customersVip.length; i++) {
			customersVip[i] = new CustomerVip(i, monitor);
			customersVip[i].start();
		}
		
		System.out.println ("Launching v3 VIPC(0000)");
		
		for (int i=0; i<customers.length; i++) {
			try {customers[i].join();} catch (InterruptedException e) {}
		}
		for (int i=0; i<customersVip.length; i++) {
			try {customersVip[i].join();} catch (InterruptedException e) {}
		}
		
	}
}
