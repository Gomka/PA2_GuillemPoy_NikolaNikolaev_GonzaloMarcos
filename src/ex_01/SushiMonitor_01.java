package ex_01;

import java.nio.channels.AcceptPendingException;
import java.util.concurrent.locks.*;

public class SushiMonitor_01 {

	/* COMPLETE */
	Lock accessGrantor = new ReentrantLock(false);
	volatile int emptySeats = 5;
	volatile boolean groupEating = false;
	Condition canEnter = accessGrantor.newCondition();

	public void enter(int i) {
		/* COMPLETE */
		accessGrantor.lock();
		System.out.println("----> Entering C(" + i + ")");

		if (groupEating) {
			System.out.println("*** I'm told to wait for all free C(" + i + ")");
		}

		while (groupEating || emptySeats <= 0) {
			if (emptySeats <= 0 && !groupEating) {
				System.out.println("*** Possible group detected. I wait C(" + i + ") ");
				groupEating = true;
			}

			canEnter.awaitUninterruptibly();
		}

		System.out.println("+++ [free: " + emptySeats + "] I sit down C(" + i + ")");
		emptySeats--;
		accessGrantor.unlock();
	}

	public void exit(int i) {
		/* COMPLETE */
		accessGrantor.lock();
		emptySeats++;
		System.out.println("---> now leaving [free: " + emptySeats + "] C(" + i + ")");

		if (emptySeats == 5) {
			groupEating = false;
		}

		canEnter.signal();

		accessGrantor.unlock();
	}
}
