package nl.han.ica.core.ast;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 5-10-12
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class ASTStrategyHelper  {
    
    public static void insertMember(TypeDeclaration type, BodyDeclaration decl, int insertIndex) {
        List<BodyDeclaration> members = type.getMembers();
        if (members == null) {
            members = new ArrayList<>();
            type.setMembers(members);
        }
        members.add(insertIndex, decl);
    }
}
