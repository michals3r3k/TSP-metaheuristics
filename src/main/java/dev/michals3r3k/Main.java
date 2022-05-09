package dev.michals3r3k;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        List<String> filenames = Arrays.asList("berlin52.txt", "tsp250.txt", "bier127.txt", "tsp500.txt", "tsp1000.txt");
        for(String filename : filenames)
        {
            printDistanceAndRoute(filename);
        }
    }

    public static void printDistanceAndRoute(final String filename) throws FileNotFoundException
    {
        Graph graph = new GraphReader(filename).getGraph();
        TravellingSalesman travellingSalesman = new TravellingSalesman(graph);

        List<Node> route = travellingSalesman.getGreedyTspRoute(graph.getNodes().get(0));
        double routeDistance = travellingSalesman.getRouteDistance();

        System.out.println(filename);
        printPath(route);
        System.out.println("route distance: " + routeDistance);
        System.out.println();
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
            System.out.println(node.getId() + " " + node.getX() +  " " + node.getY());
        }
    }

    private static boolean isContainsAllNodes(final Graph graph, final List<Node> visited)
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
        return graph.getNeighbours(currentNode).stream()
            .filter(Predicate.not(visited::contains))
            .findFirst()
            .orElse(null);
    }

    private static void printPath(final List<Node> visited)
    {
        String path = visited.stream()
            .map(node -> Integer.toString(node.getId()))
            .collect(Collectors.joining(" -> "));
        System.out.println(path);
    }

    private static void printEdges(final double[][] graphDistances)
    {
        for(int i = 0; i < graphDistances.length; ++i)
        {
            for(int j = 0; j < graphDistances[i].length; ++j)
            {
                System.out.println(i + " - " + j + " -> " + graphDistances[i][j]);
                System.out.println();
            }
        }
    }

}
