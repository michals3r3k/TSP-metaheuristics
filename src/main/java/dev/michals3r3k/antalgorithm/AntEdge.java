package dev.michals3r3k.antalgorithm;

import dev.michals3r3k.graph.Graph;
import dev.michals3r3k.graph.Node;

public class AntEdge
{
    private final Graph.Edge edge;
    private double pheromone;
    private final double evaporation;

    public AntEdge(
        final Graph.Edge edge,
        final double pheromone,
        final double evaporation
        )
    {
        this.edge = edge;
        this.pheromone = pheromone;
        this.evaporation = evaporation;
    }

    public Node getStartNode()
    {
        return edge.getStartNode();
    }

    public Node getEndNode()
    {
        return edge.getEndNode();
    }

    public double getDistance()
    {
        return edge.getDistance();
    }

    Graph.Edge getEdge()
    {
        return edge;
    }

    public void evaporate()
    {
        this.pheromone *= (1 - this.evaporation);
    }

    public void setPheromone(double pheromone)
    {
        this.pheromone = pheromone;
    }

    public void spreadPheromone(final double distance, final double antPheromone)
    {
        this.pheromone += edge.getDistance() * (antPheromone / distance);
    }

    public double getAttraction()
    {
        return pheromone / edge.getDistance();
    }

    public void updatePheromone(double tau0)
    {
        evaporate();
        this.pheromone += evaporation * tau0;
    }

    public double getPheromone()
    {
        return pheromone;
    }

}
