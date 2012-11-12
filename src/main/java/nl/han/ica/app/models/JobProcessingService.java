package nl.han.ica.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.han.ica.core.Job;
import org.apache.log4j.Logger;

/**
 * Helps processing jobs on a background worker thread.
 *
 * @author Teun van Vegchel
 */
public class JobProcessingService extends Service {

    private ObjectProperty<Job> jobProperty;
    private Logger logger;

    /**
     * Initialize a new JobProcessingService for a certain job.
     *
     * @param job The job to process.
     */
    public JobProcessingService(Job job) {
        logger = Logger.getLogger(getClass());
        jobProperty = new SimpleObjectProperty<>(job);
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                try {
                    jobProperty.get().process();
                } catch (IllegalStateException e) {
                    logger.fatal("Threading exception", e);
                }
                return null;
            }

        };
    }

    @Override
    protected void failed() {
        logger.fatal("Task execution failed.", getException());
    }

    /**
     * Returns the job property.
     *
     * @return The job property.
     */
    public ObjectProperty getJobProperty() {
        return jobProperty;
    }

    /**
     * Sets the job that this service will process.
     *
     * @param job The job to process.
     */
    public void setJob(Job job) {
        jobProperty.set(job);
    }

    /**
     * Returns the job this service will process.
     *
     * @return The job to process.
     */
    public Job getJob() {
        return jobProperty.get();
    }

}
