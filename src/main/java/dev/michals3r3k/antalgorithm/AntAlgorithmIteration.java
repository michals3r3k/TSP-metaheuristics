package dev.michals3r3k.antalgorithm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;
import dev.michals3r3k.utils.EdgeUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AntAlgorithmIteration
{
    private final ListMultimap<Node, AntEdge> edgeMap;
    private final int iterationNumber;
    private final double randomProperty;
    private final List<Ant> ants;

    public AntAlgorithmIteration(
        final Graph graph,
        final int antQuantity,
        final ListMultimap<Node, AntEdge> edgeMap,
        final int iterationNumber,
        double tau0,
        double randomProperty)
    {
        this.edgeMap = edgeMap;
        this.iterationNumber = iterationNumber;
        this.randomProperty = randomProperty;
        this.ants = IntStream.range(0, antQuantity)
            .mapToObj(x -> new Ant(graph, edgeMap, tau0))
            .collect(Collectors.toList());
    }

    boolean runIteration(AlgorithmTimer timer, AntAlgorithmResult result)
    {
        for(int i = 0; i < ants.size(); ++i)
        {
            Ant ant = ants.get(i);
            boolean finishedRoute = ant.run(
                iterationNumber,
                randomProperty,
                iterationNumber == 0 && i == 0,
                timer);
            if(!finishedRoute)
            {
                return false;
            }
            result.update(ant.getRoute());
        }
        return true;
    }

    void putBestResultWithOpt(AntAlgorithmResult result, AlgorithmTimer timer)
    {
        for(Ant ant : ants)
        {
            List<AntEdge> antRoute = ant.getRoute();
            if(result.isEmpty())
            {
                result.update(antRoute);
            }
            for(int i = 0; i < 10; i++)
            {
                Opt2Result opt2Result = getOpt2(result, timer);
                if(opt2Result.timeEnd)
                {
                    return;
                }
                if(!opt2Result.foundBetter)
                {
                    break;
                }
            }
        }
    }

    private Opt2Result getOpt2(AntAlgorithmResult algorithmResult, AlgorithmTimer timer)
    {
        List<Node> nodesOnRoad = EdgeUtils.getNodesOnRoad(algorithmResult.getBestRoute());
        for(int start = 0; start < nodesOnRoad.size() - 2; ++start)
        {
            for(int end = start + 1; end < nodesOnRoad.size() - 1 && end <= start + 10; ++end)
            {
                List<AntEdge> edges = getReversedFrom(nodesOnRoad, start, end);
                boolean foundBetter = algorithmResult.update(edges);
                boolean timeEnd = timer.isTimeEnd();
                if(timeEnd || foundBetter)
                {
                    return new Opt2Result(foundBetter, timeEnd);
                }
            }
        }
        return new Opt2Result(false, timer.isTimeEnd());
    }

    private class Opt2Result
    {
        private final boolean foundBetter;
        private final boolean timeEnd;

        public Opt2Result(boolean foundBetter, boolean timeFinished)
        {
            this.foundBetter = foundBetter;
            this.timeEnd = timeFinished;
        }
    }

    private Opt2Result getOpt2New(AntAlgorithmResult algorithmResult, AlgorithmTimer timer)
    {
        List<Node> nodesOnRoad = EdgeUtils.getNodesOnRoad(algorithmResult.getBestRoute());
        List<Node> nodes = new ArrayList<>(nodesOnRoad);
        for(int start = 0; start < nodesOnRoad.size() - 2; ++start)
        {
            for(int end = start + 1; end < nodesOnRoad.size() - 1; ++end)
            {
                Node first = nodes.get(start);
                Node second = nodes.get(end);
                nodes.set(end, first);
                nodes.set(start, second);
                boolean foundBetter = algorithmResult.update(getEdges(nodes));
                boolean timeEnd = timer.isTimeEnd();
                if(timeEnd || foundBetter)
                {
                    return new Opt2Result(foundBetter, timeEnd);
                }
            }
        }
        return new Opt2Result(false, timer.isTimeEnd());
    }

    private List<AntEdge> getReversedFrom(List<Node> nodesOnRoad, int start, int end)
    {
        ImmutableList.Builder<Node> builder = ImmutableList.builder();
        List<Node> nodesPart = new ArrayList<>(nodesOnRoad.subList(start, end));
        Collections.reverse(nodesPart);
        builder.addAll(new ArrayList<>(nodesOnRoad.subList(0, start)));
        builder.addAll(nodesPart);
        builder.addAll(new ArrayList<>(nodesOnRoad.subList(end, nodesOnRoad.size())));
        List<Node> build = builder.build();

        return getEdges(build);
    }

    private List<AntEdge> getEdges(List<Node> nodes)
    {
        List<AntEdge> collect = IntStream.range(0, nodes.size() - 1)
            .mapToObj(i -> getAntEdge(nodes, i))
            .collect(Collectors.toList());
        Node first = nodes.get(0);
        Node last = nodes.get(nodes.size() - 1);
        List<AntEdge> collect1 = Stream.concat(
                collect.stream(),
                Stream.of(getEdge(last, first)))
            .collect(Collectors.toList());
        return collect1;
    }

    private AntEdge getEdge(final Node staringNode, final Node endingNode)
    {
        return edgeMap.get(staringNode).stream()
            .filter(edge -> endingNode.equals(edge.getEndNode()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Cannot find edge from "
                + staringNode.getId() + "to" + endingNode.getId()));
    }

    private AntEdge getAntEdge(List<Node> better, int i)
    {
        Node starting = better.get(i);
        Node ending = better.get(i + 1);
        AntEdge edges = edgeMap.get(starting).stream()
            .filter(edge -> ending.equals(edge.getEndNode()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException("Error"));
        return edges;
    }

}
