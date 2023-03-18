import javafx.scene.image.ImageView;

interface Player{
    public void moveUp();
    public void moveDown();
    public void moveRight();
    public void moveLeft();
    public void setMap(Map m);
    public Position getPosition();
}

public class MyPlayer implements Player {
    private ImageView player;
    private Map map;
    private Position playerPosition = new Position(0, 0);

    public MyPlayer(ImageView player){
        this.player = player;
        player.setFitHeight(30);
  		player.setFitWidth(30);
    }

    @Override
    public void moveUp() {
      if (player.getY() != 0){
        player.setY(player.getY() - 30);
      }
    }

    @Override
    public void moveDown() {
      if (player.getY() != (map.getSize()*30) - 30) {
        player.setY(player.getY() + 30);
      }
    }

    @Override
    public void moveLeft() {
        if (player.getX() != 0){
          player.setX(player.getX() - 30);
        }
    }

    @Override
    public void moveRight() {
        if (player.getX() != (map.getSize()*30) - 30) {
          player.setX(player.getX() + 30);
        }
    }

    @Override
    public void setMap(Map map){
        this.map = map;
        this.player.setY(map.getYPosition());
        this.player.setX(map.getXPosition());
    }

    @Override
    public Position getPosition(){
        return playerPosition;
    }
}