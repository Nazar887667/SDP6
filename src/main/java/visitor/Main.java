package visitor;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        DirectoryNode root = buildDemoTree();

        System.out.println("Total size: " + new TotalSizeVisitor().totalSizeOf(root) + " bytes");

        SearchVisitor search = new SearchVisitor(SearchVisitor.nameEndsWith(".java"), 1000);
        System.out.println("Search (*.java, size >= 1000):");
        search.search(root).forEach(path -> System.out.println("  " + path));

        Statistics stats = new StatisticsVisitor().collect(root);
        System.out.println("Statistics: files=" + stats.fileCount()
                + ", directories=" + stats.directoryCount()
                + ", maxDepth=" + stats.maxDepth());
    }

    static DirectoryNode buildDemoTree() {
        DirectoryNode root = new DirectoryNode("project");
        DirectoryNode src = new DirectoryNode("src");
        DirectoryNode model = new DirectoryNode("model");
        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode empty = new DirectoryNode("empty");

        model.add(new FileNode("User.java", 2400));
        model.add(new FileNode("Order.java", 900));
        src.add(model);
        src.add(new FileNode("App.java", 1800));
        src.add(new FileNode("notes.txt", 1500));
        docs.add(new FileNode("report.pdf", 52000));
        root.add(src);
        root.add(docs);
        root.add(empty);
        root.add(new FileNode("pom.xml", 700));
        return root;
    }
}
