package visitor;

public final class TotalSizeVisitor implements Visitor {

    private long total;

    public long totalSizeOf(Element root) {
        total = 0;
        TreeWalker.walk(root, this);
        return total;
    }

    @Override
    public void visit(FileNode file) {
        total = Math.addExact(total, file.getSizeInBytes());
    }

    @Override
    public void visit(DirectoryNode directory) {
    }
}
