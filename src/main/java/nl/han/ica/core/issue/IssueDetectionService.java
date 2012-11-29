package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Provides issue detection functionality. Uses a set of {@link IssueDetector}s to find {@link Issue}s in a set of
 * {@link CompilationUnit}s.
 */
public class IssueDetectionService {

    private Set<IssueDetector> detectors;
	private Logger logger;
    
    /**
     * Instantiate a new IssueDetectionService.
     */
    public IssueDetectionService() {
        this.detectors = new HashSet<>();
        logger = Logger.getLogger(getClass().getName());
        logger.info("Created IssueDetectionService");
    }

    /**
     * Let all the {@link IssueDetector}s scan the {@link CompilationUnit}s and find the {@link Issue}s.
     *
     * @param compilationUnits The compilation units to scan though for issues.
     * @return The detected issues.
     */
    public Set<Issue> detectIssues(Set<CompilationUnit> compilationUnits) {
        logger.info("Detecting issues.....");
        Set<Issue> issues = new HashSet<>();
        for (IssueDetector detector : detectors) {
            detector.reset();
            detector.setCompilationUnits(compilationUnits);
            detector.detectIssues();
            issues.addAll(detector.getIssues());
        }
        logger.info("Done detecting issues");
        return issues;
    }

    /**
     * Returns the list of issue detectors.
     *
     * @return The list of issue detectors.
     */
    public Set<IssueDetector> getDetectors() {
        return Collections.unmodifiableSet(detectors);
    }

    /**
     * Add an issue detector to this service.
     *
     * @param issueDetector The issue detector to add.
     */
    public void addDetector(IssueDetector issueDetector) {
        detectors.add(issueDetector);
    }

    /**
     * Remove an issue detector from this service.
     *
     * @param issueDetector The issue detector to remove.
     */
    public void removeDetector(IssueDetector issueDetector) {
        detectors.remove(issueDetector);
    }

}
