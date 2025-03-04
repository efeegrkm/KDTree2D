
import javafx.geometry.Point2D;

public class myTest {
    public static void main(String[] args) {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = 
        {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, -10),
            new Point2D(80, 90),
            new Point2D(-15, 70),
            new Point2D(60, 20),
            new Point2D(0, 0),
            new Point2D(60, 20),
            new Point2D(60, 20),
            new Point2D(2, 4),
            new Point2D(100, -120),
        };

        for (Point2D p : pointsToInsert) {
            System.out.println("\n -> inserted: " + tree.insert(p));
            tree.displayTreeAlternative();
        }
        Iterable<Position<Point2D>> ar = tree.positions();
        for(Position<Point2D> i :ar){
            try{
            System.out.println(i.getElement()+ "-> Left: "+ tree.left(i).getElement()+ "&& Right: "+ tree.right(i).getElement());}
            catch(Exception e){
                if(tree.left(i) != null) System.out.println(String.format("%s'in lefti %s", i.getElement(),tree.left(i).getElement()));
                if(tree.right(i) != null) System.out.println(String.format("%s'in rightı %s", i.getElement(),tree.right(i).getElement()));
            }
        }
        tree.remove(new Point2D(30, 40));
        tree.displayTreeAlternative();
        tree.displayTree();
        Iterable<Point2D> inRange = tree.printRectangularRange(new Point2D(-10, 0), new Point2D(100, 80));
        System.out.println("rect range:");
        for(Point2D point2d:inRange){
            System.out.println(point2d);
        }
        Iterable<Point2D> inRangeC = tree.printCircularRange(new Point2D(-15, 70), 0);
        System.out.println("circ range");
        for(Point2D point2d:inRangeC){
            System.out.println(point2d);
        }
        // for(Position<Point2D> i :ar){
        //     try{
        //     System.out.println(i.getElement()+ "-> Left: "+ tree.left(i).getElement()+ "&& Right: "+ tree.right(i).getElement());}
        //     catch(Exception e){
        //         if(tree.left(i) != null) System.out.println(String.format("%s'in lefti %s", i.getElement(),tree.left(i).getElement()));
        //         if(tree.right(i) != null) System.out.println(String.format("%s'in rightı %s", i.getElement(),tree.right(i).getElement()));
        //     }
        // }
    }
}
