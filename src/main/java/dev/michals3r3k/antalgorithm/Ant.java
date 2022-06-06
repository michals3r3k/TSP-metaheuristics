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
    private final double tau0;

    private final Set<Node> visited;
    private final List<AntEdge> visitedEdges;
    private final List<AntEdge> routeEdges;
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

    public RouteWithDistance getRouteWithDistance()
    {
        final List<Graph.Edge> edges = routeEdges.stream().map(
            AntEdge::getEdge).collect(Collectors.toList());
        return new RouteWithDistance(getDistance(), edges);
    }

    public double getDistance()
    {
        return routeEdges.stream()
            .map(AntEdge::getDistance)
            .reduce(0.0, Double::sum);
    }

    public List<AntEdge> getAntRoute()
    {
        return routeEdges;
    }

    public boolean isFinishedTrace()
    {
        return finishedTrace;
    }

    public void run(int iterationNumber, double randomProperty)
    {
        final Node startingNode = getStartNode();
        Node currentNode = startingNode;
        visited.add(currentNode);
        while(graph.getNodeQuantity() != visited.size())
        {
            final AntEdge edge = pickNextEdge(currentNode, randomProperty);
            final Node nextNode = edge.getEndNode();
            currentNode = nextNode;
            AntEdge returningEdge = getEdge(edge.getEndNode(), edge.getStartNode());

            //to zadziała!
            if(iterationNumber > 0)
            {
                edge.updatePheromone(tau0);
                returningEdge.updatePheromone(tau0);
            }

            visited.add(nextNode);
            routeEdges.add(edge);
            visitedEdges.add(edge);
            visitedEdges.add(returningEdge);
        }
        AntEdge lastEdge = getEdge(currentNode, startingNode);
        AntEdge returningLastEdge = getEdge(lastEdge.getEndNode(), lastEdge.getStartNode());
        routeEdges.add(lastEdge);
        visitedEdges.add(lastEdge);
        visitedEdges.add(returningLastEdge);
        this.finishedTrace = true;
    }

    private Node getStartNode()
    {
        List<Node> nodes = new ArrayList<>(graph.getNodes());
        Collections.shuffle(nodes);
        final Node currentNode = nodes.iterator().next();
        visited.add(currentNode);
        return currentNode;
    }

    private AntEdge pickNextEdge(final Node currentNode, double randomProperty)
    {
        List<AntEdge> edgesToNotVisitedNodes = new ArrayList<>();
        getEdgesToNotVisitedNodes(currentNode).stream()
            .sorted(Comparator.comparing(AntEdge::getAttraction).reversed())
            .forEachOrdered(edgesToNotVisitedNodes::add);

        AntEdge result = null;
        do
        {
            result = edgesToNotVisitedNodes.get(0);
            edgesToNotVisitedNodes.remove(0);
        }
        while(rand.nextDouble() > randomProperty && edgesToNotVisitedNodes.size() > 0);
        return result;



//        final double pick = getPick(edgesToNotVisitedNodes);
//        return getChances(edgesToNotVisitedNodes).stream()
//            .filter(chance -> isContains(pick, chance))
//            .findFirst()
//            .map(Chance::getEdge)
//            .orElseThrow(() -> new IllegalStateException("Cannot find next node."));
    }

//    private boolean isContains(double pick, Chance chance)
//    {
//        return chance.contains(pick);
//    }

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

//    private static double getPick(final List<AntEdge> edgesToNotVisitedNodes)
//    {
//        double wholeAttraction = edgesToNotVisitedNodes.stream()
//            .map(AntEdge::getAttraction)
//            .reduce(0.0, Double::sum);
//        return rand.nextDouble() * wholeAttraction;
//    }
//
//    private static List<Chance> getChances(
//        final List<AntEdge> edgesToNotVisitedNodes)
//    {
//        double chanceBefore = 0.0;
//        ImmutableList.Builder<Chance> builder = ImmutableList.builder();
//        for(final AntEdge edge : edgesToNotVisitedNodes)
//        {
//            final Range<Double> chanceRange = Range.closed(chanceBefore,
//                chanceBefore + edge.getAttraction());
//            builder.add(new Chance(chanceRange, edge));
//            chanceBefore = chanceRange.upperEndpoint();
//        }
//        return builder.build();
//    }
//
//    private void updateEdges()
//    {
//        edgeMap.values().forEach(AntEdge::evaporate);
//        final Double distance = this.getDistance();
//        visitedEdges.forEach(edge -> edge.spreadPheromone(distance, this.pheromone));
//    }

}
