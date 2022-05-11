package dev.michals3r3k.antalgorithm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Range;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

public class Ant
{
    private static final Random rand = new Random();

    private final Graph graph;
    private final ListMultimap<Node, AntEdge> edgeMap;

    private final Set<Node> visited;
    private final List<AntEdge> visitedEdges;
    private final double pheromone;
    private boolean finishedTrace;

    public Ant(
        final Graph graph,
        final ListMultimap<Node, AntEdge> edgeMap,
        final double pheromone)
    {
        this.graph = graph;
        this.edgeMap = edgeMap;
        this.visited = new HashSet<>();
        this.visitedEdges = new ArrayList<>();
        this.pheromone = pheromone;
        this.finishedTrace = false;
    }

    public RouteWithDistance getRouteWithDistance()
    {
        final List<Graph.Edge> edges = visitedEdges.stream().map(
            AntEdge::getEdge).collect(Collectors.toList());
        return new RouteWithDistance(getDistance(), edges);
    }

    public double getDistance()
    {
        return visitedEdges.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

    public boolean isFinishedTrace()
    {
        return finishedTrace;
    }

    public void run()
    {
        final Node firstNode = getFirstNode();
        Node currentNode = firstNode;
        while(graph.getNodeQuantity() != visited.size())
        {
            final AntEdge edge = pickNextEdge(currentNode);
            final Node nextNode = edge.getEndNode();
            visited.add(nextNode);
            visitedEdges.add(edge);
            currentNode = nextNode;
        }
        visitedEdges.add(getEdge(currentNode, firstNode));
        updateEdges();
        this.finishedTrace = true;
    }

    private Node getFirstNode()
    {
        final Node currentNode = graph.getNodes().iterator().next();
        visited.add(currentNode);
        return currentNode;
    }

    private AntEdge pickNextEdge(final Node currentNode)
    {
        final List<AntEdge> edgesToNotVisitedNodes =
            getEdgesToNotVisitedNodes(currentNode);
        final double pick = getPick(edgesToNotVisitedNodes);
        return getChances(edgesToNotVisitedNodes).stream()
            .filter(chance -> isContains(pick, chance))
            .findFirst()
            .map(Chance::getEdge)
            .orElseThrow(() -> new IllegalStateException("Cannot find next node."));
    }

    private boolean isContains(double pick, Chance chance)
    {
        return chance.contains(pick);
    }

    private List<AntEdge> getEdgesToNotVisitedNodes(final Node currentNode)
    {
        return edgeMap.get(currentNode).stream()
            .filter(edge -> !visited.contains(edge.getEndNode()))
            .collect(Collectors.toList());
    }

    private AntEdge getEdge(final Node staringNode, final Node endingNode)
    {
        return edgeMap.get(staringNode).stream()
            .filter(edge -> endingNode.equals(edge.getEndNode()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Cannot find edge from "
                + staringNode.getId() + "to" + endingNode.getId()));
    }

    private static double getPick(final List<AntEdge> edgesToNotVisitedNodes)
    {
        double wholeAttraction = edgesToNotVisitedNodes.stream()
            .map(AntEdge::getAttraction)
            .reduce(0.0, Double::sum);
        return rand.nextDouble() * wholeAttraction;
    }

    private static List<Chance> getChances(
        final List<AntEdge> edgesToNotVisitedNodes)
    {
        double chanceBefore = 0.0;
        ImmutableList.Builder<Chance> builder = ImmutableList.builder();
        for(final AntEdge edge : edgesToNotVisitedNodes)
        {
            final Range<Double> chanceRange = Range.closed(chanceBefore,
                chanceBefore + edge.getAttraction());
            builder.add(new Chance(chanceRange, edge));
            chanceBefore = chanceRange.upperEndpoint();
        }
        return builder.build();
    }

    private void updateEdges()
    {
        edgeMap.values().forEach(AntEdge::evaporate);
        final Double distance = this.getDistance();
        visitedEdges.forEach(edge -> edge.spreadPheromone(distance, this.pheromone));
    }

}
