package dev.michals3r3k;

import dev.michals3r3k.antalgorithm.AntAlgorithm;
import dev.michals3r3k.antalgorithm.RouteWithDistance;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;
import dev.michals3r3k.graph.service.GraphReader;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        List<String> filenames = Arrays.asList("berlin52.txt");
//        List<String> filenames = Arrays.asList("berlin52.txt", "bier127.txt",
//            "tsp250.txt", "tsp500.txt", "tsp1000.txt");
        for(String filename : filenames)
        {
//            printDistanceAndRoute(filename);
            Graph graph = new GraphReader(filename).getGraph();
            AntAlgorithm antAlgorithm = new AntAlgorithm(graph, 40.0, 30, 30.0);
            List<RouteWithDistance> routeWithDistances = antAlgorithm.runAlgorithm().stream()
                .sorted(Comparator.comparing(RouteWithDistance::getDistance))
                .collect(Collectors.toList());
            for(RouteWithDistance route : routeWithDistances)
            {
                String routeStr = route.getRoute().stream().map(Node::getId).map(
                    Objects::toString).collect(
                    Collectors.joining("->", "\n", ""));
                System.out.println(routeStr);
                System.out.println("Distance: " + route.getDistance() + "\n");
            }

        }

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
