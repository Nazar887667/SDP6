package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StatisticsVisitorTest {

    @Test
    void T5_countsAndMaxDepthForNestedTree() {
        Statistics stats = new StatisticsVisitor().collect(Main.buildDemoTree());

        assertEquals(new Statistics(6, 5, 3), stats);
    }

    @Test
    void T5_depthIsRelativeToTheVisitedRoot() {
        DirectoryNode src = (DirectoryNode) Main.buildDemoTree().children().get(3);

        assertEquals(new Statistics(4, 2, 2), new StatisticsVisitor().collect(src));
    }

    @Test
    void T6_emptyDirectoryAndSingleFileRoots() {
        assertEquals(new Statistics(0, 1, 0), new StatisticsVisitor().collect(new DirectoryNode("e")));
        assertEquals(new Statistics(1, 0, 0), new StatisticsVisitor().collect(new FileNode("f", 5)));
    }

    @Test
    void T7_repeatedRunsDoNotLeakCountersOrDepth() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        DirectoryNode deep = Main.buildDemoTree();

        Statistics first = visitor.collect(deep);
        Statistics shallow = visitor.collect(new DirectoryNode("e"));
        Statistics again = visitor.collect(deep);

        assertEquals(new Statistics(0, 1, 0), shallow);
        assertEquals(first, again);
    }
}
