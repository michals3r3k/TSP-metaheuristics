package dev.michals3r3k;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GraphReader
{
    private final Graph graph;

    private static Graph readGraph(String filename) throws FileNotFoundException
    {
        final Graph graph;
        Scanner scanner = new Scanner(new FileReader(filename));
        int nodeQuantity = Integer.parseInt(scanner.nextLine());
        List<Node> nodes = new ArrayList<>();
        while(scanner.hasNextLine())
        {
            String[] node = scanner.nextLine().split(" ");
            int number = Integer.parseInt(node[0]);
            int x = Integer.parseInt(node[1]);
            int y = Integer.parseInt(node[2]);
            nodes.add(new Node(number, x, y));
        }
        Collections.sort(nodes, Comparator.comparing(Node::getId));
        return new Graph(nodeQuantity, nodes);
    }

    GraphReader() throws FileNotFoundException
    {
        this.graph = readGraph("dane.txt");
    }

    public Graph getGraph()
    {
        return graph;
    }

    public double[][] getGraphDistanceArray()
    {
        double[][] newGraph = new double[graph.getNodeQuantity()][graph.getNodeQuantity()];
        for(Node startingNode : graph.getNodes())
        {
            for(Node endingNode : graph.getNodes())
            {
                if(!startingNode.equals(endingNode))
                {
                    double distance = startingNode.getDistance(endingNode);
                    newGraph[startingNode.getIndex()][endingNode.getIndex()] = distance;
                }
            }
        }
        return newGraph;
    }

}
