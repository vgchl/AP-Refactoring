package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.IssueDetector;

public class EncapsulateCollectionDetector extends IssueDetector {

    private static final String STRATEGY_TITLE = "Encapsulate Collection";
    private static final String STRATEGY_DESCRIPTION = "Class contains a public collection. Make it private and provide collection accessors.";

    @Override
    public void detectIssues() {

    }

    @Override
    public String getTitle() {
        return STRATEGY_TITLE;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }

}
