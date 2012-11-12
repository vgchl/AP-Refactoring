package nl.han.ica.app.controllers;

import javafx.concurrent.Worker;
import javafx.scene.Parent;

import java.io.IOException;

public class ProgressController extends BaseController {

    private Worker worker;

    public ProgressController(Worker worker) {
        this.worker = worker;
    }


    @Override
    public Parent getView() {
        try {
            return buildView("/views/progress_popup.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

}
