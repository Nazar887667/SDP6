package visitor;

import java.util.List;

public abstract class Node implements Element {

    private final String name;
    private DirectoryNode parent;

    protected Node(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        if (name.contains("/")) {
            throw new IllegalArgumentException("Name must not contain '/': " + name);
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Element getParent() {
        return parent;
    }

    DirectoryNode parentDirectory() {
        return parent;
    }

    void attachTo(DirectoryNode newParent) {
        this.parent = newParent;
    }

    void detach() {
        this.parent = null;
    }

    @Override
    public abstract List<Element> children();
}
