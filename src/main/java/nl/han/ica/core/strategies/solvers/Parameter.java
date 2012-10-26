package nl.han.ica.core.strategies.solvers;

public class Parameter {

    private String title;
    private Object value;

    public Parameter(String title, Object value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return "Parameter<" + value.getClass() +":" + value + ">";
    }
}
