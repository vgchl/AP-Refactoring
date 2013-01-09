package nl.han.ica.app.models.parameter;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import nl.han.ica.core.solution.Parameter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParameterControlFactoryTest {

    private ParameterControlFactory parameterControlFactory;

    @Before
    public void setUp() throws Exception {
        parameterControlFactory = new ParameterControlFactory();
    }

    @Test
    public void testControlForParameter() throws Exception {
        TextField textField = (TextField) parameterControlFactory.controlForParameter(new Parameter("Hi,", "expr"));
        assertEquals("expr", textField.getText());
    }

    @Test
    public void testControlForParameterWithEventHandler() throws Exception {
        TextField textField = (TextField) parameterControlFactory.controlForParameter(new Parameter("Hi,", "expr"), null);
        assertEquals("expr", textField.getText());
    }
}
