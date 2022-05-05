package dev.michals3r3k;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TravellingSalesman
{
    private Graph graph;
    private List<Node> visitedNodes;

    public TravellingSalesman(final Graph graph)
    {
        this.graph = graph;
        this.visitedNodes = new ArrayList<>();
    }

    public List<Node> getGreedyTspRoute(Node stringNode)
    {
        this.visitedNodes = new ArrayList<>();
        Node currentNode = stringNode;
        while(!isAllVisited())
        {
            visitedNodes.add(currentNode);
            currentNode = getNextNotVisitedNode(currentNode);
        }
        return visitedNodes;
    }

    private boolean isAllVisited()
    {
        for(final Node node : graph.getNodes())
        {
            if(!visitedNodes.contains(node))
            {
                return false;
            }

        }
        return true;
    }

    private Node getNextNotVisitedNode(Node currentNode)
    {
        return graph.getNeighbours(currentNode).stream()
            .filter(Predicate.not(visitedNodes::contains))
            .findFirst()
            .orElse(null);
    }

    public double getRouteDistance()
    {
        double[][] distanceMatrix = graph.getDistanceMatrix();
        double distance = 0.0;
        for(int i = 0; i < visitedNodes.size() - 1; ++i)
        {
            final Node node = visitedNodes.get(i);
            final Node nextNode = visitedNodes.get(i + 1);
            distance += distanceMatrix[node.getIndex()][nextNode.getIndex()];
        }
        final Node endingNode = visitedNodes.get(visitedNodes.size() - 1);
        final Node startingNode = visitedNodes.get(0);
        distance += distanceMatrix[endingNode.getIndex()][startingNode.getIndex()];
        return distance;
    }

}
