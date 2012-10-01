package nl.han.ica.core.strategies;

import org.eclipse.jdt.core.dom.AST;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public interface IStrategy {

    public void rewriteAST(AST ast);

}
