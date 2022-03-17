package ex_03_vips;

import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class SushiMonitor_03 {

	Lock accessGrantor = new ReentrantLock(false);
	volatile int emptySeats = 5;
	volatile boolean groupEating = false;
	Condition canEnter = accessGrantor.newCondition();
	LinkedList<Integer> waitingQueue = new LinkedList<Integer>();
	volatile int waitingVips = 0;

	public void enterVIP(int i) {
		accessGrantor.lock();
		waitingVips++;

		System.out.println("----> Entering VIPC(" + i + ")");

		while (emptySeats <= 0) {
			canEnter.awaitUninterruptibly();
		}

		System.out.println("+++ [free: " + emptySeats + "] I sit down VIPC(" + i + ")");
		emptySeats--;
		waitingVips--;
		groupEating = false;
		accessGrantor.unlock();
	}

	public void exitVIP(int i) {
		accessGrantor.lock();
		emptySeats++;
		System.out.println("---> now leaving [free: " + emptySeats + "] VIPC(" + i + ")");

		if (emptySeats == 5) {
			groupEating = false;
		}

		canEnter.signal();

		accessGrantor.unlock();
	}

	public void enter(int i) {
		/* COMPLETE */
		accessGrantor.lock();
		System.out.println("----> Entering C(" + i + ")");

		if (groupEating) {
			System.out.println("*** I'm told to wait for all free C(" + i + ")");
		}

		if (!waitingQueue.contains(i))
			waitingQueue.add(i);
		while (waitingVips > 0 || groupEating || emptySeats <= 0 || i != waitingQueue.get(0)) {
			if (emptySeats <= 0 && !groupEating) {
				System.out.println("*** Possible group detected. I wait C(" + i + ") ");
				groupEating = true;
			}
			canEnter.signal();
			canEnter.awaitUninterruptibly();
		}

		waitingQueue.pop();

		System.out.println("+++ [free: " + emptySeats + "] I sit down C(" + i + ")");
		emptySeats--;
		accessGrantor.unlock();
	}

	public void exit(int i) {

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
