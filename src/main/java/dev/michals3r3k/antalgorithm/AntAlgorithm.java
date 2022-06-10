package dev.michals3r3k.antalgorithm;

import com.google.common.base.Functions;
import com.google.common.collect.*;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.List;

public class AntAlgorithm
{
    private static final int MAX_TIME_OF_WORK = 5;
    private static final double STARING_EDGE_PHEROMONE = 1.0;

    private final ListMultimap<Node, AntEdge> edgeMap;
    private final Graph graph;
    private final int antQuantity;
    private final int iterations;
    private final double alpha;
    private final double randomProperty;

    public AntAlgorithm(
        final Graph graph,
        final int antQuantity,
        final double evaporation,
        int iterations,
        double alpha,
        double randomProperty)
    {
        this.graph = graph;
        this.antQuantity = antQuantity;
        this.iterations = iterations;
        this.alpha = alpha;
        this.randomProperty = randomProperty;
        this.edgeMap = getEdgeMap(graph, evaporation);
    }

    public AntAlgorithmResult runAlgorithm()
    {
        double tau0 = 1;
        AntAlgorithmResult result = new AntAlgorithmResult();
        AlgorithmTimer timer = new AlgorithmTimer(MAX_TIME_OF_WORK, 0);
        for(int i = 0; i < iterations; ++i)
        {
            AntAlgorithmIteration iteration = new AntAlgorithmIteration(
                graph, antQuantity, edgeMap, i, tau0, randomProperty);

            if(!iteration.runIteration(timer, result))
            {
                return result;
            }

            iteration.putBestResultWithOpt(result, timer);

            if(timer.isTimeEnd())
            {
                return result;
            }
            tau0 = 1 / (graph.getNodeQuantity() * result.getBestDistance());
            updateEdges(result, tau0, i == 0);
        }
        return result;
    }

    private void updateEdges(AntAlgorithmResult result, double tau0, boolean firstIteration)
    {
        double pheromoneToAdd = alpha / result.getBestDistance();
        for(AntEdge edge : edgeMap.values())
        {
            if(firstIteration)
            {
                edge.setPheromone(tau0);
            }
            edge.setPheromone(edge.getPheromone() * (1 - alpha));
            if(isInBestRoute(edge, result.getBestRoute()))
            {
                AntEdge returning = getEdge(edge.getEndNode(), edge.getStartNode());
                returning.setPheromone(returning.getPheromone() + pheromoneToAdd);
                edge.setPheromone(edge.getPheromone() + pheromoneToAdd);
            }
        }
    }

    private boolean isInBestRoute(AntEdge edge, List<AntEdge> bestRoute)
    {
        return bestRoute.stream()
            .anyMatch(routeEdge -> routeEdge.getStartNode().equals(edge.getStartNode())
                && routeEdge.getEndNode().equals(edge.getEndNode()));
    }

    private AntEdge getEdge(final Node staringNode, final Node endingNode)
    {
        return edgeMap.get(staringNode).stream()
            .filter(edge -> endingNode.equals(edge.getEndNode()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Cannot find edge from "
                + staringNode.getId() + "to" + endingNode.getId()));
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

}
