package nl.han.ica.core.strategies;


import japa.parser.ast.CompilationUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceMagicNumber extends Strategy {

    private String replaceName = "MAGIC";

    @Override
    public void rewriteAST() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }
}
