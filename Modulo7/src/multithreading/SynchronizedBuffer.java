package multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedBuffer implements Buffer {
	
	private Lock accessLock = new ReentrantLock();
	
	private Condition canStartCountdown = accessLock.newCondition();
	private Condition canGenerateCharacters = accessLock.newCondition();
	
	private int buffer = -1;
	private boolean occupied = false;
	
	@Override
	public void startCountdown(int value) {
		accessLock.lock();
		try {
			while(!occupied) {
				System.out.println("Starting countdown.");
				canGenerateCharacters.await();
			}
			buffer = value;
			occupied = true;
			canStartCountdown.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			accessLock.unlock();
		}
	}
	
	@Override
	public void stopCountdown(int value) {
		accessLock.lock();
		try {
			while (occupied) {
				System.out.println("Stop countdown timer");
				canStartCountdown.await();	
			}
			buffer = value;
			occupied = false;
			canGenerateCharacters.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			accessLock.unlock();
		}
	}

	@Override
	public int getCountdownValue() {
		return buffer;
	}
}
