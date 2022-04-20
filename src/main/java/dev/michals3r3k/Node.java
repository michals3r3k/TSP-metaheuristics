package dev.michals3r3k;

import java.util.Objects;

public class Node
{
    private final int id;
    private final int x;
    private final int y;

    public Node(int id, int x, int y)
    {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId()
    {
        return id;
    }

    public int getIndex()
    {
        return getId() - 1;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getDistance(Node node)
    {
        if(this.equals(node))
        {
            throw new IllegalArgumentException("Returning is not allowed");
        }
        double xx = Math.pow(Math.abs(getX() - node.getX()), 2);
        double yy = Math.pow(Math.abs(getY() - node.getY()), 2);
        return Math.sqrt(xx + yy);
    }

    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

}
