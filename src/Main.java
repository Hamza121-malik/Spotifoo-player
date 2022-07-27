public class Main {
    
    public static void main(String[] args){
        SpotifooView spotifooView = new SpotifooView();
        SpotifooModel spotifooModel = new SpotifooModel();
        SpotifooController spotifooController = new SpotifooController(spotifooModel, spotifooView);
         // Initialize the Spotifoo Player
        spotifooController.initialize();
    }
}
