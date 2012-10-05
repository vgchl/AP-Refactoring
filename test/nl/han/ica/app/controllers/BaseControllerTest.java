package nl.han.ica.app.controllers;

import javafx.scene.layout.BorderPane;
import nl.han.ica.app.controllers.BaseController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javafx.scene.Parent;

public class BaseControllerTest {

    private class TestController extends BaseController {

        public getFXMLLoader() {
            return fxmlLoader;
        }

    }

    private BaseController controller;

    @Before
    public void setUp() {
        controller = new TestController();
    }

    @Test(expected=IllegalArgumentException.class)
    public void requiresPathToViewResource() {
        controller.buildView(null);
    }

    @Test
    public void loadsViewFromViewResource() {
        BorderPane root = (BorderPane) controller.buildView("/views/test.fxml");
        Assert.assertSame("test_view", root.getId());
    }

    @Test
    public void setsSelfAsViewController() {
        Assert.assertEquals(controller, controller.getFXMLLoader().getController());
    }
}
