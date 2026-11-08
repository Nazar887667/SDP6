package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class TreeIntegrityTest {

    @Test
    void T8_negativeSizeIsRejectedButZeroIsAllowed() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad", -1));
        assertEquals(0, new FileNode("zero", 0).getSizeInBytes());
    }

    @Test
    void T8_invalidNamesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(" "));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("a/b"));
    }

    @Test
    void T8_duplicateSiblingNamesAreRejectedAcrossElementTypes() {
        DirectoryNode dir = new DirectoryNode("d");
        dir.add(new FileNode("same", 1));

        assertThrows(IllegalArgumentException.class, () -> dir.add(new FileNode("same", 2)));
        assertThrows(IllegalArgumentException.class, () -> dir.add(new DirectoryNode("same")));
        assertEquals(1, dir.children().size());
    }

    @Test
    void T8_sameNameInDifferentDirectoriesIsAllowed() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        a.add(new FileNode("x", 1));
        b.add(new FileNode("x", 1));

        assertEquals(1, a.children().size());
        assertEquals(1, b.children().size());
    }

    @Test
    void T8_multipleParentOwnershipIsRejected() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("f", 1);
        first.add(file);

        assertThrows(IllegalStateException.class, () -> second.add(file));
        assertEquals(0, second.children().size());
        assertSame(first, file.getParent());
    }

    @Test
    void T8_cyclesAreRejected() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        DirectoryNode c = new DirectoryNode("c");
        a.add(b);
        b.add(c);

        assertThrows(IllegalArgumentException.class, () -> a.add(a));
        assertThrows(IllegalArgumentException.class, () -> c.add(a));
        a.remove("b");
        assertThrows(IllegalArgumentException.class, () -> c.add(b));
        assertEquals(0, c.children().size());
    }

    @Test
    void T8_removedChildCanBeReattachedElsewhere() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        FileNode file = new FileNode("f", 7);
        a.add(file);

        assertSame(file, a.remove("f"));
        assertNull(file.getParent());
        b.add(file);

        assertEquals(0, a.children().size());
        assertEquals(List.of(file), b.children());
    }

    @Test
    void T8_validDeepNestingAndEmptyDirectoriesAreSupported() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode current = root;
        for (int level = 0; level < 50; level++) {
            DirectoryNode next = new DirectoryNode("d" + level);
            current.add(next);
            current = next;
        }
        current.add(new FileNode("leaf", 9));

        assertEquals(9, new TotalSizeVisitor().totalSizeOf(root));
        assertEquals(new Statistics(1, 51, 51), new StatisticsVisitor().collect(root));
    }

    @Test
    void childrenViewCannotBeUsedToModifyTheDirectory() {
        DirectoryNode dir = new DirectoryNode("d");
        dir.add(new FileNode("f", 1));

        assertThrows(UnsupportedOperationException.class, () -> dir.children().add(new FileNode("g", 1)));
        assertThrows(UnsupportedOperationException.class, () -> dir.children().clear());
        assertEquals(1, dir.children().size());
    }
}
