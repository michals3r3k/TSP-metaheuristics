package dev.michals3r3k;

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
