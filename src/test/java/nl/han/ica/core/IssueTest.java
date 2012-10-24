package nl.han.ica.core;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.strategies.Strategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.internal.PowerMockitoCore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author: Wouter Konecny
 * @created: 24-10-12
 */
public class IssueTest {

    private Issue issue = null;

    @Mock
    private File file;
    @Mock
    private IRuleViolation ruleViolation;
    @Mock
    private Strategy strategy;
    @Mock
    private Strategy strategy2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        issue = new Issue(strategy);
    }

    @Test
    public void testGetStrategy() throws Exception {
        assertSame(strategy, issue.getStrategy());
    }

    @Test
    public void testSetStrategy() throws Exception {
        issue.setStrategy(strategy2);
        assertSame(strategy2, issue.getStrategy());
        assertNotSame(strategy, issue.getStrategy());
    }

    @Test
    public void testSetAndGetFile() throws Exception {
        issue.setFile(file);
        assertSame(file, issue.getFile());
    }

    @Test
    public void testSetAndGetRuleViolation() throws Exception {
        issue.setRuleViolation(ruleViolation);
        assertSame(ruleViolation, issue.getRuleViolation());
    }
}
