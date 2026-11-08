package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TotalSizeVisitorTest {

    @Test
    void T2_sumsFilesAcrossSeveralNestingLevelsEachExactlyOnce() {
        DirectoryNode root = new DirectoryNode("r");
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        root.add(new FileNode("f1", 10));
        root.add(a);
        a.add(new FileNode("f2", 20));
        a.add(b);
        b.add(new FileNode("f3", 300));
        b.add(new FileNode("f4", 0));

        assertEquals(330, new TotalSizeVisitor().totalSizeOf(root));
        assertEquals(320, new TotalSizeVisitor().totalSizeOf(a));
    }

    @Test
    void T6_emptyDirectoryAndSingleFileRoots() {
        assertEquals(0, new TotalSizeVisitor().totalSizeOf(new DirectoryNode("empty")));
        assertEquals(42, new TotalSizeVisitor().totalSizeOf(new FileNode("only", 42)));
    }

    @Test
    void T7_repeatedRunsOnSameInstanceDoNotAccumulate() {
        DirectoryNode root = Main.buildDemoTree();
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        long first = visitor.totalSizeOf(root);
        long second = visitor.totalSizeOf(root);
        long other = visitor.totalSizeOf(new DirectoryNode("empty"));
        long third = visitor.totalSizeOf(root);

        assertEquals(59300, first);
        assertEquals(first, second);
        assertEquals(0, other);
        assertEquals(first, third);
    }
}
