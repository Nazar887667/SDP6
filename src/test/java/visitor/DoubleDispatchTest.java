package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DoubleDispatchTest {

    private static final class RecordingVisitor implements Visitor {
        final List<String> calls = new ArrayList<>();

        @Override
        public void visit(FileNode file) {
            calls.add("file:" + file.getName());
        }

        @Override
        public void visit(DirectoryNode directory) {
            calls.add("dir:" + directory.getName());
        }
    }

    @Test
    void T1_eachElementTypeReachesItsOwnVisitOverload() {
        RecordingVisitor visitor = new RecordingVisitor();
        new FileNode("a.txt", 1).accept(visitor);
        new DirectoryNode("d").accept(visitor);

        assertEquals(List.of("file:a.txt", "dir:d"), visitor.calls);
    }

    @Test
    void T1_dispatchWorksThroughTheElementInterfaceReference() {
        RecordingVisitor visitor = new RecordingVisitor();
        Element asFile = new FileNode("x", 0);
        Element asDirectory = new DirectoryNode("y");
        asDirectory.accept(visitor);
        asFile.accept(visitor);

        assertEquals(List.of("dir:y", "file:x"), visitor.calls);
    }

    @Test
    void walkerVisitsEveryNodeExactlyOnceInNameOrder() {
        RecordingVisitor visitor = new RecordingVisitor();
        TreeWalker.walk(Main.buildDemoTree(), visitor);

        assertEquals(List.of(
                "dir:project", "dir:docs", "file:report.pdf", "dir:empty", "file:pom.xml",
                "dir:src", "file:App.java", "dir:model", "file:Order.java", "file:User.java",
                "file:notes.txt"), visitor.calls);
    }
}
