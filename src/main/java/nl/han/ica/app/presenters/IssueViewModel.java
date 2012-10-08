package nl.han.ica.app.presenters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sourceforge.pmd.RuleViolation;

public class IssueViewModel {
    private RuleViolation ruleViolation;
    private StringProperty issueName;

    public void setIssueName(String value) {
        issueNameProperty().set(value);
    }

    public String getIssueName() {
        return issueNameProperty().get();
    }

    public StringProperty issueNameProperty() {
        if (issueName == null) issueName = new SimpleStringProperty(this, "issueName");
        return issueName;
    }

    public RuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(RuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }
}