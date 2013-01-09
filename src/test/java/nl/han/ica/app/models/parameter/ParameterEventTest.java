package nl.han.ica.app.models.parameter;

import nl.han.ica.core.solution.Parameter;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ParameterEventTest {

    ParameterEvent parameterEvent = null;

    @Before
    public void setUp() throws Exception {
        parameterEvent = new ParameterEvent();
    }

    @Test
    public void testConstructors() {
        parameterEvent = null;
        parameterEvent = new ParameterEvent();
        assertNotNull(parameterEvent);

        parameterEvent = null;
        parameterEvent = new ParameterEvent(new Parameter("", ""));
        assertNotNull(parameterEvent);
    }

    @Test
    public void testGetAndSetParameter() throws Exception {
        assertEquals(null, parameterEvent.getParameter());
        Parameter parameter = new Parameter("x", "y");
        parameterEvent.setParameter(parameter);
        assertEquals(parameter, parameterEvent.getParameter());
    }
}
