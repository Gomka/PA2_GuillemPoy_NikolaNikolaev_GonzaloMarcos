package ex_01;

public class Launcher01 {

	public static void main(String[] args) {
		Customer[] customers = new Customer[20];
		SushiMonitor_01 monitor = new SushiMonitor_01();

		System.out.println("Launching v1 C(0000)");
		System.out.println();

		for (int i = 0; i < customers.length; i++) {
			customers[i] = new Customer(i, monitor);
			customers[i].start();
		}

		for (int i = 0; i < customers.length; i++) {
			try {
				customers[i].join();
			} catch (InterruptedException e) {
			}
		}

	}
}
