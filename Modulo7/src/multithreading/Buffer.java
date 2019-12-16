package multithreading;

public interface Buffer {

	public void startCountdown(int value);
	
	public void stopCountdown(int value);

	public int getCountdownValue();
}
