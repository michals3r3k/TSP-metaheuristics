package dev.michals3r3k.antalgorithm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AntAlgorithmIteration
{
    private final ListMultimap<Node, AntEdge> edgeMap;
    private final int iterationNumber;
    private final double randomProperty;
    private final List<Ant> ants;

    public AntAlgorithmIteration(
        final Graph graph,
        final int antQuantity,
        final ListMultimap<Node, AntEdge> edgeMap,
        final int iterationNumber,
        double tau0,
        double randomProperty)
    {
        this.edgeMap = edgeMap;
        this.iterationNumber = iterationNumber;
        this.randomProperty = randomProperty;
        this.ants = IntStream.range(0, antQuantity - 1)
            .mapToObj(x -> new Ant(graph, edgeMap, tau0))
            .collect(Collectors.toList());

    }

    void runIteration()
    {
        ants.forEach(ant -> ant.run(iterationNumber, randomProperty));
    }
    List<AntEdge> getBestRoute(double globalBest)
    {
        double bestDistance = globalBest;
        List<AntEdge> result = Collections.emptyList();
        for(Ant ant : ants)
        {
            double bestAntDistance = ant.getDistance();
            List<AntEdge> antRoute = ant.getAntRoute();
            result = antRoute;
            for(int i = 0; i < 10; i++)
            {
                List<AntEdge> firstBetter = findFirstBetter(edgeMap,
                    bestAntDistance, antRoute);
                double betterDistance = getFullDistance(firstBetter);
                if(betterDistance < bestDistance)
                {
                    bestDistance = betterDistance;
                    result = firstBetter;
                }
                else
                {
                    break;
                }
            }
        }
        return result;
    }

    private List<AntEdge> findFirstBetter(ListMultimap<Node, AntEdge> edgeMap,
        double bestAntDistance, List<AntEdge> antRoute)
    {
        List<Node> nodesOnRoad = antRoute.stream()
            .map(AntEdge::getStartNode)
            .collect(Collectors.toList());
        for(int start = 0; start < nodesOnRoad.size() - 2; start++)
        {
            for(int end = start + 1; end < nodesOnRoad.size() - 1; end++)
            {
                List<Node> better = getBetter(nodesOnRoad, start, end);
                List<AntEdge> edges = getEdges(edgeMap, better);
                Double distance = getFullDistance(edges);
                if(distance < bestAntDistance)
                {
                    return edges;
                }
            }
        }
        return antRoute;
    }

    private Double getFullDistance(List<AntEdge> edges)
    {
        return edges.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

    private List<AntEdge> getEdges(ListMultimap<Node, AntEdge> edgeMap,
        List<Node> better)
    {
        return IntStream.range(0,
                better.size() - 1)
            .mapToObj(i -> getAntEdge(edgeMap, better, i))
            .collect(Collectors.toList());
    }

    private AntEdge getAntEdge(ListMultimap<Node, AntEdge> edgeMap,
        List<Node> better, int i)
    {
        Node starting = better.get(i);
        Node ending = better.get(i + 1);
        return edgeMap.get(starting).stream()
            .filter(edge -> ending.equals(edge.getEndNode()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException("Error"));
    }


    private List<Node> getBetter(List<Node> nodesOnRoad, int start, int end)
    {
        ImmutableList.Builder<Node> builder = ImmutableList.builder();
        List<Node> nodesPart = new ArrayList<>(nodesOnRoad.subList(start, end));
        Collections.reverse(nodesPart);
        builder.addAll(new ArrayList<>(nodesOnRoad.subList(0, start)));
        builder.addAll(nodesPart);
        builder.addAll(new ArrayList<>(nodesOnRoad.subList(end, nodesOnRoad.size())));
        List<Node> build = builder.build();
        return build;
    }

    private double opt2(Ant ant)
    {
        double bestDistance = ant.getDistance();
        List<AntEdge> antRoute = ant.getAntRoute();
        int i = 0;
        boolean poprawa = true;
        while(i < 10 && poprawa)
        {

        }
        return bestDistance;
    }

    private List<AntEdge> getBetter(Ant ant)
    {
        double bestDistance = ant.getDistance();
        List<AntEdge> antRoute = ant.getAntRoute();
        start:
        for(int start = 0; start < antRoute.size() - 2; ++start)
        {
            for(int end = start + 1; end < antRoute.size() - 1; ++end)
            {
                AntEdge[] edges = (AntEdge[]) antRoute.subList(start,
                    end).toArray();
                ArrayUtils.reverse(edges);
                List<AntEdge> reversed = Arrays.asList(edges);
                List<AntEdge> localBest = new ArrayList<>();
                for(int x = 0; x < start; ++x)
                {
                    localBest.add(antRoute.get(x));
                }
                for(int x = start; x <= end; ++x)
                {
                    localBest.add(reversed.get(x - start));
                }
                for(int x = end; x < antRoute.size() - 1; ++x)
                {
                    localBest.add(reversed.get(x));
                }
                double distance = getDistance(localBest);
                if(distance < bestDistance)
                {
                    bestDistance = distance;
                    return localBest;
                }
            }
        }
        return antRoute;
    }

    private static double getDistance(List<AntEdge> route)
    {
        return route.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

}
