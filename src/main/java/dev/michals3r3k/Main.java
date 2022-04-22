package dev.michals3r3k;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Graph graph1 = new GraphInstanceGenerator(12, 1000).generate();
        GraphFileWriter fw = new GraphFileWriter("test.txt");
        fw.write(graph1);

        GraphReader graphReader = new GraphReader("test.txt");
        Graph graph = graphReader.getGraph();
        printNodes(graph);
        printEdges(graphReader.getGraphDistanceArray());

        List<Node> visited = getGreedyTspRoute(graph);

        printPath(visited);
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
