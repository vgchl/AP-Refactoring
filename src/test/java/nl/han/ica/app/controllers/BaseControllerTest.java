package nl.han.ica.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BaseControllerTest {

    private class TestController extends BaseController {

        public FXMLLoader getFXMLLoader() {
            return fxmlLoader;
        }


        @Override
        public Parent getView() {
            try {
                return buildView("/views/test_view.fxml");
            } catch (IOException e) {
                return null;
            }
        }
    }

    private TestController controller;

    @Before
    public void setUp() {
        controller = new TestController();
    }

    @Test(expected = IllegalArgumentException.class)
    public void requiresPathToViewResource() throws IOException {
        controller.buildView(null);
    }

    @Test
    public void loadsViewFromViewResource() {
        BorderPane root = (BorderPane) controller.getView();
        Assert.assertEquals("test_view", root.getId());
    }

    @Test
    public void setsSelfAsViewController() {
        Assert.assertEquals(controller, controller.getFXMLLoader().getController());
    }
}
