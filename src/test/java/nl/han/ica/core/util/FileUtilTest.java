package nl.han.ica.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

public class FileUtilTest {

    private File directory;

    @Before
    public void setUp() throws URISyntaxException {
        directory = new File(getClass().getResource("/util/file_util/data").toURI());
    }

    @Test
    public void listsFilesRecursively() {
        List<File> foundFiles = FileUtil.listFilesRecursively(directory);
        Assert.assertEquals(foundFiles.size(), 3);
    }

    @Test
    public void filtersByExtension() {
        List<File> foundFiles;

        foundFiles = FileUtil.listFilesRecursively(directory, ".txt");
        Assert.assertEquals(foundFiles.size(), 2);

        foundFiles = FileUtil.listFilesRecursively(directory, ".md");
        Assert.assertEquals(foundFiles.size(), 1);
    }

    @Test
    public void isUninstantiable() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Assert.assertEquals("There must be only one constructor.", 1, FileUtil.class.getDeclaredConstructors().length);
        Constructor constructor = FileUtil.class.getDeclaredConstructor();
        Assert.assertFalse("Constructor must not be accessible.", constructor.isAccessible());

        // Call the constructor body for coverage.
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
