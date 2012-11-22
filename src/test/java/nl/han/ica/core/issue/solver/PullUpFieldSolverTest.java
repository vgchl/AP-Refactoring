package nl.han.ica.core.issue.solver;

import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.solver.PullUpFieldSolver;
import nl.han.ica.core.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 7-11-12
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldSolverTest {

    private PullUpFieldSolver solver;

    @Before
    public void setUP() throws Exception {


        solver = new PullUpFieldSolver();
    }

    @Test
    public void testCanSolve()
    {

    }

    @Test
    public void testCanSolveFalse()
    {

    }

    @Test
    public void testInternalSolve()
    {

    }
}
