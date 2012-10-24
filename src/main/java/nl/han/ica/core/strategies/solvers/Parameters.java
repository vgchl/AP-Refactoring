package nl.han.ica.core.strategies.solvers;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.Map;
import java.util.Set;

/**
 * A holder for Parameters that are used in a solver.
 */
public class Parameters {

    static final long serialVersionUID = 42L;

    private ObservableMap<String, Object> properties;

    public Parameters() {
        properties = FXCollections.observableHashMap();
    }

    public Object get(String key) {
        return properties.get(key);
    }
    public ObservableMap<String, Object> getAll() {
        return properties;
    }

    public void put(String key, Object value) {
        properties.put(key, value);
    }

    public void merge(Parameters parameters) {
        properties.putAll(parameters.getAll());
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }

    public Object remove(Object key) {
        return properties.remove(key);
    }

    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    public void addListener(MapChangeListener<? super String, ? super Object> mapChangeListener) {
        properties.addListener(mapChangeListener);
    }

    @Override
    public String toString() {
        return properties.toString();
    }

}
