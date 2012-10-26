package nl.han.ica.core.strategies.solvers;

/**
 * Represents an external variable used by StrategySolvers to customize the Solution.
 */
public class Parameter {

    private String title;
    private Object value;

    /**
     * Instantiates a new Parameter with a title and a value.
     * @param title The parameter's title.
     * @param value The parameter's value.
     */
    public Parameter(final String title, final Object value) {
        this.title = title;
        this.value = value;
    }

    /**
     * Returns the parameter's title.
     * @return The parameter's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the parameter's title.
     * @param title The parameter's title.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the parameter's value.
     * @return The parameter's value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the parameter's value.
     * @param value The parameter's value.
     */
    public void setValue(final Object value) {
        this.value = value;
    }


    public String toString() {
        return "Parameter<" + value.getClass() +":" + value + ">";
    }

}
