/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.Context;
import nl.han.ica.core.issues.criteria.Criteria;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Corne
 */
public class IssueFinderTest {
    
    private IssueFinder issueFinder;
    
    @Mock
    private Context context;
    @Mock
    private List<Criteria> criterias;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        issueFinder = new IssueFinder(context);
    }
    

    /**
     * Test of setCriterias method, of class IssueFinder.
     */
    @Test
    public void testSetCriterias() {
        System.out.println("setCriterias");
        issueFinder.setCriterias(criterias);
        //no getter for criterias..
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of findIssues method, of class IssueFinder.
     */
    @Test
    public void testFindIssues() {
        System.out.println("findIssues");

        List expResult = new ArrayList<>();
        List result = issueFinder.findIssues();
        assertEquals(expResult, result);
    }
}
