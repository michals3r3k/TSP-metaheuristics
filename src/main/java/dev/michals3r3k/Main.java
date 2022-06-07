package dev.michals3r3k;

import dev.michals3r3k.antalgorithm.AntAlgorithm;
import dev.michals3r3k.antalgorithm.AntAlgorithmResult;
import dev.michals3r3k.antalgorithm.AntEdge;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;
import dev.michals3r3k.graph.service.GraphReader;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        List<String> filenames = Arrays.asList("berlin52.txt", "bier127.txt",
            "tsp250.txt", "tsp500.txt", "tsp1000.txt");
        for(String filename : filenames)
        {
            long start = System.currentTimeMillis();
            Graph graph = new GraphReader(filename).getGraph();
            System.out.println(filename);
            AntAlgorithm antAlgorithm = new AntAlgorithm(
                graph,
                1000,
                0.1,
                5,
                0.9,
                0.9);
            AntAlgorithmResult result = antAlgorithm.runAlgorithm();
            List<Node> route = getRoute(result.getEdges());
            String routeStr = route.stream().map(Node::getId).map(
                    Objects::toString).collect(
                    Collectors.joining("->", "\n", ""));
                System.out.println(routeStr);
                System.out.println("Distance: " + result.getDistance() + "\n");

            long end = System.currentTimeMillis();
            double minuty = Math.floor(((end-start) / 1000.0)/60);
            double sekundy = ((end-start) / 1000) % 60;
            System.out.println(String.format("Time: %s min, %s sec", minuty, sekundy));
        }

    }

    private static List<Node> getRoute(final List<AntEdge> edges)
    {
        if(edges.isEmpty())
        {
            return Collections.emptyList();
        }
        final AntEdge firstEdge = edges.iterator().next();
        final List<Node> nodes = edges.stream().map(AntEdge::getEndNode).collect(
            Collectors.toList());
        return Stream.concat(Stream.of(firstEdge.getStartNode()), nodes.stream())
            .collect(Collectors.toList());
    }

}
