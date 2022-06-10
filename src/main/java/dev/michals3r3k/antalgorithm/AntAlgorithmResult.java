package dev.michals3r3k.antalgorithm;

import dev.michals3r3k.utils.EdgeUtils;

import java.util.Collections;
import java.util.List;

public class AntAlgorithmResult
{
    private List<AntEdge> bestRoute;
    private double bestDistance;
    private int timesOfGotBetter;

    public AntAlgorithmResult()
    {
        this.timesOfGotBetter=0;
        this.bestRoute = Collections.emptyList();
        this.bestDistance = Double.MAX_VALUE;

    }

    boolean update(List<AntEdge> newRoute)
    {
        double newRouteDistance = EdgeUtils.getDistance(newRoute);
        if(newRouteDistance < this.bestDistance)
        {
            this.bestDistance = newRouteDistance;
            this.bestRoute = newRoute;
            timesOfGotBetter++;
            return true;
        }
        return false;
    }

    public double getBestDistance()
    {
        return bestDistance;
    }

    public List<AntEdge> getBestRoute()
    {
        return bestRoute;
    }

    public boolean isEmpty()
    {
        return bestRoute.isEmpty();
    }

    public int getTimesOfGotBetter()
    {
        return timesOfGotBetter;
    }
}
