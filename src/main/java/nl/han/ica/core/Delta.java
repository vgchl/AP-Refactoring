package nl.han.ica.core;

import org.eclipse.jdt.core.dom.ASTNode;

public class Delta {

    private ASTNode before;
    private ASTNode after;

    public Delta(ASTNode before, ASTNode after) {
        this.before = before;
        this.after = after;
    }

    public ASTNode getBefore() {
        return before;
    }

    public ASTNode getAfter() {
        return after;
    }
}
