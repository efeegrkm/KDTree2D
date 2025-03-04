import javafx.geometry.Point2D;
import java.util.ArrayList;

/**
 * KD Tree class which implements only the Homework documentation's metots(For checking ease)
 * Other general KDTree propertial metots implemented in super class.
 * Written for 2024 Fall term TOBB ETU Data Structures and algorithms third homework.
 * Student Number: 231101046
 * Student mail: eakkanat@etu.edu.tr
 * @author Efe Görkem Akkanat
*/
public class KDTree2D extends My_Abstract_KDTree2D {

    //Default constructor for KDTree2D
    public KDTree2D(){ super(); }
    /**
         * Constructor with an initial root point
         * @param root  the element of the root
    */
    public KDTree2D(Point2D root) { super(root); }
    /**
     * Inserts the given point into the KDTree.
     * @param point the point to insert
     * @return the inserted point, or null if it already exists in the tree
     */
    @Override
    public Point2D insert(Point2D point) {
        if (root == null) {
            addRoot(point);
            return point;
        }
        return insertRecursive(root, point, 0);
    }
    /**
        * Auxilary methot to insert given point by looking froma given node.
        * @param node node for traversal.
        * @param point the Point2D to be inserted.
        * @param depth recursively increasing depth to detect current dimension.
     */
    private Point2D insertRecursive(Node node, Point2D point, int depth) {
        if (point.equals(node.getElement())) return null;

        int dim = depth % 2; // 0:x 1:y
        double cmp = (dim == 0) ? (point.getX() - node.getElement().getX()) : (point.getY() - node.getElement().getY());

        if (cmp <= 0) { // Insert to left subtree
            if (node.getLeft() == null) {
                addLeft(node, point);
                return point;
            }
            return insertRecursive(node.getLeft(), point, depth + 1);
        } else { // Insert to right subtree
            if (node.getRight() == null) {
                addRight(node, point);
                return point;
            }
            return insertRecursive(node.getRight(), point, depth + 1);
        }
    }

    /**
     * Searches for a point in the KDTree.
     * @param point the point to search
     * @return the point if found, null otherwise
     */
    @Override
    public Point2D search(Point2D point) {
        return searchRecursive(root, point, 0);
    }
    /**
        * @param node node for traversal.
        * @param point the Point2D to be searched.
        * @param depth recursively increasing depth to detect current dimension.
        * @implNote uses BS to search with O(logn).
    */
    private Point2D searchRecursive(Node node, Point2D point, int depth) {
        if (node == null) return null;
        if (point.equals(node.getElement())) return point;

        int dim = depth % 2;
        double cmp = (dim == 0) ? (point.getX() - node.getElement().getX()) : (point.getY() - node.getElement().getY());

        if (cmp <= 0) return searchRecursive(node.getLeft(), point, depth + 1);
        return searchRecursive(node.getRight(), point, depth + 1);
    }

    /**
    * Removes the given point from the KDTree.
    * @param point the Point2D to be removed
    */
    @Override
    public void remove(Point2D point) {
        root = removeNode(root, point, 0);
    }
    /**
        * @param node node for traversal.
        * @param point the Point2D to be removed.
        * @param depth recursively increasing depth to detect current dimension.
    */
    private Node removeNode(Node node, Point2D point, int depth) {
        if (node == null) return null;

        int currentDim = depth % 2;

        if (node.getElement().equals(point)) {
            // if leaf reached
            if (node.getLeft() == null && node.getRight() == null) {
                size--;
                return null;
            }
            // Node with right child or both children
            if (node.getRight() != null) {
                Node successor = findMinNode(node.getRight(), currentDim, depth + 1);
                node.setElement(successor.getElement());
                node.setRight(removeNode(node.getRight(), successor.getElement(), depth + 1));
            } 
            // Node with only left child
            else if (node.getLeft() != null) {
                Node successor = findMinNode(node.getLeft(), currentDim, depth + 1);
                node.setElement(successor.getElement());
                node.setRight(removeNode(node.getLeft(), successor.getElement(), depth + 1));
                node.setLeft(null);
            }
            size--;
        } else if (compareDimension(point, node.getElement(), currentDim) < 0) {
            node.setLeft(removeNode(node.getLeft(), point, depth + 1));
        } else {
            node.setRight(removeNode(node.getRight(), point, depth + 1));
        }

        return node;
    }

    /**
     * Compares two points based on a given dimension.
     * @param p1 the first point
     * @param p2 the second point
     * @param dim the dimension (0 for x, 1 for y)
     * @return negative if p1 < p2, positive if p1 > p2, 0 if equal
     */
    private int compareDimension(Point2D p1, Point2D p2, int dim) {
        if (dim == 0) return Double.compare(p1.getX(), p2.getX());
        return Double.compare(p1.getY(), p2.getY());
    }

    /**
         * @implNote Implements dotted tree display as wanted. Uses preorder traversal.
         * There is also a treesome display methot in abstract class for better visualization.
    */
    @Override
    public void displayTree() {
        if(!isEmpty())
            preOrderTraversalDisplay(0, root());
        else System.out.println("Tree is empty.");
    }
    /**
         * Auxilary called methot by display tree. Calculates depth for each call while traversing and display it.
         * @param depth recursively increasing depth to decide dot count.
         * @param p to traverse positions recursively.
         * @implNote Should only called by displayTree.
    */
    private void preOrderTraversalDisplay(int depth, Position<Point2D> p){
        System.out.println(getDottedElement(depth, p));
        for(Position<Point2D> s: children(p)){
            preOrderTraversalDisplay(depth+1, s);
        }
    }
    /**
         * Auxilary called methot from display tree. Creates the output of a spesific call.
         * @param depth depth to decide dot count.
         * @param p to recieve element to display.
         * @implNote Should only called by displayTree.
    */
    private String getDottedElement(int depth,Position<Point2D> p){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<depth;i++){
            sb.append(". ");
        }
        sb.append(String.format("(%s, %s)",p.getElement().getX(),p.getElement().getY()));
        return sb.toString();
    }
    
    /**
     * Finds the minimum point in the specified dimension.
     * @param d the dimension (0 for x, 1 for y)
     * @return the minimum point in the specified dimension
     */
    @Override
    public Point2D findMin(int d)throws IllegalArgumentException {
        if(d<0||d>1) throw new IllegalArgumentException("KDTree2D only supports 2 dimensions 0 and 1");
        Node minNode = findMinNode(root, d, 0);
        return (minNode != null) ? minNode.getElement() : null;
    }

    /**
     * Finds the node with the minimum value in the specified dimension in the subtree rooted at the given node.
     * @param node the root of the subtree
     * @param dim the dimension to compare (0 for x, 1 for y)
     * @param depth the current depth in the tree
     * @return the node with the minimum value in the specified dimension
     */
    private Node findMinNode(Node node, int dim, int depth) {
        if (node == null) return null;

        int currentDim = depth % 2;

        if (currentDim == dim) {
            if (node.getLeft() == null) return node;
            return findMinNode(node.getLeft(), dim, depth + 1);
        }

        Node leftMin = findMinNode(node.getLeft(), dim, depth + 1);
        Node rightMin = findMinNode(node.getRight(), dim, depth + 1);

        Node minNode = node;
        if (leftMin != null && compareDimension(leftMin.getElement(), minNode.getElement(), dim) < 0) minNode = leftMin;
        if (rightMin != null && compareDimension(rightMin.getElement(), minNode.getElement(), dim) < 0) minNode = rightMin;

        return minNode;
    }

    /**
    * Finds the maximum point in the specified dimension.
    * @param d the dimension (0 for x, 1 for y)
    * @return the maximum point in the specified dimension
    */
    @Override
    public Point2D findMax(int d) throws IllegalArgumentException {
        if(d<0||d>1) throw new IllegalArgumentException("KDTree2D only supports 2 dimensions 0 and 1");
        Node maxNode = findMaxNode(root, d, 0);
        return (maxNode != null) ? maxNode.getElement() : null;
    }
    /**
    * @implNote findMax's auxilary recursive methot.
    * @param d the dimension (0 for x, 1 for y)
    * @param depth to calculate current dimension.
    */
    private Node findMaxNode(Node node, int dim, int depth) {
        if (node == null) return null;

        int currentDim = depth % 2;

        if (currentDim == dim) {
            if (node.getRight() == null) return node;
            return findMaxNode(node.getRight(), dim, depth + 1);
        }

        Node leftMax = findMaxNode(node.getLeft(), dim, depth + 1);
        Node rightMax = findMaxNode(node.getRight(), dim, depth + 1);

        Node maxNode = node;
        if (leftMax != null && compareDimension(leftMax.getElement(), maxNode.getElement(), dim) > 0) maxNode = leftMax;
        if (rightMax != null && compareDimension(rightMax.getElement(), maxNode.getElement(), dim) > 0) maxNode = rightMax;

        return maxNode;
    }

    /**
         * Methot that prints elements in range of ll-ur. Uses BS to run at O(log(n))
         * @param ll represents point of lower left corner.
         * @param ur represents point of upper right corner.
         * @implNote calls snap filler auxilary with roots inBorder info.
         * @return an iterable object of in range elements.
    */
    @Override
    Iterable<Point2D> printRectangularRange(Point2D ll, Point2D ur) throws IllegalArgumentException {
        if(ur.getX()<ll.getX()||ur.getY()<ll.getY()) throw new IllegalArgumentException("upper right corner must be bigger than lower left in all dimensions");
        ArrayList<Point2D> snap = new ArrayList<>();
        printRectangularAux(snap, ll, ur, root(), false);
        return snap;
    }

    /**
         * Auxilary methot of printRectangularRange that uses BS to skip out of range points and prints (xFits && yFits) points.
         * @param snap to store printed inRange points for printRectangularRange.
         * @param ll constant minimum x-y given by main call.
         * @param ur constant maximum x-y given by main call.
         * @param pos stores curr loc for each recursive call.
         * @param compareX stores comparing dimension via reversing for each depth.
         * @implNote (methot roadmap) Rootdan başla gezerken eğer current point geçerli dimension için maxdan >= ise lefte git,
         * geçerli dimension için minden <= ise righta git; else hem x hem ysi aralıktaysa pointi snape ekle ve hem left hem righta git.
         * external gelirse uygunsa snape ekle ve return et.
    */
    private void printRectangularAux(ArrayList<Point2D> snap,Point2D ll,Point2D ur, Position<Point2D> pos, boolean compareX){
        compareX = !compareX; // For each call compareX must be reverse of its parent.Starting from false call turned true starting from false.
        if(pos==null) return;
        if(inRangeOf(ll, ur, pos.getElement())){
            snap.add(pos.getElement());
        }
        if(left(pos) == null && right(pos) == null){
            return;
        }
        boolean isOnlyLeftNecessary = (compareX)?(pos.getElement().getX()>=ur.getX()?true:false):(pos.getElement().getY()>=ur.getY()?true:false);
        boolean isOnlyRightNecessary = (compareX)?(pos.getElement().getX()<=ll.getX()?true:false):(pos.getElement().getY()<=ll.getY()?true:false);
        if(isOnlyLeftNecessary){
            printRectangularAux(snap, ll, ur, left(pos), compareX);
        }
        else if(isOnlyRightNecessary){
            printRectangularAux(snap, ll, ur, right(pos), compareX);
        }
        else{
            printRectangularAux(snap, ll, ur, left(pos), compareX);
            printRectangularAux(snap, ll, ur, right(pos), compareX);
        }
    }
    /** 
         * Methot that returns whether point in range of ll-ur.
         * @param ll constant minimum x-y given by main call.
         * @param ur constant maximum x-y given by main call.
         * @param point point to learn props.
         * @return whether point in range of ll-ur.
    */
    private boolean inRangeOf(Point2D ll, Point2D ur, Point2D point){
        return (point.getX()>=ll.getX())&&(point.getX()<=ur.getX())&&(point.getY()>=ll.getY())&&(point.getY()<=ur.getY());
    }
    /** 
         * Methot that prints and returns the points within the radius distance of center pivot. Only visits needed subtrees via BS to run at O(log(n))
         * @param center Center pivot of radius distant points.
         * @param radius Maximum distance of within points to the center.
         * @return an iterable object of points within range.
    */
    @Override
    public Iterable<Point2D> printCircularRange(Point2D center, double radius) throws IllegalArgumentException{
        if(radius<0) throw new IllegalArgumentException("Radius can not be negative.");
        ArrayList<Point2D> result = new ArrayList<>();
        circularRangeAux(result, center, radius, root(), true);
        return result;
    }
    /**    
         * Auxilary methot of printCircularRange that uses below pseudoCode to visit essantial subtrees. 
         * pseudo-code for circularRangeAux:
         * if(pos==null) return
         * if (abs(distance of c to pos)>r) : if(compareX) (if(pos.X>c.X) aux(left(pos))) (if(pos.X<c.X) aux(right(pos))) else (same but for y this time)
         * else : print posPoint call Aux for left and right.
         * @param center Center pivot of radius distant points.
         * @param radius Maximum distance of within points to the center.
         * @return an iterable object of points within range.
    */
    private void circularRangeAux(ArrayList<Point2D> result, Point2D center, double radius, Position<Point2D> pos, boolean compareX) {
        if (pos == null) return;
        double distance = pos.getElement().distance(center);
        if (distance <= radius) {
            // If the current point is in the Range, print and add it to the result list
            result.add(pos.getElement());
        }
        double posCoord = compareX ? pos.getElement().getX() : pos.getElement().getY();
        double centerCoord = compareX ? center.getX() : center.getY();
        if (centerCoord - radius <= posCoord) {
            // Search the left when we want a smaller distance to pivot.
            circularRangeAux(result, center, radius, left(pos), !compareX);
        }
        if (centerCoord + radius >= posCoord) {
            // Search the right when we want a bigger distance to pivot.
            circularRangeAux(result, center, radius, right(pos), !compareX);
        }
    }
}
