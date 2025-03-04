import java.util.ArrayList;

import javafx.geometry.Point2D;
/**
 * KD Tree class which implements only the Homework documentation's metots(For checking ease)
 * Other general KDTree propertial metots implemented in super class.
 * Written for 2024 Fall term TOBB ETU Data Structures and algorithms third homework.
 * Student Number: 231101046
 * Student mail: eakkanat@etu.edu.tr
 * @author Efe Görkem Akkanat
*/
public class KDTree2DAlternate extends My_Abstract_KDTree2D{

    //Default constructor for KDTree2D
    public KDTree2DAlternate(){ super(); }
    /**
         * Constructor with an initial root point
         * @param root  the element of the root
    */
    public KDTree2DAlternate(Point2D root) { super(root); }

    /**
         * Inserts a point to its correct place via using BST properties.
         * @param point point to be insert.
         * @return inserted element(if point exists returns null)
    */
    @Override
    public Point2D insert(Point2D point) {
        if(search(point) != null) return null;
        if(size() == 0) return addRoot(point).getElement();
        Position<Point2D> cur = root();
        boolean compareX = true;
        while(isInternal(cur)){
            if(compareX){
                if(point.getX()>cur.getElement().getX()){
                    if(right(cur)==null) break;
                    cur = right(cur);
                }
                else{
                    if(left(cur)==null) break;
                    cur = left(cur);
                }
            }else{
                if(point.getY()>cur.getElement().getY()){
                    if(right(cur)==null) break;
                    cur = right(cur);
                }
                else{
                    if(left(cur)==null) break;
                    cur = left(cur);
                }
            }
            compareX = !compareX;
        }
        if((compareX)?(point.getX()>cur.getElement().getX()):(point.getY()>cur.getElement().getY())){
            addRight(cur, point);
        }
        else{
            addLeft(cur, point);
        }
        return point;
    }
    /**
         * Searches for the given point in the tree.
         * @implNote !!!This methot can also be implemented with O(log(n))!!! via 2D binary search as below remove methot.
         * @implNote Current implementation runs with O(n).
         * But there was no time limitations in homework documentation for this methot so I kept it simple.
         * @param point point to search.
         * @return the point if found, returns null otherwise.
    */
    @Override
    public Point2D search(Point2D point) {
        for(Position<Point2D> pos2D: positions()){
            if((pos2D.getElement().getX() == point.getX()) && (pos2D.getElement().getY() == point.getY())) return point;
        }
        return null;
    }

    /**
         * Finds and remove the given point by bounding its parent with its children properly.
         * @implNote Implemented with O(log(n)) Binary traversal
         * @param point point to remove.
    */
    @Override
    public void remove(Point2D point) {
        if (isEmpty() || search(point) == null) {
            System.out.println("Point not found or tree is empty.");
            return;
        }
        set(root(), removeAux(root(), point, true).getElement());
    }

    private Position<Point2D> removeAux(Position<Point2D> node, Point2D point, boolean compareX) {
        if (node == null) return null;

        if (node.getElement().equals(point)) {
            if (isExternal(node)) {
                return null;
            }

            // İç düğümse, yerine geçen düğümü bul.
            if (compareX) {
                Point2D replacement = findMinAux(true, right(node), false); // Sağ alt ağacın min X'ini bul.
                if (replacement == null) {
                    replacement = findMinAux(true, left(node), false); // Sol alt ağacın min X'ini bul.
                }
                ((Node) node).setElement(replacement); // TreeNode sınıfına cast edilip yeni değer yerleştirildi.
                ((Node) node).setRight((Node)removeAux(right(node), replacement, !compareX));
            } else {
                Point2D replacement = findMinAux(false, right(node), false); // Sağ alt ağacın min Y'sini bul.
                if (replacement == null) {
                    replacement = findMinAux(false, left(node), false); // Sol alt ağacın min Y'sini bul.
                }
                ((Node) node).setElement(replacement); // TreeNode sınıfına cast edilip yeni değer yerleştirildi.
                ((Node) node).setRight((Node)removeAux(right(node), replacement, !compareX));
            }
        } else {
            // Hedef düğümü aramaya devam et.
            if ((compareX && point.getX() < node.getElement().getX()) ||
                (!compareX && point.getY() < node.getElement().getY())) {
                ((Node) node).setLeft((Node)removeAux(left(node), point, !compareX)); // cast edilerek setLeft kullanıldı.
            } else {
                ((Node) node).setRight((Node)removeAux(right(node), point, !compareX)); // cast edilerek setRight kullanıldı.
            }
        }

        return node;
    }

    /**
         * The helper methot to check whether c is the left child of p.
         * @param p parent to check its left child.
         * @param c expected left child of p.
         * @return whether c is the left child of p.
         * @throws RuntimeException when c is not even a child of p.
    */
    private boolean isLeftOf(Node p,Node c) throws RuntimeException{
        if(c.getParent()!=p) throw new RuntimeException(String.format("node %s is not a child of %s", c, p));
        return (left(p)==c);
    }

    /**
         * Methot to display tree with preorder traversal.
         * @implNote Has 2 auxilary helper methots preOrderTraversalDisplay and getDottedElement.
    */
    @Override
    public void displayTree() {
        if(!isEmpty())
            preOrderTraversalDisplay(0, root());
        else System.out.println("Tree is empty.");
    }
    /**
         * Auxilary called methot by display tree. Calculates depth for each call while traversing and display it.
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
         * Methot to return tree's min element for a certain dimension.
         * @param d represents dimension 0 for x 1 for y.(Other integers will cause Runtime Exception)
         * @return minimum element recursively.
         * @throws RuntimeException when d given greater than 1 or less then 0.
    */
    /**
         * Methot to return tree's min element for a certain dimension.
         * @param d represents dimension 0 for x 1 for y.(Other integers will cause Runtime Exception)
         * @return minimum element recursively.
         * @throws RuntimeException when d given greater than 1 or less then 0.
    */
    @Override
    Point2D findMin(int d) throws RuntimeException{
        if(d>1||d<0) return null;
        return findMinAux((d==0)?true:false, root(), true);
    }
    /**
         * Auxilary methot for findMin.
         * @param d represents dimension 0 for x 1 for y.(Other integers will cause Runtime Exception)
         * @param p represents position for each recursive call.
         * @param compareX makes the decision to compare with x or y for each recursive call.
         * @implNote This implementation only looks the right subtrees only if compareX is false for only 1 depth to the right. So it runs with O(log(n)) in long term.
         * @return minimum element recursively.
    */
    private Point2D findMinAux(boolean d, Position<Point2D> p, boolean compareX){
        if(isExternal(p)) return p.getElement();
        Point2D leftMin = findMinAux(d, left(p), !compareX);
        Point2D rightMin = new Point2D(Integer.MAX_VALUE, Integer.MAX_VALUE);// initial max value for not to interfere left if it doesn't declared below.
        if(d!=compareX){
            rightMin = findMinAux(d, right(p), !compareX);
        }
        if(d == true){
            return (leftMin.getX()<rightMin.getX())? leftMin: rightMin;
        }
        else{
            return (leftMin.getY()<rightMin.getY())? leftMin: rightMin;
        }
    }

    /**
         * Methot to return tree's max element for a certain dimension.
         * @param d represents dimension 0 for x 1 for y.(Other integers will cause Runtime Exception)
         * @return maximum element recursively.
         * @throws RuntimeException when d given greater than 1 or less then 0.
    */
    @Override
    Point2D findMax(int d) {
        if(d>1||d<0) throw new RuntimeException("KDTree2D class supports maximum two dimensions \"" + d+1 + "\" dimensions doesn't supported");
        return findMaxAux((d==0)?true:false, root(), true);
    }

    /**
         * Auxilary methot for findMax.
         * @param d represents dimension 0 for x 1 for y.(Other integers will cause Runtime Exception)
         * @param p represents position for each recursive call.
         * @param compareX makes the decision to compare with x or y for each recursive call.
         * @implNote This implementation only looks the left subtrees only if compareX is false for only 1 depth to the left. So it runs with O(log(n)) in long term.
         * @return maximum element recursively.
    */
    private Point2D findMaxAux(boolean d, Position<Point2D> p, boolean compareX){
        if(isExternal(p)) return p.getElement();
        Point2D leftMax = new Point2D(Integer.MIN_VALUE, Integer.MIN_VALUE);// initial min value for not to interfere right if it doesn't declared below.
        Point2D rightMax = findMaxAux(d, right(p), !compareX);
        if(d!=compareX){
            leftMax = findMinAux(d, left(p), !compareX);
        }
        if(d == true){
            return (leftMax.getX()>rightMax.getX())? leftMax: rightMax;
        }
        else{
            return (leftMax.getY()>rightMax.getY())? leftMax: rightMax;
        }
    } 
    

    /**
         * Methot that prints elements in range of ll-ur.
         * @param ll represents point of lower left corner.
         * @param ur represents point of upper right corner.
         * @implNote calls snap filler auxilary with roots inBorder info.
         * @return an iterable object of in range elements.
    */
    @Override
    Iterable<Point2D> printRectangularRange(Point2D ll, Point2D ur) {
        ArrayList<Point2D> snap = new ArrayList<>();
        double minX=ll.getX(), maxX=ur.getX(), minY=ll.getY() ,maxY=ur.getY();
        boolean rootFitsX = (root().getElement().getX()<maxX)&&(root().getElement().getX()>minX);
        boolean rootFitsY = (root().getElement().getY()<maxY)&&(root().getElement().getY()>minY);
        printRectangularAux(snap, size, size, size, size, root, true, rootFitsX, rootFitsY);
        return snap;
    }

    /**
         * Auxilary methot of printRectangularRange that uses BS to skip out of range points and prints (xFits && yFits) points
         * @param snap to store printed inRange points for printRectangularRange.
         * @param minX constant minimum x given by main call.
         * @param minY constant minimum y given by main call.
         * @param maxX constant maximym x given by main call.
         * @param maxY constant maximum y given by main call.
         * @param pos stores curr loc for each recursive call.
         * @param compareX stores comparing dimension via reversing for each depth.
         * @param xFits informs that x Fitted in previous dimension.
         * @param yFits informs that y Fitted in previous dimension.
    */
    private void printRectangularAux(ArrayList<Point2D> snap, int minX,int minY,int maxX, int maxY, Position<Point2D> pos, boolean compareX, boolean xFits,boolean yFits){
        if(pos==null) return;
        if(compareX){
            if(pos.getElement().getX()<minX){
                // if we are below minX set xFits false and call for the bigger X(right).
                xFits = false;
                printRectangularAux(snap, minX, minY, maxX, maxY, right(pos), !compareX, xFits, yFits);
            }  
            else if(pos.getElement().getX()>maxX){
                // if we are above maxX set xFits false and call for the lesser X(left).
                xFits = false;
                printRectangularAux(snap, minX, minY, maxX, maxY, left(pos), !compareX, xFits, yFits);
            } 
            else{
                xFits = true;
                if(yFits){
                    // If the current point is in the Range, print and add it to the result list
                    System.out.println(pos.getElement());
                    printRectangularAux(snap, minX, minY, maxX, maxY, left(pos), !compareX, true, true);
                    printRectangularAux(snap, minX, minY, maxX, maxY, right(pos), !compareX, true, true);
                }
            }
        }
        else{
            if(pos.getElement().getY()<minY){
                // if we are below minY set yFits false and call for the bigger Y(right).
                yFits = false;
                printRectangularAux(snap, minX, minY, maxX, maxY, right(pos), !compareX, xFits, yFits);
            }  
            else if(pos.getElement().getY()>maxY){
                // if we are above maxY set yFits false and call for the lesser Y(left).
                yFits = false;
                printRectangularAux(snap, minX, minY, maxX, maxY, left(pos), !compareX, xFits, yFits);
            } 
            else{
                yFits = true;
                if(xFits){
                    // If the current point is in the Range, print and add it to the result list
                    System.out.println(pos.getElement());
                    printRectangularAux(snap, minX, minY, maxX, maxY, left(pos), !compareX, true, true);
                    printRectangularAux(snap, minX, minY, maxX, maxY, right(pos), !compareX, true, true);
                }
            }
        }
        
    }
    /** 
         * Methot that prints and returns the points within the radius distance of center pivot. Only visits needed subtrees via BS. 
         * @param center Center pivot of radius distant points.
         * @param radius Maximum distance of within points to the center.
         * @return an iterable object of points within range.
    */
    @Override
    public Iterable<Point2D> printCircularRange(Point2D center, double radius) {
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
            System.out.println(pos.getElement());
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
