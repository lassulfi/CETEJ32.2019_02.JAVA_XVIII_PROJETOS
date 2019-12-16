package multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.swing.JLabel;

public class ThreadObject extends Thread {

	private Lock lockObject; // application lock; passed in to constructor
	private Condition suspend; // used to suspend and resume thread
	private boolean suspended = false; // true if thread suspended
	private JLabel countdownJLabel; // the count down JLabel
	private Buffer sharedLocation; // reference to the shared object
	private int timeValue;

	public ThreadObject(Lock theLock, JLabel label, Buffer shared) {
		this.lockObject = theLock;
		this.suspend = lockObject.newCondition();
		this.countdownJLabel = label;
		this.sharedLocation = shared;
	}

	@Override
	public void run() {
		try {
			this.startCountdown();
			while (suspended) {
				timeValue = this.sharedLocation.getCountdownValue();
				this.sharedLocation.stopCountdown(timeValue);
				suspend.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lockObject.unlock();
		}
	}

	private void startCountdown() throws InterruptedException {
		lockObject.lock();
		
		timeValue = this.sharedLocation.getCountdownValue();
		while (timeValue > 0) {
			Thread.sleep(1000L);
			timeValue--;
			this.countdownJLabel.setText(String.valueOf(timeValue));
		}
	}
}
