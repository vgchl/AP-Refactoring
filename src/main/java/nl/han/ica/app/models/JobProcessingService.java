package nl.han.ica.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.han.ica.core.Job;

public class JobProcessingService extends Service {

    private ObjectProperty<Job> jobProperty;

    public JobProcessingService(Job job) {
        jobProperty = new SimpleObjectProperty<>(job);
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                jobProperty.get().process();
                return null;
            }

        };
    }

    public ObjectProperty getJobProperty() {
        return jobProperty;
    }

    public void setJob(Job job) {
        jobProperty.set(job);
    }

    public Job getJob() {
        return jobProperty.get();
    }

}
