package ex_01;

public class Customer extends Thread {
	
	private int id;
	private SushiMonitor_01 monitor;
	
	public Customer (int id, SushiMonitor_01 monitor) {
		this.id = id;
		this.monitor = monitor;
	}
	
	public void run () {
		for (int i=0; i<40; i++) {
			try {Thread.sleep(125+(int)(2500*Math.random()));} catch(InterruptedException ie) {}
			monitor.enter(id);
			try {Thread.sleep(125+(int)(250*Math.random()));} catch(InterruptedException ie) {}
			System.out.println("\tEATING MY SUSHI "+"C("+id+")");
			try {Thread.sleep(125+(int)(125*Math.random()));} catch(InterruptedException ie) {}
			monitor.exit(id);
		}
	}
	
}
