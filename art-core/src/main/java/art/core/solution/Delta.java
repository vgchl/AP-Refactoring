package art.core.solution;

import difflib.DiffUtils;
import difflib.Patch;
import art.core.SourceFile;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the changes in a SourceFile.
 *
 * @author Teun van Vegchel
 */
public class Delta {

    private SourceFile sourceFile;
    private String before;
    private String after;

    /**
     * Instantiate a new Delta for a certain {@link SourceFile}.
     *
     * @param sourceFile The changed source file
     */
    public Delta(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Set the {@link SourceFile}'s before change state.
     *
     * @param before The SourceFile's before state
     */
    public void setBefore(String before) {
        this.before = before;
    }

    /**
     * Returns the {@link SourceFile}'s before change state.
     *
     * @return the {@link SourceFile}'s before change state
     */
    public String getBefore() {
        return before;
    }

    /**
     * Set the {@link SourceFile}'s after change state.
     *
     * @param after The SourceFile's after state.
     */
    public void setAfter(String after) {
        this.after = after;
    }

    /**
     * Returns the {@link SourceFile}'s after change state.
     *
     * @return the {@link SourceFile}'s after change state
     */
    public String getAfter() {
        return after;
    }

    /**
     * Get the file from this delta
     *
     * @return the file
     */
    public File getFile() {
        return sourceFile.getFile();
    }

    /**
     * Gets the difference between the before and after source.
     */
    public List<difflib.Delta> getDifferences() {
        Patch patch = DiffUtils.diff(contentToLines(before), contentToLines(after));
        return patch.getDeltas();
    }

    /**
     * Splits the content of a file into seperate lines.
     *
     * @param content The content to split.
     * @return a List of all lines in the content string.
     */
    private List<String> contentToLines(String content) {
        String[] lines = content.split(System.getProperty("line.separator"));
        List<String> linesAsList = new LinkedList<>();

        linesAsList.addAll(Arrays.asList(lines));

        return linesAsList;
    }

}
