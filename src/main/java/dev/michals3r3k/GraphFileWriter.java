package dev.michals3r3k;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class GraphFileWriter
{
    private final String filename;

    public GraphFileWriter(final String filename)
    {
        this.filename = filename;
    }

    public void write(final Graph graph)
    {
        final List<Node> nodes = graph.getNodes();
        final File file = new File(filename);
        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        if(file.canWrite())
        {
            try(final FileWriter fileWriter = new FileWriter(file, false))
            {
                fileWriter.write(graph.getNodeQuantity() + "\n");
                for(final Node node : nodes)
                {
                    fileWriter.write(getLine(node));
                }
            } catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private String getLine(final Node node)
    {
        return node.getId() + " " + node.getX() + " " + node.getY() + "\n";
    }

    /**
     * Writer test.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        GraphInstanceGenerator graphGenerator =
            new GraphInstanceGenerator(10, 1000);
        Graph graph = graphGenerator.generate();
        GraphUtils.print(graph);
        GraphFileWriter writer = new GraphFileWriter("test.txt");
        writer.write(graph);
    }

}
