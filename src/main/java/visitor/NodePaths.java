package visitor;

import java.util.ArrayDeque;
import java.util.Deque;

final class NodePaths {

    private NodePaths() {
    }

    static int depth(Element root, Element node) {
        int depth = 0;
        for (Element current = node; current != root; current = current.getParent()) {
            depth++;
        }
        return depth;
    }

    static String relativePath(Element root, Element node) {
        if (node == root) {
            return node.getName();
        }
        Deque<String> names = new ArrayDeque<>();
        for (Element current = node; current != root; current = current.getParent()) {
            names.addFirst(current.getName());
        }
        return String.join("/", names);
    }
}
