package art.core.issue;

import art.core.Context;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides issue detection functionality. Uses a set of {@link IssueDetector}s to find {@link Issue}s in a set of
 * {@link org.eclipse.jdt.core.dom.CompilationUnit}s.
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
    }

    /**
     * Let all the {@link IssueDetector}s scan the {@link org.eclipse.jdt.core.dom.CompilationUnit}s and find the {@link Issue}s.
     *
     * @param context The context (and its compilation units) to scan trough for issues.
     * @return The detected issues.
     */
    public Set<Issue> detectIssues(Context context) {
        logger.info("Detecting issues...");
        Set<Issue> issues = new HashSet<>();
        for (IssueDetector detector : detectors) {
            issues.addAll(detector.detectIssues(context));
        }
        logger.debug("Done detecting issues");
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
