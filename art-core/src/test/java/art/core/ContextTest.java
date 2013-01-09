package art.core;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class ContextTest {

    private Context context;

    @Mock
    private SourceFile sourceFileA, sourceFileB;
    @Mock
    private CompilationUnit compilationUnitA, compilationUnitB;
    @Mock
    private ASTVisitor visitor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new Context();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitializesSourceFiles() {
        Assert.assertNotNull(context.getSourceFiles());
    }

    @Test
    public void testChecksSourceFilePresence() {
        Assert.assertFalse(context.hasSourceFiles());
        Assert.assertNotNull(sourceFileA);
        context.addSourceFile(sourceFileA);
        Assert.assertTrue(context.hasSourceFiles());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSourceFilesUnmodifiableAdd() {
        context.getSourceFiles().add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSourceFilesUnmodifiableRemove() {
        context.getSourceFiles().remove(null);
    }

    @Test
    public void testAddsAndRemovesSourceFiles() {
        Assert.assertEquals(0, context.getSourceFiles().size());
        context.addSourceFile(sourceFileA);
        Assert.assertEquals(1, context.getSourceFiles().size());
        context.removeSourceFile(sourceFileA);
        Assert.assertEquals(0, context.getSourceFiles().size());
    }

    @Test
    public void testClearsSourceFiles() {
        context.addSourceFile(sourceFileA);
        Assert.assertEquals(1, context.getSourceFiles().size());
        context.clear();
        Assert.assertEquals(0, context.getSourceFiles().size());
    }

    @Test
    @Ignore
    public void testVisitorsVisitAllSourceFiles() {
        when(sourceFileA.getCompilationUnit()).thenReturn(compilationUnitA);
        when(sourceFileB.getCompilationUnit()).thenReturn(compilationUnitB);

        context.addSourceFile(sourceFileA);
        context.addSourceFile(sourceFileB);
        context.accept(visitor);

        verify(compilationUnitA, times(1)).accept(visitor); // TODO: Can't test because ASTNode.accept(Visitor) is final.
        verify(compilationUnitB, times(1)).accept(visitor); // TODO: Can't test because ASTNode.accept(Visitor) is final.
    }



}
