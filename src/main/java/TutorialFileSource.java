import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TutorialFileSource  implements ViewerListener  {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected boolean loop = true;

    public TutorialFileSource() {
        System.setProperty("org.graphstream.ui", "swing");
        String filePath = "LeHavre.dgs";
        Graph g = new DefaultGraph("g");
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            g.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        FileSource fs = null;
        try {
            fs = FileSourceFactory.sourceFor(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fs.addSink(g);

        try {
            fs.begin(filePath);

            while (fs.nextEvents()) {
                // Optionally some code here ...
            }
        } catch( IOException e) {
            e.printStackTrace();
        }
        g.edges().forEach(edge -> {
            if(edge.hasAttribute("isTollway")) {
                edge.setAttribute("ui.class", "tollway");
            } else if(edge.hasAttribute("isTunnel")) {
                edge.setAttribute("ui.class", "tunnel");
            } else if(edge.hasAttribute("isBridge")) {
                edge.setAttribute("ui.class", "bridge");
            }
            float speedMax = (float)(edge.getNumber("speedMax") / 130.0);
            edge.setAttribute("ui.color", speedMax);
        });
        try {
            fs.end();
        } catch( IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }

        Viewer viewer = g.display(false);
        ViewPanel view = (ViewPanel) viewer.getDefaultView();

        view.enableMouseOptions();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(g);
        while (loop) {
            try {
                fromViewer.pump();
            } catch (Exception e) {
                logger.error("here: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void viewClosed(String viewName) {
        logger.debug("Closing");
    }

    @Override
    public void buttonPushed(String id) {
        logger.debug("Button pressed");
    }

    @Override
    public void buttonReleased(String id) {
        logger.debug("Button released");
    }

    @Override
    public void mouseOver(String id) {
        logger.debug("Mouse over");
    }

    @Override
    public void mouseLeft(String id) {
        logger.debug("Mouse left");
    }

    public static void main(String[] args) {
        new TutorialFileSource();
    }
}