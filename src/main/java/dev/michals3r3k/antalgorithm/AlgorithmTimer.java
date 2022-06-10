package dev.michals3r3k.antalgorithm;

public class AlgorithmTimer
{
    private final long timeToFinish;
    private final long startTime;

    public AlgorithmTimer(int minutes, int seconds)
    {
        long secondMillis = seconds * 1000;
        long minutesMillis = minutes * 60 * 1000;
        this.timeToFinish = minutesMillis + secondMillis;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isTimeEnd()
    {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= timeToFinish;
    }

}
