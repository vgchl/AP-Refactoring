package nl.han.ica.core;

public class Delta {

    private SourceFile sourceFile;
    private String before;
    private String after;

    public Delta(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }
}
