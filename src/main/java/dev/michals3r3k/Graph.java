package dev.michals3r3k;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class that represents graph.
 */
public class Graph
{
    private final List<Node> nodes;
    private final int nodeQuantity;

    public Graph(int nodeQuantity, List<Node> nodes)
    {
        this.nodes = nodes;
        this.nodeQuantity = nodeQuantity;
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public int getNodeQuantity()
    {
        return nodeQuantity;
    }

    /**
     * Method returns all neighbours of given node.
     *
     * @param node node
     * @return all neighbours of given node
     */
    public List<Node> getNeighbours(Node node)
    {
        return nodes.stream()
            .filter(Predicate.not(node::equals))
            .sorted(Comparator.comparing(node::getDistance))
            .collect(Collectors.toList());
    }

}
