package dev.michals3r3k.antalgorithm;

import com.google.common.collect.Range;

public class Chance
{
    private final Range<Double> chanceRange;
    private final AntEdge edge;

    public Chance(final Range<Double> chanceRange, final AntEdge edge)
    {
        this.chanceRange = chanceRange;
        this.edge = edge;
    }

    public AntEdge getEdge()
    {
        return edge;
    }
    public boolean contains(double point)
    {
        return chanceRange.contains(point);
    }

}
