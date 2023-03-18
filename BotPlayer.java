import javafx.scene.image.ImageView;

public class BotPlayer implements Player {
	private ImageView bot;
	private Map map;
	private Position botPosition = new Position(0, 0);

	public BotPlayer(ImageView bot) {
		this.bot = bot;
        bot.setFitHeight(30);
  		bot.setFitWidth(30);
	}	

	@Override
    public void moveUp() {
      if (bot.getY() != 0){
        bot.setY(bot.getY() - 30);
      }
    }

    @Override
    public void moveDown() {
      if (bot.getY() != (map.getSize()*30) - 30) {
        bot.setY(bot.getY() + 30);
      }
    }

    @Override
    public void moveLeft() {
        if (bot.getX() != 0){
          bot.setX(bot.getX() - 30);
        }
    }

    @Override
    public void moveRight() {
        if (bot.getX() != (map.getSize()*30) - 30) {
          bot.setX(bot.getX() + 30);
        }
    }

    @Override
    public void setMap(Map map){
        this.map = map;
        this.bot.setY(map.getYPosition());
        this.bot.setX(map.getXPosition());
    }

    @Override
    public Position getPosition(){
        return botPosition;
    }
}