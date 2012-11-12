package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.CompilationUnit;

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
            detector.reset();
            for (CompilationUnit compilationUnit : compilationUnits) {
                compilationUnit.accept(detector);
            }
            issues.addAll(detector.getIssues());
        }
        System.out.println("RETURN ISSUES");
        return issues;
    }

    public Set<IssueDetector> getDetectors() {
        return detectors;
    }

}
