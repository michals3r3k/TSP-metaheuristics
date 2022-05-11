package dev.michals3r3k.antalgorithm;

import dev.michals3r3k.graph.Node;

import java.util.List;

public class RouteWithDistance
{
    private final List<Node> route;
    private final double distance;

    public RouteWithDistance(final List<Node> route, final double distance)
    {
        this.route = route;
        this.distance = distance;
    }

    public List<Node> getRoute()
    {
        return route;
    }

    public double getDistance()
    {
        return distance;
    }

}
