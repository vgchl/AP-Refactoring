package nl.han.ica.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FileUtilTest {

    private File directory;
    private File file;
    private List<File> files = new ArrayList<>();

    @Before
    public void setUp() throws URISyntaxException {
        directory = new File(getClass().getResource("/util/file_util/data").toURI());
        file = new File(getClass().getResource("/util/file_util/data/file_1.txt").toURI());
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

    @Test(expected = IllegalArgumentException.class)
    public void haltOnNullDirectory() {
        FileUtil.listFilesRecursively(null);
    }

    @Test
    public void testGetFileContent() throws IOException {
        Assert.assertEquals("CONTENT\n", FileUtil.getFileContent(file));
    }

    @Test
    public void testSetFileContent() throws IOException {
        FileUtil.setFileContent(file, "EXTRA");
        Assert.assertEquals("EXTRA\n", FileUtil.getFileContent(file));
    }

    @Test
    public void testGetFilePaths() {
        files.add(file);
        String[] str = new String[files.size()];
        str[0] = file.toString();
        Assert.assertArrayEquals(str, FileUtil.getFilePaths(files));
    }

    @Test
    public void testGetFolderPaths() {
        files.add(file);
        String[] str = new String[files.size()];
        str[0] = files.get(0).getParentFile().getPath();
        Assert.assertArrayEquals(str, FileUtil.getFolderPaths(files));
    }

}
