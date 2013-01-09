package nl.han.ica.core.solution;

import nl.han.ica.core.SourceFile;
import nl.han.ica.core.solution.Delta;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

public class DeltaTest {
    private Delta delta = null;

    @Mock
    private SourceFile sourceFile;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        delta = new Delta(sourceFile);
    }

    @Test
    public void testSetBefore() {
        delta.setBefore("Before");
        assertEquals("Before",delta.getBefore());
    }

    @Test
    public void testGetBefore() {
        assertNull("It actually was: " + delta.getBefore(), delta.getBefore());
    }

    @Test
    public void testSetAfter() {
        delta.setAfter("After");
        assertEquals("After", delta.getAfter());
    }

    @Test
    public void testGetAfter() {
        assertNull("It actually was: " + delta.getAfter(),delta.getAfter());
    }

    @Test
    public void testGetFile(){
        assertNull(delta.getFile());
    }

}
