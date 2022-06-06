package dev.michals3r3k;

import dev.michals3r3k.antalgorithm.AntAlgorithm;
import dev.michals3r3k.antalgorithm.AntEdge;
import dev.michals3r3k.antalgorithm.RouteWithDistance;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;
import dev.michals3r3k.graph.service.GraphReader;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {

        long start = System.currentTimeMillis();
//        List<String> filenames = Arrays.asList("berlin52.txt");
        List<String> filenames = Arrays.asList("berlin52.txt", "bier127.txt",
            "tsp250.txt", "tsp500.txt", "tsp1000.txt");
        for(String filename : filenames)
        {
            Graph graph = new GraphReader(filename).getGraph();
            System.out.println(filename);
            AntAlgorithm antAlgorithm = new AntAlgorithm(
                graph,
                2000,
                0.1,
                3,
                0.9,
                0.95);
            List<AntEdge> antEdges = antAlgorithm.runAlgorithm();
            Double distance = antEdges.stream().map(AntEdge::getDistance).reduce(
                0.0, Double::sum);
            List<Node> route = getRoute(antEdges);
            String routeStr = route.stream().map(Node::getId).map(
                    Objects::toString).collect(
                    Collectors.joining("->", "\n", ""));
                System.out.println(routeStr);
                System.out.println("Distance: " + distance + "\n");

        }
        long end = System.currentTimeMillis();
        double minuty = Math.floor((end-start / 1000)/60);
        double sekundy = (end-start / 1000) % 60;
        System.out.println(String.format("Time: %s min, %s sec", minuty, sekundy));

    }

    private static List<Node> getRoute(final List<AntEdge> edges)
    {
        if(edges.isEmpty())
        {
            return Collections.emptyList();
        }
        final AntEdge firstEdge = edges.iterator().next();
        final List<Node> nodes = edges.stream().map(AntEdge::getEndNode).collect(
            Collectors.toList());
        return Stream.concat(Stream.of(firstEdge.getStartNode()), nodes.stream())
            .collect(Collectors.toList());
    }

    private static List<Node> getGreedyTspRoute(final Graph graph)
    {
        List<Node> visited = new ArrayList<>();

        Node currentNode = graph.getNodes().get(0);
        while(!isContainsAllNodes(graph, visited))
        {
            visited.add(currentNode);
            currentNode = getNextNotVisitedNode(graph, visited, currentNode);
        }
        return visited;
    }

    private static void printNodes(final Graph graph)
    {
        for(Node node : graph.getNodes())
        {
            System.out.println(
                node.getId() + " " + node.getX() + " " + node.getY());
        }
    }

    private static boolean isContainsAllNodes(final Graph graph,
        final List<Node> visited)
    {
        for(Node node : graph.getNodes())
        {
            if(!visited.contains(node))
            {
                return false;
            }

        }
        return true;
    }

    private static Node getNextNotVisitedNode(final Graph graph,
        final List<Node> visited, Node currentNode)
    {
        return graph.getNeighbours(currentNode).stream().filter(
            Predicate.not(visited::contains)).findFirst().orElse(null);
    }

    private static void printPath(final List<Node> visited)
    {
        String path = visited.stream().map(
            node -> Integer.toString(node.getId())).collect(
            Collectors.joining(" -> "));
        System.out.println(path);
    }

    private static void printEdges(final double[][] graphDistances)
    {
        for(int i = 0; i < graphDistances.length; ++i)
        {
            for(int j = 0; j < graphDistances[i].length; ++j)
            {
                System.out.println(
                    i + " - " + j + " -> " + graphDistances[i][j]);
                System.out.println();
            }
        }
    }

}
