import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * An abstract class that implements general BinaryTree<Point2D> metots and defines general KDTree metots.
 * Written for 2024 Fall term TOBB ETU Data Structures and algorithms third homework.
 * Student Number: 231101046
 * Student mail: eakkanat@etu.edu.tr
 * @author Efe Görkem Akkanat
*/
public abstract class My_Abstract_KDTree2D implements BinaryTree<Point2D>{
    //Default constructor for KDTree2D
    public My_Abstract_KDTree2D(){}
    /**
         * Constructor with an initial root point
         * @param root  the element of the root
    */
    public My_Abstract_KDTree2D(Point2D root){
        addRoot(root);
    }
    //--------------- Nested Node Class --------------------
    protected static class Node implements Position<Point2D> {
        private Point2D element;          // an element stored at this node
        private Node parent;     // a reference to the parent node (if any)
        private Node left;       // a reference to the left child (if any)
        private Node right;      // a reference to the right child (if any)
    
        /**
         * Constructs a node with the given element and neighbors.
         *
         * @param e  the element to be stored
         * @param above       reference to a parent node
         * @param leftChild   reference to a left child node
         * @param rightChild  reference to a right child node
         */
        public Node(Point2D e, Node above, Node leftChild, Node rightChild) {
          element = e;
          parent = above;
          left = leftChild;
          right = rightChild;
        }
    
        // accessor methods
        public Point2D getElement() { return element; }
        public Node getParent() { return parent; }
        public Node getLeft() { return left; }
        public Node getRight() { return right; }
    
        // update methods
        public void setElement(Point2D e) { element = e; }
        public void setParent(Node parentNode) { parent = parentNode; }
        public void setLeft(Node leftChild) { left = leftChild; }
        public void setRight(Node rightChild) { right = rightChild; }
    } //----------- end of nested Node class -----------

    // KDTree instance variables
    /** The root of the binary tree */
    protected Node root = null;

    /** The number of nodes in the binary tree */
    protected int size = 0;

    /**
   * Verifies that a Position belongs to the Point2D type Node class
   *
   * @param p   a Position (that should belong to this tree)
   * @return    the underlying Node instance for the position
   * @throws IllegalArgumentException if an invalid position is detected
   */
    protected Node validate(Position<Point2D> p) throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node node = (Node) p;       // safe cast
        if (node.getParent() == node)     // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
   * Returns the root Position of the tree (or null if tree is empty).
   * @return root Position of the tree (or null if tree is empty)
   */
    @Override
    public Position<Point2D> root() {
        return root;
    }

     /**
    * Returns the number of nodes in the tree.
    * @return number of nodes in the tree
    */
    @Override
    public int size() {
        return size;
    }

    /**
   * Returns the Position of p's parent (or null if p is root).
   *
   * @param p    A valid Position within the tree
   * @return Position of p's parent (or null if p is root)
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
    @Override
    public Position<Point2D> parent(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        return node.getParent();
    }

    /**
    * Returns the Position of p's left child (or null if no child exists).
    *
    * @param p A valid Position within the tree
    * @return the Position of the left child (or null if no child exists)
    * @throws IllegalArgumentException if p is not a valid Position for this tree
    */
    @Override
    public Position<Point2D> left(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        return node.getLeft();
    }

    /**
    * Returns the Position of p's right child (or null if no child exists).
    *
    * @param p A valid Position within the tree
    * @return the Position of the right child (or null if no child exists)
    * @throws IllegalArgumentException if p is not a valid Position for this tree
    */
    @Override
    public Position<Point2D> right(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        return node.getRight();
    }

    /**
    * Returns the Position of p's sibling (or null if no child exists).
    *
    * @param p A valid Position within the tree
    * @return the Position of the sibling (or null if no sibling exists)
    * @throws IllegalArgumentException if p is not a valid Position for this tree
    */
    @Override
    public Position<Point2D> sibling(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        if(node==root||numChildren(node.getParent())==1) return null;
        return (node.getParent().getLeft()==node) ? node.getParent().getRight() : node.getParent().getLeft();
    }

    /**
    * Returns an iterable list of p's children (or null if no child exists).
    *
    * @param p A valid Position within the tree
    * @return an iterable list of p's children (or null if no sibling exists)
    * @throws IllegalArgumentException if p is not a valid Position for this tree
    */
    @Override
    public Iterable<Position<Point2D>> children(Position<Point2D> p) throws IllegalArgumentException {
        ArrayList<Position<Point2D>> snapshot = new ArrayList<>(2);
        Position<Point2D> left = left(p);
        Position<Point2D> right = right(p);
        if(left!=null)
            snapshot.add(left);
        if(right!=null)
            snapshot.add(right);
        return snapshot;
    }
    /**
     * Displays tree in a treesome way.
     */
    public void displayTreeAlternative() {
        if (!isEmpty()) {
            preOrderTreeDisplay(root(), "", true);
        } else {
            System.out.println("Tree is empty.");
        }
    }

    /**
     * Helper method for displaying the tree in a tree-like structure.
     * @param p the current position being displayed
     * @param prefix the current prefix for tree structure
     * @param isTail whether the current node is the last child
     */
    private void preOrderTreeDisplay(Position<Point2D> p, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") 
                + String.format("(%s, %s)", p.getElement().getX(), p.getElement().getY()));
        
        List<Position<Point2D>> children = new ArrayList<>();
        children.addAll((ArrayList<Position<Point2D>>)children(p));
        for (int i = 0; i < children.size(); i++) {
            boolean last = (i == children.size() - 1);
            preOrderTreeDisplay(children.get(i), prefix + (isTail ? "    " : "│   "), last);
        }
    }
    // -----------update methods supported by this class------------

    /**
    * Factory function to create a new node storing element e.
    *
    * @param e element of the new node
    * @param parent parent of the new node
    * @param left left child of the new node
    * @param right right child of the new node
    * @return created node
    */
    protected Node createNode(Point2D e, Node parent,Node left, Node right) {
        return new Node(e, parent, left, right);
    }

    /**
    * Places element e at the root of an empty tree and returns its new Position.
    *
    * @param e   the new element
    * @return the Position of the new element
    * @throws IllegalStateException if the tree is not empty
    */
    protected Position<Point2D> addRoot(Point2D e) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
            root = createNode(e, null, null, null);
        size = 1;
        return root;
    }

    /**
    * Creates a new left child of Position p storing element e and returns its Position.
    *
    * @param p   the Position to the left of which the new element is inserted
    * @param e   the new element
    * @return the Position of the new element
    * @throws IllegalArgumentException if p is not a valid Position for this tree
    * @throws IllegalArgumentException if p already has a left child
    */
    protected Position<Point2D> addLeft(Position<Point2D> p, Point2D e) throws IllegalArgumentException {
        Node parent = validate(p);
        if (parent.getLeft() != null)
            throw new IllegalArgumentException("p already has a left child");
        Node child = createNode(e, parent, null, null);
        parent.setLeft(child);
        size++;
        return child;
    }

    /**
    * Creates a new right child of Position p storing element e and returns its Position.
    *
    * @param p   the Position to the right of which the new element is inserted
    * @param e   the new element
    * @return the Position of the new element
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    * @throws IllegalArgumentException if p already has a right child
    */
    protected Position<Point2D> addRight(Position<Point2D> p, Point2D e) throws IllegalArgumentException {
        Node parent = validate(p);
        if (parent.getRight() != null)
            throw new IllegalArgumentException("p already has a right child");
        Node child = createNode(e, parent, null, null);
        parent.setRight(child);
        size++;
        return child;
    }

    /**
    * Replaces the element at Position p with element e and returns the replaced element.
    *
    * @param p   the relevant Position
    * @param e   the new element
    * @return the replaced element
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    */
    public Point2D set(Position<Point2D> p, Point2D e) throws IllegalArgumentException {
        Node node = validate(p);
        Point2D temp = node.getElement();
        node.setElement(e);
        return temp;
    }
    /**
    * Returns the number of children of p
    *
    * @param p   the relevant Position
    * @return number of children
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    */
    @Override
    public int numChildren(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        int num = 0;
        if(node.getLeft()!=null) num++;
        if(node.getRight()!=null) num++;
        return num;
    }

    /**
    * Returns whether p is an internal node.
    *
    * @param p   the relevant Position
    * @return   whether p is an internal node.
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    */
    @Override
    public boolean isInternal(Position<Point2D> p) throws IllegalArgumentException {
        return numChildren(p)!=0;
    }

    /**
    * Returns whether p is an external node.
    *
    * @param p   the relevant Position
    * @return   whether p is an external node.
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    */
    @Override
    public boolean isExternal(Position<Point2D> p) throws IllegalArgumentException {
        return numChildren(p)==0;
    }

    /**
    * Returns whether p is the root.
    *
    * @param p   the relevant Position
    * @return   whether p is the root.
    * @throws IllegalArgumentException if p is not a valid Position for this tree.
    */
    @Override
    public boolean isRoot(Position<Point2D> p) throws IllegalArgumentException {
        Node node = validate(p);
        return node == root();
    }

    /**
    * Returns whether tree is empty.
    * @return   whether tree is empty.
    */
    @Override
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using an inorder traversal
     * @param p       Position serving as the root of a subtree
     * @param snapshot  a list to which results are appended
     */
    private void inorderSubtree(Position<Point2D> p, List<Position<Point2D>> snapshot) {
        if (left(p) != null)
            inorderSubtree(left(p), snapshot);
        snapshot.add(p);
        if (right(p) != null)
            inorderSubtree(right(p), snapshot);
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in inorder.
     * @return iterable collection of the tree's positions reported in inorder
     */
    public Iterable<Position<Point2D>> inorder() {
        List<Position<Point2D>> snapshot = new ArrayList<>();
        if (!isEmpty())
        inorderSubtree(root(), snapshot);   // fill the snapshot recursively
        return snapshot;
    }

    /**
     * Returns an iterable collection of the positions of the tree using inorder traversal
     * @return iterable collection of the tree's positions using inorder traversal
     */
    @Override
    public Iterable<Position<Point2D>> positions() {
        return inorder();
    }

    //---------------- nested ElementIterator class ----------------
    /* This class adapts the iteration produced by positions() to return elements. */
    private class Point2D_Iterator implements Iterator<Point2D> {
        Iterator<Position<Point2D>> posIterator = positions().iterator();
        public boolean hasNext() { return posIterator.hasNext(); }
        public Point2D next() { return posIterator.next().getElement(); }
        public void remove() { posIterator.remove(); }
    }
    /**
     * Returns an elemental iterator object.
     * @return Iterator object.
     */
    @Override
    public Iterator<Point2D> iterator() {
        return new Point2D_Iterator();
    }
    /*
     * Declarations of homework methots:
     */
    public abstract Point2D insert(Point2D point);
    public abstract Point2D search(Point2D point);
    public abstract void remove(Point2D point);
    public abstract void displayTree();
    abstract Point2D findMin(int d);
    abstract Point2D findMax(int d);
    abstract Iterable<Point2D> printRectangularRange(Point2D ll, Point2D ur);
    abstract Iterable<Point2D> printCircularRange(Point2D c, double r);
    /*
     * End of homework methot declarations.
     */
}
