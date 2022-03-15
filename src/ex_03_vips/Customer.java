package ex_03_vips;

public class Customer extends Thread {
	
	private int id;
	private SushiMonitor_03 monitor;
	
	public Customer (int id, SushiMonitor_03 monitor) {
		this.id = id;
		this.monitor = monitor;
	}
	
	public void run () {
		for (int i=0; i<30; i++) {
			try {Thread.sleep(125+(int)(2500*Math.random()));} catch(InterruptedException ie) {}
			monitor.enter(id);
			try {Thread.sleep(125+(int)(250*Math.random()));} catch(InterruptedException ie) {}
			System.out.println("\tEATING MY SUSHI "+"C("+id+")");
			try {Thread.sleep(125+(int)(125*Math.random()));} catch(InterruptedException ie) {}
			monitor.exit(id);
		}
	}
	
}
