package dev.michals3r3k;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GraphInstanceGenerator
{
    private static final Random ran = new Random();

    private final int quantity;
    private final int limit;

    public GraphInstanceGenerator(int quantity, int limit)
    {
        this.quantity = quantity;
        this.limit = limit;
    }

    public Graph generate()
    {
        return new Graph(quantity, getGeneratedNodes());
    }

    private List<Node> getGeneratedNodes()
    {
        final List<Node> nodes = new ArrayList<>();
        IntStream.range(1, quantity)
            .mapToObj(Integer::valueOf)
            .map(id -> getGeneratedNode(id, nodes))
            .forEach(nodes::add);
        return nodes;
    }

    private Node getGeneratedNode(final Integer id, final List<Node> nodes)
    {
        while(true)
        {
            Node node = generateNode(id);
            if(!nodes.contains(node))
            {
                return node;
            }
        }
    }

    private Node generateNode(final int id)
    {
        return new Node(id, ran.nextInt(this.limit), ran.nextInt(this.limit));
    }

}
