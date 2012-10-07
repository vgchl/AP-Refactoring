package nl.han.ica.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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

        foundFiles = FileUtil.listFilesRecursively(directory, ".xml");
        Assert.assertEquals(foundFiles.size(), 1);
    }

}
