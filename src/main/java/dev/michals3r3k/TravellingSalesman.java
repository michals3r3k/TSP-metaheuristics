package dev.michals3r3k;

import com.google.common.collect.ImmutableList;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

import java.util.ArrayList;
import java.util.Collection;
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

    public List<Graph.Edge> getGreedyTspRoute()
    {
        Node startingNode = graph.getNodes().get(0);
        Node currentNode = startingNode;
        while(!isAllVisited())
        {
            visitedNodes.add(currentNode);
            currentNode = getNextNotVisitedNode(currentNode);
        }
        visitedNodes.add(startingNode);
        ImmutableList.Builder<Graph.Edge> builder = ImmutableList.builder();
        for(int i = 0; i < visitedNodes.size() - 1; i++)
        {
            Node node = visitedNodes.get(i);
            Node nextNode = visitedNodes.get(i + 1);
            Graph.Edge tsp_error = graph.getEdge(node, nextNode)
                .orElseThrow(() -> new IllegalStateException("TSP Error"));
            builder.add(tsp_error);
        }
        return builder.build();
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

}
