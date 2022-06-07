package dev.michals3r3k.antalgorithm;

import dev.michals3r3k.utils.EdgeUtils;

import java.util.Collections;
import java.util.List;

public class AntAlgorithmResult
{
    private List<AntEdge> edges;
    private double distance;

    public AntAlgorithmResult()
    {
        this.edges = Collections.emptyList();
        this.distance = Double.MAX_VALUE;
    }

    boolean update(List<AntEdge> edges)
    {
        double distance = EdgeUtils.getDistance(edges);
        if(distance < this.distance)
        {
            this.distance = distance;
            this.edges = edges;
            return true;
        }
        return false;
    }

    public double getDistance()
    {
        return distance;
    }

    public List<AntEdge> getEdges()
    {
        return edges;
    }

    public boolean isEmpty()
    {
        return edges.isEmpty();
    }

}
