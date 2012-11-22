package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IssueDetectionService {

    private Set<IssueDetector> detectors;

    public IssueDetectionService() {
        this.detectors = new HashSet<>();
    }

    public Set<Issue> detectIssues(Set<CompilationUnit> compilationUnits) {
        Set<Issue> issues = new HashSet<>();
        for (IssueDetector detector : detectors) {
            detector.setCompilationUnits(compilationUnits);
            issues.addAll(detector.detectIssues());
        }
        return issues;
    }

    public Set<IssueDetector> getDetectors() {
        return detectors;
    }

}
