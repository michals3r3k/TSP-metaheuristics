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
        int nodeQuantity = Integer.parseInt(scanner.nextLine().trim());
        List<Node> nodes = new ArrayList<>();
        while(scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            if(line.isBlank())
            {
                continue;
            }
            String[] node = line.split("\\s+");
            int number = Integer.parseInt(node[0].trim());
            int x = Integer.parseInt(node[1].trim());
            int y = Integer.parseInt(node[2].trim());
            nodes.add(new Node(number, x, y));
        }
        Collections.sort(nodes, Comparator.comparing(Node::getId));
        return new Graph(nodeQuantity, nodes);
    }

    GraphReader(String filename) throws FileNotFoundException
    {
        this.graph = readGraph(filename);
    }

    public Graph getGraph()
    {
        return graph;
    }

}
