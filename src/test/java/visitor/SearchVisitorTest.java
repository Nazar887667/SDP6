package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SearchVisitorTest {

    private final DirectoryNode root = Main.buildDemoTree();

    @Test
    void T3_bothConditionsMustHold() {
        assertEquals(List.of("src/App.java", "src/model/User.java"),
                new SearchVisitor(SearchVisitor.nameEndsWith(".java"), 1000).search(root));
        assertEquals(List.of("src/model/Order.java"),
                new SearchVisitor(SearchVisitor.nameContains("Order"), 0).search(root));
        assertTrue(new SearchVisitor(SearchVisitor.nameContains("Order"), 901).search(root).isEmpty());
    }

    @Test
    void T3_thresholdIsInclusive() {
        assertEquals(List.of("src/model/Order.java"),
                new SearchVisitor(SearchVisitor.nameContains("Order"), 900).search(root));
    }

    @Test
    void T4_pathsAreRootRelativeSlashSeparatedAndSorted() {
        List<String> paths = new SearchVisitor(name -> true, 0).search(root);

        assertEquals(List.of("docs/report.pdf", "pom.xml", "src/App.java",
                "src/model/Order.java", "src/model/User.java", "src/notes.txt"), paths);
    }

    @Test
    void T4_pathsAreRelativeToTheChosenRoot() {
        DirectoryNode src = (DirectoryNode) root.children().get(3);
        assertEquals("src", src.getName());

        assertEquals(List.of("App.java", "model/Order.java", "model/User.java", "notes.txt"),
                new SearchVisitor(name -> true, 0).search(src));
    }

    @Test
    void T6_noMatchAndEmptyStructures() {
        assertTrue(new SearchVisitor(SearchVisitor.nameContains("zzz"), 0).search(root).isEmpty());
        assertTrue(new SearchVisitor(name -> true, 0).search(new DirectoryNode("empty")).isEmpty());
        assertTrue(new SearchVisitor(name -> true, 1_000_000).search(root).isEmpty());
    }

    @Test
    void T7_sameInstanceGivesSameResultOnRepeat() {
        SearchVisitor visitor = new SearchVisitor(SearchVisitor.nameEndsWith(".java"), 0);

        List<String> first = visitor.search(root);
        List<String> second = visitor.search(root);

        assertEquals(3, first.size());
        assertEquals(first, second);
        assertTrue(visitor.search(new DirectoryNode("empty")).isEmpty());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(name -> true, -1));
    }
}
