package visitor;

public final class StatisticsVisitor implements Visitor {

    private Element root;
    private int fileCount;
    private int directoryCount;
    private int maxDepth;

    public Statistics collect(Element statisticsRoot) {
        root = statisticsRoot;
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
        TreeWalker.walk(statisticsRoot, this);
        return new Statistics(fileCount, directoryCount, maxDepth);
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        recordDepth(file);
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        recordDepth(directory);
    }

    private void recordDepth(Element node) {
        maxDepth = Math.max(maxDepth, NodePaths.depth(root, node));
    }
}
