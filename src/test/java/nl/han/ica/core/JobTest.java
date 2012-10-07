package nl.han.ica.core;

import org.junit.Before;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

public class JobTest {

    private Job job;

    @Before
    public void setUp() {
        job = new Job();
    }

    @Test
    public void hasReportAfterInstantiation() {
        Assert.that(null != job.getReport(), "Report present after instantiation.");
    }

}
