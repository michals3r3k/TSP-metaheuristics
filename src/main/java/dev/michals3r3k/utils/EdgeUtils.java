package dev.michals3r3k.utils;

import dev.michals3r3k.antalgorithm.AntEdge;
import dev.michals3r3k.graph.Node;

import java.util.List;
import java.util.stream.Collectors;

public class EdgeUtils
{

    public static double getDistance(List<AntEdge> edges)
    {
        return edges.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

    public static List<Node> getNodesOnRoad(List<AntEdge> edges)
    {
        return edges.stream()
            .map(AntEdge::getStartNode)
            .collect(Collectors.toList());
    }

}
