# Visitor Pattern — Advanced File System

Assignment 06, ShP-2216 Software Design Patterns. Java 17, Maven, JUnit 5.

## Run the tests
```
mvn test
```

## Run the demonstration
```
mvn -q compile
java -cp target/classes kz.aitu.visitor.Main
```
Expected output (deterministic):
```
Total size: 59300 bytes
Search (*.java, size >= 1000):
  src/App.java
  src/model/User.java
Statistics: files=6, directories=5, maxDepth=3
```

## Design notes
- **Traversal owner:** `TreeWalker` (pre-order, children sorted by name, each node once). Elements only dispatch in `accept`; visitors only handle one node.
- **Visitor state:** visitors are reusable. `totalSizeOf`, `search` and `collect` reset internal state at the start of every run, so results never depend on earlier runs.
- **Paths / depth:** derived from the parent chain up to the chosen root (root depth = 0; paths are root-relative, `/`-separated, sorted lexicographically; a root that is a file yields its own name).
- **Extension boundary:** `StatisticsVisitor` was added in a separate commit with no change to `FileNode`, `DirectoryNode` or `accept`. Adding a new *operation* needs only a new visitor; adding a new *element type* requires a new `visit` overload in `Visitor` and every visitor.
- **Integrity:** negative sizes, blank/`/` names, duplicate sibling names, a second parent and cycles are rejected (`IllegalArgumentException` / `IllegalStateException`). `children()` is an unmodifiable snapshot.

## Test classes
`DoubleDispatchTest`, `TotalSizeVisitorTest`, `SearchVisitorTest`, `StatisticsVisitorTest`, `TreeIntegrityTest`
