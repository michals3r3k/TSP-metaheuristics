package dev.michals3r3k.antalgorithm;

import com.google.common.collect.ListMultimap;
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
    private final List<AntEdge> routeEdges;

    private final double tau0;
    private boolean finishedTrace;

    public Ant(
        final Graph graph,
        final ListMultimap<Node, AntEdge> edgeMap,
        double tau0)
    {
        this.graph = graph;
        this.edgeMap = edgeMap;
        this.tau0 = tau0;
        this.visited = new HashSet<>();
        this.visitedEdges = new ArrayList<>();
        this.routeEdges = new ArrayList<>();
        this.finishedTrace = false;
    }

    public double getDistance()
    {
        return routeEdges.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

    public List<AntEdge> getRoute()
    {
        return routeEdges;
    }

    public boolean isFinishedTrace()
    {
        return finishedTrace;
    }

    public boolean run(int iterationNumber, double randomProperty, boolean firstAnt, AlgorithmTimer timer)
    {
        final Node startingNode = getStartNode(firstAnt);
        Node currentNode = startingNode;
        visited.add(currentNode);
        while(graph.getNodeQuantity() != visited.size())
        {
            final AntEdge edge = pickNextEdge(currentNode, randomProperty, firstAnt);
            final Node nextNode = edge.getEndNode();
            currentNode = nextNode;
            AntEdge returningEdge = getEdge(edge.getEndNode(), edge.getStartNode());

            // to zadziała!
            if(iterationNumber > 0)
            {
                edge.updatePheromone(tau0);
                returningEdge.updatePheromone(tau0);
            }

            visited.add(nextNode);
            routeEdges.add(edge);
            visitedEdges.add(edge);
            visitedEdges.add(returningEdge);
            if(timer.isTimeEnd())
            {
                return false;
            }
        }
        AntEdge lastEdge = getEdge(currentNode, startingNode);
        AntEdge returningLastEdge = getEdge(lastEdge.getEndNode(), lastEdge.getStartNode());
        routeEdges.add(lastEdge);
        visitedEdges.add(lastEdge);
        visitedEdges.add(returningLastEdge);
        this.finishedTrace = true;
        return true;
    }

    private Node getStartNode(boolean firstAnt)
    {
        if(firstAnt)
        {
            return graph.getNodes().stream()
                .filter(node -> node.getIndex() == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find node with index 0"));
        }
        List<Node> nodes = new ArrayList<>(graph.getNodes());
        Collections.shuffle(nodes);
        final Node currentNode = nodes.iterator().next();
        visited.add(currentNode);
        return currentNode;
    }

    private AntEdge pickNextEdge(final Node currentNode, double randomProperty,
        boolean firstAnt)
    {
        List<AntEdge> edgesToNotVisitedNodes = new ArrayList<>();

        // sortowanie od najlepszej atrakcyjności
        getEdgesToNotVisitedNodes(currentNode).stream()
            .sorted(Comparator.comparing(AntEdge::getAttraction).reversed())
            .forEachOrdered(edgesToNotVisitedNodes::add);

        if(firstAnt)
        {
            return edgesToNotVisitedNodes.get(0);
        }

        // jeśli wylosowano poniżej randomProperty, to zwracamy pierwszy
        // w przeciwnym wypadku zwracamy losowy.
        AntEdge result;
        do
        {
            result = edgesToNotVisitedNodes.get(0);
            edgesToNotVisitedNodes.remove(0);
        }
        while(rand.nextDouble() > randomProperty && edgesToNotVisitedNodes.size() > 0);
        return result;
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

}
