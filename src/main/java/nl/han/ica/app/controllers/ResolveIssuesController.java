package nl.han.ica.app.controllers;

import javafx.scene.Parent;

public class ResolveIssuesController extends BaseController {

    @Override
    public Parent getView() {
        return buildView("/views/resolve_issues.fxml");
    }

}