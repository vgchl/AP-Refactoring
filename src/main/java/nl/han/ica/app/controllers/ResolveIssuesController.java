package nl.han.ica.app.controllers;

import javafx.scene.Parent;
import nl.han.ica.core.Job;

public class ResolveIssuesController extends BaseController {

    public ResolveIssuesController(Job job)
    {

    }

    @Override
    public Parent getView() {
        return buildView("/views/resolve_issues.fxml");
    }

}