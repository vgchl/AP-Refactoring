package nl.han.ica.core;

import nl.han.ica.core.strategies.solvers.StrategySolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author: Wouter Konecny
 * @created: 24-10-12
 */
public class SolutionTest {

    private Solution solution = null;

    @Mock
    private StrategySolver strategySolver;

    private String beforeState = "before";
    private String afterState = "after";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        solution = new Solution(strategySolver);
    }

    @Test
    public void testSetAndGetStrategySolver() throws Exception {
        solution.setStrategySolver(strategySolver);
        assertSame(strategySolver, solution.getStrategySolver());
    }

    @Test
    public void testSetAndGetBefore() throws Exception {
        solution.setBefore(beforeState);
        assertEquals(beforeState, solution.getBefore());
    }

    @Test
    public void testSetAndGetAfter() throws Exception {
        solution.setAfter(afterState);
        assertEquals(afterState, solution.getAfter());
    }
}
