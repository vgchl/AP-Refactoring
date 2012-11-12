package nl.han.ica.core;

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

}
