import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import java.util.*;

public class Map extends Pane{
  ArrayList<Position> img = new ArrayList<>();
  ArrayList<ImageView> walls = new ArrayList<>();
  private int size;
  private int xPosition;
  private int yPosition;

  public Map(Scanner scan) throws InvalidMapException{

    while(scan.hasNext()){
      size = scan.nextInt();
      
      if (size == 0) throw new InvalidMapException("Map size can not be zero");

      for (int i = 0; i < size*30; i+=30) {
          for (int j = 0; j < size*30; j+=30) {
              ImageView wall = new ImageView();
              wall.setFitHeight(30);
              wall.setFitWidth(30);
              wall.setX(j);
              wall.setY(i);
              switch(scan.next()){
                case "0": break;
                case "P": xPosition = j; yPosition = i; break;
                case "S": wall.setImage(new Image("resources/images/steelWall.png")); img.add(new Position(j, i)); walls.add(wall); break;
                case "W": wall.setImage(new Image("resources/images/water.jpg")); img.add(new Position(j, i)); break;
                case "T": wall.setImage(new Image("resources/images/grass.png")); break;
                case "B": wall.setImage(new Image("resources/images/brickWall.png")); img.add(new Position(j, i)); walls.add(wall); break;
              }
              getChildren().add(wall);
          }
      }
    }
  }

  public int getXPosition(){
        return this.xPosition;
  }
  public int getYPosition(){
       return this.yPosition;
  }

  public ArrayList<Position> getList(){
    return this.img;
  }

  public ArrayList<ImageView> getWalls() {
    return this.walls;
  }

  public int getSize(){
      return size;
  }
}