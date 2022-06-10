package dev.michals3r3k.graph;

import com.google.common.base.Functions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that represents graph.
 */
public class Graph
{
    private final Map<Integer, List<Node>> neighboursMap;
    private ListMultimap<Node, Edge> edgesMap;
    private final List<Node> nodes;
    private final int nodeQuantity;

    public Graph(final int nodeQuantity, final List<Node> nodes)
    {
        this.nodes = nodes;
        this.nodeQuantity = nodeQuantity;
        this.neighboursMap = new HashMap<>();
        initNeighboursMap(nodes);
        this.edgesMap = getEdgesMap(nodes);
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public int getNodeQuantity()
    {
        return nodeQuantity;
    }

    public Optional<Edge> getEdge(Node startNode, Node endNode)
    {
        return getEdges(startNode).stream().filter(edge -> endNode.equals(edge.getEndNode())).findFirst();
    }

    public List<Edge> getEdges(Node node)
    {
        return edgesMap.get(node);
    }

    public Collection<Edge> getEdges()
    {
        return edgesMap.values();
    }


    private List<Node> getNeighboursList(Node node)
    {
        return nodes.stream().filter(Predicate.not(node::equals)).sorted(
            Comparator.comparing(node::getDistance)).collect(
            Collectors.toList());
    }

    /**
     * Method returns all neighbours of given node.
     *
     * @param node node
     * @return all neighbours of given node
     */
    public List<Node> getNeighbours(Node node)
    {
        return neighboursMap.get(node.getId());
    }

    private ListMultimap<Node, Edge> getEdgesMap(final List<Node> nodes)
    {
        return nodes.stream().flatMap(this::getEdgeStream)
            .collect(Multimaps.toMultimap(
                Edge::getStartNode,
                Functions.identity(),
                ArrayListMultimap::create));
    }

    private void initNeighboursMap(final List<Node> nodes)
    {
        nodes.forEach(node -> neighboursMap.put(node.getId(), getNeighboursList(node)));
    }

    private Stream<Edge> getEdgeStream(final Node node)
    {
        return neighboursMap.get(node.getId()).stream().map(
            neighbour -> new Edge(node, neighbour));
    }

    public static class Edge
    {
        private final Node startNode;
        private final Node endNode;
        private final double distance;

        public Edge(Node startNode, Node endNode)
        {
            this.startNode = startNode;
            this.endNode = endNode;
            this.distance = startNode.getDistance(endNode);
        }

        public Node getStartNode()
        {
            return startNode;
        }

        public Node getEndNode()
        {
            return endNode;
        }

        public double getDistance()
        {
            return distance;
        }

    }

}
