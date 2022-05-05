package dev.michals3r3k;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class that represents graph.
 */
public class Graph
{
    private final Map<Integer, List<Node>> neigboursMap;
    private final List<Node> nodes;
    private final int nodeQuantity;
    private final double[][] distanceMatrix;

    public Graph(final int nodeQuantity, final List<Node> nodes)
    {
        this.nodes = nodes;
        this.nodeQuantity = nodeQuantity;
        this.neigboursMap = new HashMap<>();
        nodes.forEach(node -> neigboursMap.put(node.getId(), getNeighboursList(node)));
        this.distanceMatrix = initDistanceMatrix(nodeQuantity, nodes);
    }

    private static double[][] initDistanceMatrix(int nodeQuantity, List<Node> nodes)
    {
        double[][] newGraph = new double[nodeQuantity][nodeQuantity];
        for(Node startingNode : nodes)
        {
            for(Node endingNode : nodes)
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

    public List<Node> getNodes()
    {
        return nodes;
    }

    public int getNodeQuantity()
    {
        return nodeQuantity;
    }

    private List<Node> getNeighboursList(Node node)
    {
        return nodes.stream()
            .filter(Predicate.not(node::equals))
            .sorted(Comparator.comparing(node::getDistance))
            .collect(Collectors.toList());
    }

    /**
     * Method returns all neighbours of given node.
     *
     * @param node node
     * @return all neighbours of given node
     */
    public List<Node> getNeighbours(Node node)
    {
        return neigboursMap.get(node.getId());
    }

    public double[][] getDistanceMatrix()
    {
        return distanceMatrix;
    }

}
