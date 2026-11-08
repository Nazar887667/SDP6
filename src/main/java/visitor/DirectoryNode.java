package visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DirectoryNode extends Node {

    private final Map<String, Node> childrenByName = new TreeMap<>();

    public DirectoryNode(String name) {
        super(name);
    }


    public void add(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child must not be null");
        }
        if (child.parentDirectory() != null) {
            throw new IllegalStateException("Child already has a parent: " + child.getName());
        }
        rejectCycle(child);
        if (childrenByName.containsKey(child.getName())) {
            throw new IllegalArgumentException("Duplicate sibling name: " + child.getName());
        }
        childrenByName.put(child.getName(), child);
        child.attachTo(this);
    }

    public Node remove(String childName) {
        Node removed = childrenByName.remove(childName);
        if (removed != null) {
            removed.detach();
        }
        return removed;
    }

    private void rejectCycle(Node child) {
        for (DirectoryNode current = this; current != null; current = current.parentDirectory()) {
            if (current == child) {
                throw new IllegalArgumentException("Cycle: " + child.getName() + " is this directory or its ancestor");
            }
        }
    }

    @Override
    public List<Element> children() {
        return Collections.unmodifiableList(new ArrayList<>(childrenByName.values()));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
