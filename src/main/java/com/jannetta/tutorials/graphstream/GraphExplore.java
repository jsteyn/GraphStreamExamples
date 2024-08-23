package com.jannetta.tutorials.graphstream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphExplore {
    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing");
        new GraphExplore();
    }

    public GraphExplore() {
        Graph graph = new SingleGraph("tutorial 1");
        graph.setAttribute("ui.stylesheet", styleSheet);

        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();

        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("DE", "D", "E");
        graph.addEdge("DF", "D", "F");
        graph.addEdge("EF", "E", "F");

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }

        explore(graph.getNode("A"));
    }

    public void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    protected void sleep() {
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    protected String styleSheet =
            "node {" +
                    "	fill-color: black;" +
                    "}" +
            "node.marked {" +
            "	fill-color: red;" +
            "}";
}