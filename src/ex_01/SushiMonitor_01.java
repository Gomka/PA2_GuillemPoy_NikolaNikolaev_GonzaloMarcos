package ex_01;

import java.util.concurrent.locks.*;

public class SushiMonitor_01 {

	/* COMPLETE */
	Lock accessGrantor = new ReentrantLock(false);
	volatile int emptySeats = 5;
	volatile boolean groupEating = false;

	public void enter(int i) {
		/* COMPLETE */
		accessGrantor.lock();
		System.out.println("----> Entering C(" + i + ")");

		if (emptySeats == 0 && !groupEating) {
			System.out.println("*** Possible group detected. I wait C(" + i + ") ");
			groupEating = true;
		}

		if (groupEating) {
			System.out.println("*** I'm told to wait for all free C(" + i + ")");
		}

		while (groupEating || emptySeats == 0) {
			accessGrantor.unlock();
			Thread.yield();
			accessGrantor.lock();
		}
		emptySeats--;
		System.out.println("+++ [free: " + emptySeats + "] I sit down C(" + i + ")");
	}

	public void exit(int i) {
		/* COMPLETE */
		System.out.println("---> now leaving [free: " + emptySeats + "] C(" + i + ")");
		emptySeats++;

		if (emptySeats == 5) {
			groupEating = false;
		}

		accessGrantor.unlock();
	}
}
