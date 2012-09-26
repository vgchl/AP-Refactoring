package nl.han.ica.app.views;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.han.ica.app.controllers.RefactorToolApp;

/**
 * Stage for the workspace.
 *
 * @author Joppe Catsburg
 * @since 27-11-2011
 * @version 1.0
 *
 */
public class WorkspaceStage extends Stage {
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    private static final String STAGE_NAME = "Refactor Tool 1.0";
    private static final boolean RESIZABLE = false;

    private Group root;
    private Scene scene;

    private VBox mainLayout;

    /**
     * Constructor for the workspace stage.
     *
     * Initializes the layout and adds the menubar, tableview and bottombar.
     *
     * @author
     * @since
     * @version 1.0
     */
    public WorkspaceStage() {
        setStageProperties();
        root = new Group();
        createAndSetScene();
        initLayout();
    }


    private void setStageProperties() {
        setTitle(STAGE_NAME);
        setResizable(RESIZABLE);
    }

    private void initLayout() {
        mainLayout = new VBox();
        mainLayout.setPrefSize(MIN_WIDTH, MIN_HEIGHT);

        root.getChildren().add(mainLayout);
    }

    private void createAndSetScene() {
        scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);
        setScene(scene);
    }

    /**
     * Sets the title of the workspace.
     *
     * @author
     * @param
     */
    public void setWorkspaceTitle(String title) {
        setTitle(STAGE_NAME + title);
    }

}
