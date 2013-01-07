package nl.han.ica.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.han.ica.core.issue.Issue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class JobTest {

    private Job job;
    private ObservableList<Issue> issues = FXCollections.observableArrayList();
    private Set<SourceFile> sourceFiles = new HashSet<>();

    @Mock
    private Issue issue;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        job = new Job();
    }

    @Test
    public void testProcess() {
        job.process();
        Assert.assertTrue(job.getIssues().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveHasNoSuitableSolverAvailable() {
        job.createSolution(issue);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveWithParameters() {
        Map<String, Parameter> parameters = null;

        job.createSolution(issue, parameters);
    }

    @Test
    public void testCanProcess() {
        Assert.assertFalse(job.canProcess());
    }

    @Test
    public void testGetIssues() {
        Assert.assertEquals(issues, job.getIssues());
    }

    @Test
    public void testGetSourceFiles() {
        Assert.assertEquals(sourceFiles, job.getContext().getSourceFiles());
    }

    @Test
    public void testGetIssueDetectionService() {
        Assert.assertNotNull(job.getIssueDetectionService());
    }

    @Test
    public void testGetIssueSolvingService() {
        Assert.assertNotNull(job.getIssueSolvingService());
    }

    @Test
    public void testApplySolution() {
        //TODO FIX THIS TEST
    }

    @Test
    public void testIgnoreSolution() {
        //TODO: Access the issues allow manipulation.

    }
}
