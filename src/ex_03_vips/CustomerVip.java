package ex_03_vips;

public class CustomerVip extends Thread {
	
	private int id;
	private SushiMonitor_03 monitor;
	
	public CustomerVip (int id, SushiMonitor_03 monitor) {
		this.id = id;
		this.monitor = monitor;
	}
	
	public void run () {
		for (int i=0; i<30; i++) {
			try {Thread.sleep(125+(int)(2500*Math.random()));} catch(InterruptedException ie) {}
			monitor.enterVIP(id);
			try {Thread.sleep(125+(int)(250*Math.random()));} catch(InterruptedException ie) {}
			System.out.println("\tEATING MY SUSHI "+"VIPC("+id+")");
			try {Thread.sleep(125+(int)(125*Math.random()));} catch(InterruptedException ie) {}
			monitor.exitVIP(id);
		}
	}
	
}
