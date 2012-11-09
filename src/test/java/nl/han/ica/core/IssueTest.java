package nl.han.ica.core;

import java.io.File;
import nl.han.ica.core.strategies.Strategy;
import org.eclipse.jdt.core.dom.ASTNode;


import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author: Wouter Konecny
 * @created: 24-10-12
 */
public class IssueTest {

    private Issue issue = null;

    @Mock
    private File file;
    @Mock
    private Strategy strategy;
    @Mock
    private Strategy strategy2;
    @Mock
    private ASTNode aSTNode;
    @Mock
    private SourceHolder sourceHolder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        issue = new Issue(strategy, aSTNode, sourceHolder);
    }

    @Test
    public void testGetStrategy() throws Exception {
        assertSame(strategy, issue.getStrategy());
    }
    
    @Test
    public void testGetSourceHolder() throws Exception {
        assertSame(sourceHolder, issue.getSourceHolder());
    }
    
    @Test
    public void testGetViolationNode() throws Exception {
        assertSame(aSTNode, issue.getViolationNode());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetDescription() throws NullPointerException {
        assertSame(file.getName(), issue.getDescription());
    }


}
