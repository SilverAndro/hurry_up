package dev.silverandro.hurry_up;

public interface PlayerTimer {
	void hurry_up$setDefused(boolean isDefused);
	void hurry_up$setTicks(int count);
	void hurry_up$setlastDeathTime(long time);

	boolean hurry_up$isDefused();
	int hurry_up$getRemainingTicks();
	long hurry_up$getLastDeathTime();
}
