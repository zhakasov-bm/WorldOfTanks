public class InvalidMapException extends Exception{
    public InvalidMapException(String string){
      super("" + string);
    }
}