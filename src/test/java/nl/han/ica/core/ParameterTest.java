package nl.han.ica.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ParameterTest {


    private Parameter.Constraint constraint;

    private Parameter parameter;
    private List<Parameter.Constraint> constraints = new ArrayList<>();

    @Before
    public void setUp() {
        this.parameter = new Parameter("Test", "1" );
    }

    @Test
    public void testGetTitle() {
        Assert.assertEquals("Test", parameter.getTitle());
    }

    @Test
    public void testSetTitle() {
        parameter.setTitle("Test2");
        Assert.assertEquals("Test2", parameter.getTitle());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("1", parameter.getValue());
    }

    @Test
    public void testSetValue() {
        parameter.setValue("2");
        Assert.assertEquals("2", parameter.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrongValue() {
        constraint = new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z]*(_[A-Z]+)*$");
            }
        };
        constraints.add(constraint);
        parameter.setConstraints(constraints);
        parameter.setValue("Illegal argument");
    }

    @Test
    public void testGetConstraints() {
        Assert.assertEquals(this.constraints, parameter.getConstraints());
    }

    @Test
    public void testSetConstraints() {
        parameter.setConstraints(this.constraints);
        Assert.assertNotNull(parameter.getConstraints());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Parameter<class java.lang.String:1>", parameter.toString());
    }

    @Test
    public void testIsValidValue() {
        constraint = new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        };
        constraints.add(constraint);
        parameter.setConstraints(constraints);

        Assert.assertTrue(constraint.isValid("ALLCAPITALS"));
        Assert.assertFalse(constraint.isValid("no capitals"));

        Assert.assertTrue(parameter.isValidValue("ALLCAPITALS"));
        Assert.assertFalse(parameter.isValidValue("nocapitals"));

    }

}
