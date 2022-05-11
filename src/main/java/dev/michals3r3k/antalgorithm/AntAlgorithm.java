package dev.michals3r3k.antalgorithm;

import com.google.common.base.Functions;
import com.google.common.collect.*;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AntAlgorithm
{
    private static final double STARING_EDGE_PHEROMONE = 1.0;
    private final ListMultimap<Node, AntEdge> edgeMap;
    private List<Ant> ants;

    public AntAlgorithm(
        final Graph graph,
        final double pheromone,
        final int antQuantity,
        final double evaporation)
    {
        this.edgeMap = getEdgeMap(graph, evaporation);
        initAnts(graph, antQuantity, pheromone);
    }

    private void initAnts(final Graph graph, final int antQuantity, final double pheromone)
    {
        this.ants = IntStream.range(0, antQuantity - 1)
            .mapToObj(x -> new Ant(graph, edgeMap, pheromone))
            .collect(Collectors.toList());
    }

    private static ListMultimap<Node, AntEdge> getEdgeMap(final Graph graph, final double evaporation)
    {
        return graph.getEdges().stream()
            .map(edge -> new AntEdge(edge, STARING_EDGE_PHEROMONE, evaporation))
            .collect(Multimaps.toMultimap(
                AntEdge::getStartNode,
                Functions.identity(),
                ArrayListMultimap::create));
    }

    public List<RouteWithDistance> runAlgorithm()
    {
        ants.forEach(Ant::run);
        return ants.stream().map(Ant::getRouteWithDistance).collect(
            Collectors.toList());
    }

}
