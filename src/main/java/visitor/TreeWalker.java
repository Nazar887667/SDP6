package visitor;

public final class TreeWalker {

    private TreeWalker() {
    }

    public static void walk(Element element, Visitor visitor) {
        element.accept(visitor);
        for (Element child : element.children()) {
            walk(child, visitor);
        }
    }
}
