package dev.michals3r3k;

import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

public class GraphUtils
{
    public static void print(Graph graph)
    {
        for(Node node : graph.getNodes())
        {
            System.out.println(node.getId() + " " + node.getX() + " " + node.getY());
        }
    }

}
