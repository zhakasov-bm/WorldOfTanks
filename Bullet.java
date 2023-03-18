import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;


public class Bullet extends Circle {

    Bullet(double x, double y, double radius) {
      super(x, y, radius);

      setFill(Color.ORANGE);  // Set bullet color
      setStroke(Color.RED);
    }
}
