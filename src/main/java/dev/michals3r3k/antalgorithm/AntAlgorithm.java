package dev.michals3r3k.antalgorithm;

import com.google.common.base.Functions;
import com.google.common.collect.*;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class AntAlgorithm
{
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
        for(int i = 0; i < iterations; ++i)
        {
            AntAlgorithmIteration iteration = new AntAlgorithmIteration(
                graph, antQuantity, edgeMap, i, tau0, randomProperty);
            iteration.runIteration();
            iteration.putBestResultWithoutOpt(result);
            if(i == 0)
            {
                tau0 = 1 / ( graph.getNodeQuantity() * result.getDistance() );
                for(AntEdge edge : edgeMap.values())
                {
                    edge.setPheromone(tau0);
                }
            }
            for(AntEdge edge : edgeMap.values())
            {
                edge.setPheromone(edge.getPheromone() * alpha);
            }
            for(AntEdge edge : result.getEdges())
            {
                AntEdge returning = getEdge(edge.getEndNode(), edge.getStartNode());
                returning.setPheromone(returning.getPheromone() + (alpha / result.getDistance()));
                edge.setPheromone(edge.getPheromone() + (alpha / result.getDistance()));
            }
        }
        return result;
    }
    public List<AntEdge> runAlgorithm1()
    {
        double tau0 = 1;
        List<AntEdge> bestGlobalRoute = new ArrayList<>();
        double globalBestDistance = Double.MAX_VALUE;
        for(int i = 0; i < iterations; ++i)
        {
            AntAlgorithmIteration iteration = new AntAlgorithmIteration(
                graph, antQuantity, edgeMap, i, tau0, randomProperty);
            iteration.runIteration();
//            List<AntEdge> bestRoute = iteration.getBestRoute(globalBestDistance);
            List<AntEdge> bestRoute = iteration.getBestRoute();
            bestGlobalRoute = bestRoute;
            double distance = bestRoute.stream()
                .map(AntEdge::getDistance)
                .reduce(0.0, Double::sum);
            if(distance < globalBestDistance)
            {
                globalBestDistance = distance;
            }

            if(i == 0)
            {
                tau0 = 1 / ( graph.getNodeQuantity() * globalBestDistance );
                for(AntEdge edge : edgeMap.values())
                {
                    edge.setPheromone(tau0);
                }
            }
            for(AntEdge edge : edgeMap.values())
            {
                edge.setPheromone(edge.getPheromone() * alpha);
            }
            for(AntEdge edge : bestGlobalRoute)
            {
                AntEdge returning = getEdge(edge.getEndNode(), edge.getStartNode());
                returning.setPheromone(returning.getPheromone() + (alpha / globalBestDistance));
                edge.setPheromone(edge.getPheromone() + (alpha / globalBestDistance));
            }
        }
        return bestGlobalRoute;
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
