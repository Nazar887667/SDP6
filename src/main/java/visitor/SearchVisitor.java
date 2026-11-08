package visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class SearchVisitor implements Visitor {

    private final Predicate<String> nameCriterion;
    private final long minimumSizeInBytes;
    private Element root;
    private final List<String> matches = new ArrayList<>();

    public SearchVisitor(Predicate<String> nameCriterion, long minimumSizeInBytes) {
        if (nameCriterion == null) {
            throw new IllegalArgumentException("Name criterion must not be null");
        }
        if (minimumSizeInBytes < 0) {
            throw new IllegalArgumentException("Minimum size must not be negative");
        }
        this.nameCriterion = nameCriterion;
        this.minimumSizeInBytes = minimumSizeInBytes;
    }

    public static Predicate<String> nameContains(String text) {
        return name -> name.contains(text);
    }

    public static Predicate<String> nameEndsWith(String suffix) {
        return name -> name.endsWith(suffix);
    }

    public List<String> search(Element searchRoot) {
        root = searchRoot;
        matches.clear();
        TreeWalker.walk(searchRoot, this);
        List<String> result = new ArrayList<>(matches);
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    @Override
    public void visit(FileNode file) {
        if (nameCriterion.test(file.getName()) && file.getSizeInBytes() >= minimumSizeInBytes) {
            matches.add(NodePaths.relativePath(root, file));
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
    }
}
