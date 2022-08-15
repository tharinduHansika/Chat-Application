package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/server.fxml"));
        Scene server = new Scene(loader.load());
        primaryStage.setScene(server);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Chat Application");
        primaryStage.setResizable(false);

        // passing data via the controller
        try {
            ServerController controller = loader.getController();
            controller.initData(primaryStage);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        primaryStage.show();

    }
}
