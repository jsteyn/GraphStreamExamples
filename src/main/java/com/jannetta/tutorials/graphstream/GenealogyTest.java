package com.jannetta.tutorials.graphstream;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class GenealogyTest  implements ViewerListener{
    Graph graph = new SingleGraph("Clicks");
    boolean loop = true;

    GenealogyTest() {
        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        populateGraph(graph);
    }

    public void populateGraph(Graph graph) {
        graph.addNode("I0001").setAttribute("name", "Jannetta Steyn");
        graph.addNode("I0002").setAttribute("name", "Willie Steyn");
        graph.addNode("I0003").setAttribute("name", "Leonetta Henning");
        graph.addNode("I0004").setAttribute("name", "Leonetta Lewis");
        graph.addNode("I0005").setAttribute("name", "Stuart Lewis");
        graph.addEdge("F0001", "I0002", "I0001", true)
                .setAttribute("relation", "child of");
        graph.addEdge("F0002", "I0001", "I0003", true)
                .setAttribute("relation", "child of");
        graph.addEdge("F0003", "I0001", "I0004", true)
                .setAttribute("relation", "child of");

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

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing");
        new GenealogyTest();
    }

    @Override
    public void viewClosed(String s) {

    }

    @Override
    public void buttonPushed(String s) {

    }

    @Override
    public void buttonReleased(String s) {

    }

    @Override
    public void mouseOver(String s) {

    }

    @Override
    public void mouseLeft(String s) {

    }
}
