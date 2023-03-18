import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.geometry.*;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;


public class Main extends Application{
  private static String fileName;
  
  //Pane
  private BorderPane root;
  private Pane pane;
  private VBox vbox;

  //Scene
  private Scene scene;

  //Map section
  private int size;
  private ArrayList<Position> list;
  private ArrayList<ImageView> walls;
  private int length;

  //Player and Bot scores
  private int score_1 = 0;
  private int score_2 = 0;
  private Label label_1;
  private Label label_2;
  
  //Bullet section
  private Bullet bullet;
  private int direction_1 = 3;
  private int direction_2 = 7;
  
  //Player section
  private ImageView tank = new ImageView(new Image("resources/images/red.png"));
  private ImageView bot = new ImageView(new Image("resources/images/blue.png"));
  private MyPlayer player = new MyPlayer(tank);
  private BotPlayer botPlayer = new BotPlayer(bot);
  private Position playerPos;
  private Position botPos = new Position((int)bot.getX(), (int)bot.getY());
  private boolean move = false;

  //Time
  private SimpleDateFormat t = new SimpleDateFormat("HH:mm");
  private String time = String.valueOf(t.format(new Date()));

  //Main method for input textfile
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("\nChoose a map < map1 >, < map2 > or < map3 >, !WITH .txt! : ");
    fileName = input.next();

    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws FileNotFoundException{
      root = new BorderPane(); // main pane
      //game pane
      pane = new Pane();
      // pane.setPrefSize(30*size, size*30);
      pane.setPadding(new Insets(200, 20, 20, 20));
      pane.setStyle("-fx-border-color: white; -fx-background-color: black");
      
      Scanner scan = new Scanner(new File("resources/maps/" + fileName));
      
      try{
        Map map = new Map(scan);
        size = map.getSize();
        System.out.println("Map size: " + size);
        list = map.getList();
        walls = map.getWalls();
        player.setMap(map);
        botPlayer.setMap(map);
        pane.getChildren().add(map);
      } 
      catch(Exception e){}

      length = size * 30;
      bot.setX(length - 30);
      bot.setY(length - 30);

      //VBox for results
      VBox vbox = new VBox(15);
      vbox.setPadding(new Insets(20, 20, 20, 20));
      vbox.setStyle("-fx-border-color: white;");

      TextField box = new TextField(time);
      box.setPrefWidth(100);
      box.setFocusTraversable(false);
      box.setEditable(false);
      
      //Player's life
      Rectangle live_1 = new Rectangle(100, 10);
      live_1.setStyle("-fx-fill: blue; -fx-stroke: black; ");
      label_1 = new Label("Red: " + score_1);

      //Bot's life
      Rectangle live_2 = new Rectangle(100, 10);
      live_2.setStyle("-fx-fill: red; -fx-stroke: black; ");
      label_2 = new Label("Blue: " + score_2);
      vbox.getChildren().addAll(live_1, live_2, box, label_1, label_2);


      root.setCenter(pane);
      root.setRight(vbox);

      //Thread
      Thread t1 = new Thread(new Move(tank, pane));
      t1.start();

      Thread t2 = new Thread(new Move(bot, pane));
      t2.start();

      //Scene
      scene = new Scene(root, size*30 + 145, size*30);
      stage.setScene(scene);
      stage.setTitle("WorldOfTanks");
      stage.show();
      // stage.getIcons().add(new Image(new File("resources/images/icon.jpg").toURI().toString()));
      stage.setResizable(false);

      //Controls key events;
      scene.setOnKeyPressed(e ->{
        switch(e.getCode()){    
            case UP:    checkMovePlayer("Up", 3, tank);    break;
            case DOWN:  checkMovePlayer("Down", 4, tank);  break;
            case LEFT:  checkMovePlayer("Left", 2, tank);  break;
            case RIGHT: checkMovePlayer("Right", 1, tank); break;
            case SPACE: shootPlayer(tank, bot, live_1); System.out.println("Fire!"); break;
        }

        ////Controls key events for bot;
        switch(e.getCode()) {
            case W: checkMoveBot("W", 7, bot);  break;
            case S: checkMoveBot("S", 8, bot);  break;
            case A: checkMoveBot("A", 6, bot);  break;
            case D: checkMoveBot("D", 5, bot);  break;
            case E: shootBot(bot, tank, live_2); System.out.println("Bot's shooting!"); break;
        }
      });
  } 
    // Check move method for Player
    public void checkMovePlayer(String str, int direction_1, ImageView img){
      
      this.direction_1 = direction_1;
      System.out.println(str); 

      switch(direction_1){
        case 1: playerPos = new Position((int)img.getX() + 30, (int)img.getY()); 
                img.setRotate(90); break;
        case 2: playerPos = new Position((int)img.getX() - 30, (int)img.getY()); 
                img.setRotate(-90); break;
        case 3: playerPos = new Position((int)img.getX(), (int)img.getY() - 30); 
                img.setRotate(0); break;
        case 4: playerPos = new Position((int)img.getX(), (int)img.getY() + 30); 
                img.setRotate(180); break; 
      }
      for (Position p : list) {
        if( playerPos.equals(p) || playerPos.equals(botPos)){
          move = true; System.out.println("Invalid position!"); break;
        }
      }

      if(move == false){
        switch(str) {
          case "Up": player.moveUp(); break;
          case "Down": player.moveDown(); break;
          case "Left": player.moveLeft(); break;
          case "Right": player.moveRight(); break;
        }
      } move = false;
    }

    //Check move method for BotPlayer
    public void checkMoveBot(String str, int direction_2, ImageView img){
      
      this.direction_2 = direction_2;
      System.out.println(str); 

      switch(direction_2){
        case 5: botPos = new Position((int)img.getX() + 30, (int)img.getY()); 
                img.setRotate(90); break;
        case 6: botPos = new Position((int)img.getX() - 30, (int)img.getY()); 
                img.setRotate(-90); break;
        case 7: botPos = new Position((int)img.getX(), (int)img.getY() - 30); 
                img.setRotate(0); break;
        case 8: botPos = new Position((int)img.getX(), (int)img.getY() + 30); 
                img.setRotate(180); break; 
      }
      for (Position p : list) {
        if(botPos.equals(p) || botPos.equals(playerPos)){
          move = true; System.out.println("Invalid position!"); break;
        }
      }

      if(move == false){
        switch(str) {
          case "W": botPlayer.moveUp(); break;
          case "S": botPlayer.moveDown(); break;
          case "A": botPlayer.moveLeft(); break;
          case "D": botPlayer.moveRight(); break;
        }
      }
      move = false;
    }

    //Shoot method for Player
    public void shootPlayer(ImageView img, ImageView enemy, Rectangle live){
       Bullet bullet = new Bullet(img.getX() + 30/2, img.getY() + 30/2, 2);
       pane.getChildren().add(bullet);

       AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                switch(direction_1) {
                  case 1: bullet.setCenterX(bullet.getCenterX() + 5); break;
                  case 2: bullet.setCenterX(bullet.getCenterX() - 5); break;
                  case 3: bullet.setCenterY(bullet.getCenterY() - 5); break;
                  case 4: bullet.setCenterY(bullet.getCenterY() + 5); break;
                }

                for (ImageView wall : walls) {
                  if(bullet.getBoundsInParent().intersects(wall.getBoundsInParent())){
                    pane.getChildren().remove(bullet);  
                  }
                }

                if(bullet.getCenterX() > length || bullet.getCenterX() == 0 || bullet.getCenterY() == 0 || bullet.getCenterY() > length)
                  pane.getChildren().remove(bullet);

                if(bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                  pane.getChildren().remove(bullet);
                  live.setWidth(live.getWidth() - 2);
                  if((int)live.getWidth() == 0){
                    int n = 500;
                    while(n != 0) {
                      if(n == 1) {
                        score_1++;
                        label_1.setText("Red: " + score_1);
                        enemy.setX(30);
                        enemy.setY(30);
                        live.setWidth(100);
                      }
                      n--;
                    }
                  }   
                }
            }
        };
        timer.start();   
    }

    //Shoot method for BotPlayer
    public void shootBot(ImageView img, ImageView enemy, Rectangle live){
       Bullet bullet = new Bullet(img.getX() + 30/2, img.getY() + 30/2, 2);
       pane.getChildren().add(bullet);

       AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                switch(direction_2) {
                  case 5: bullet.setCenterX(bullet.getCenterX() + 5); break;
                  case 6: bullet.setCenterX(bullet.getCenterX() - 5); break;
                  case 7: bullet.setCenterY(bullet.getCenterY() - 5); break;
                  case 8: bullet.setCenterY(bullet.getCenterY() + 5); break;
                }

                for (ImageView wall : walls) {
                  if(bullet.getBoundsInParent().intersects(wall.getBoundsInParent())){
                    pane.getChildren().remove(bullet); 
                  }
                }

                if(bullet.getCenterX() > length || bullet.getCenterX() == 0 || bullet.getCenterY() == 0 || bullet.getCenterY() > length)
                  pane.getChildren().remove(bullet);

                if(bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                  pane.getChildren().remove(bullet);
                  live.setWidth(live.getWidth() - 2);
                  if((int)live.getWidth() == 0){
                    int n = 500;
                    while(n != 0) {
                      if(n == 1) {
                        score_2++;
                        label_2.setText("Blue: " + score_2);
                        enemy.setX(length - 30);
                        enemy.setY(length - 30);
                        live.setWidth(100);
                      }
                      n--;
                    }
                  }   
                }
            }
        };
        timer.start();  
    }
}

//Thread class
class Move implements Runnable {
  private ImageView player;
  private Pane pane;

  public Move(ImageView player, Pane pane) {
    this.player = player;
    this.pane = pane;
  }

  public void run() {
    Platform.runLater(() -> {
      pane.getChildren().add(player);
    });
  }
}