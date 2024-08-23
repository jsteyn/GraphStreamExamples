package com.jannetta.tutorials.graphstream;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class GraphStreamTest implements ViewerListener {
    protected boolean loop = true;
    Graph graph = new SingleGraph("Clicks");
    Node first = null;
    Node second = null;

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing");
        new GraphStreamTest();
    }

    public GraphStreamTest() {
        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        graph.addNode("a").setAttribute("ui.label", "a");
        graph.addNode("b").setAttribute("ui.label", "b");
        graph.addNode("c").setAttribute("ui.label", "c");
        graph.addNode("d").setAttribute("ui.label", "d");
        graph.addNode("e").setAttribute("ui.label", "e");
        graph.addNode("f").setAttribute("ui.label", "f");
        graph.getNode("a").setAttribute("xyz", 294191.255, 378471.609, 0);
        graph.getNode("b").setAttribute("xyz", 294173.568, 378465.056, 0);

        graph.addEdge("1", "a", "b", true)
                .setAttribute("ui.label", 1);


        graph.addEdge("2", "a", "d", true)
                .setAttribute("ui.label", 20);

        graph.addEdge("3", "d", "f", true)
                .setAttribute("ui.label", 1);

        graph.addEdge("4", "b", "c", true)
                .setAttribute("weight", 4);
        graph.addEdge("5", "c", "d", true)
                .setAttribute("weight", 4);
        graph.addEdge("6", "c", "f", true)
                .setAttribute("weight", 4);
        graph.addEdge("7", "e", "f", true)
                .setAttribute("weight", 4);

        graph.addEdge("8", "b", "a", true)
                .setAttribute("weight", 1);




        Viewer viewer = graph.display();
        viewer.getDefaultView().enableMouseOptions();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener( this);
        fromViewer.addSink(graph);

        while (loop) {
            fromViewer.pump(); // or fromViewer.blockingPump(); in the nightly builds

        }
    }

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        System.out.println("Button pushed on node " + id);
        System.out.println(Arrays.toString(graph.getNode(id).getArray(id)));
        if (first == null) {
            if (second != null) {
                second.removeAttribute("ui.class");
                second = null;
            }
            System.out.println("Set first");
            first = graph.getNode(id);
            graph.getNode(id).setAttribute("ui.class", "marked");
        } else {
            if (second == null) {
                System.out.println("Set second");
                second = graph.getNode(id);
                graph.getNode(id).setAttribute("ui.class", "marked");
                AStar aStar = new AStar(graph);
                aStar.compute(first.getId(), second.getId());
                Path shortestPath = aStar.getShortestPath();
                System.out.println("aStar.getShortestPath() = " + shortestPath);
                if (shortestPath != null)
                shortestPath.getEdgePath().forEach(p->{
                    p.setAttribute("ui.class", "marked");
                    System.out.println("make red");
                });
                first.removeAttribute("ui.class");
                first = null;
            }
        }
    }

    public void buttonReleased(String id) {
    }

    public void mouseOver(String id) {
    }

    public void mouseLeft(String id) {
    }
}

