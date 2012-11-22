package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class IssueDetectionService {

    private Set<IssueDetector> detectors;
    private Logger logger;
    
    public IssueDetectionService() {
        this.detectors = new HashSet<>();
        logger = Logger.getLogger(getClass().getName());
        logger.info("Created IssueDetectionService");
    }

    public Set<Issue> detectIssues(Set<CompilationUnit> compilationUnits) {
        logger.info("Detecting issues.....");
        Set<Issue> issues = new HashSet<>();
        for (IssueDetector detector : detectors) {
            detector.setCompilationUnits(compilationUnits);
            issues.addAll(detector.detectIssues());
        }
        logger.info("Done detecting issues");
        return issues;
    }

    public Set<IssueDetector> getDetectors() {
        return detectors;
    }

}
