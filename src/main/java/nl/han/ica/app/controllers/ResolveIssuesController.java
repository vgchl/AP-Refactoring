package nl.han.ica.app.controllers;

import javafx.scene.Parent;
import nl.han.ica.core.Job;

import java.io.IOException;

public class ResolveIssuesController extends BaseController {

    private Job job;

    public ResolveIssuesController(Job job) {
        this.job = job;
        job.process();
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/resolve_issues.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}