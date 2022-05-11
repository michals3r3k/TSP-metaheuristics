package dev.michals3r3k.antalgorithm;

import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteWithDistance
{
    private final List<Node> route;
    private final double distance;

    public RouteWithDistance(final double distance, final List<Graph.Edge> edges)
    {
        this.route = getRoute(edges);
        this.distance = distance;
    }

    private static List<Node> getRoute(final List<Graph.Edge> edges)
    {
        if(edges.isEmpty())
        {
            return Collections.emptyList();
        }
        final Graph.Edge firstEdge = edges.iterator().next();
        final List<Node> nodes = edges.stream().map(Graph.Edge::getEndNode).collect(
            Collectors.toList());
        return Stream.concat(Stream.of(firstEdge.getStartNode()), nodes.stream())
            .collect(Collectors.toList());
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
