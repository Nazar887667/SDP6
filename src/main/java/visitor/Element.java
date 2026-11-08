package visitor;

import java.util.List;

public interface Element {

    String getName();

    Element getParent();

    List<Element> children();

    void accept(Visitor visitor);
}
