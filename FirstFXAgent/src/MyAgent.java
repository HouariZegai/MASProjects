
import jade.core.*;

import jade.core.Runtime;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyAgent extends Application {
    static Agent myAgent;

    @Override
    public void start(Stage stage) {
        VBox vBox = new VBox();

        Label lblName = new Label("Hello, i'm the first agent my name is: " + myAgent.getLocalName());
        lblName.setStyle("-fx-font-size: 18px");

        Button btnKill = new Button("kill"); // For kill Agent
        btnKill.setStyle("-fx-background-color: #2196f3; -fx-text-fill: #FFF; -fx-font-size: 18px; -fx-pref-width: 80px; -fx-pref-height: 35px; -fx-cursor: hand");
        btnKill.setOnAction(e -> {
            myAgent.doDelete();
            Platform.exit();
        });

        vBox.getChildren().addAll(lblName, btnKill);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(15));

        stage.setScene(new Scene(vBox));
        stage.setTitle("Agent: " + myAgent.getLocalName());
        stage.show();
    }

    public static void main(String[] args) {
        myAgent = new Agent() {
            @Override
            public void setup() {
                System.out.println("Hello World!, my name is: " + getLocalName());
            }
        };

        Runtime runtime = Runtime.instance();

        Properties properties = new ExtendedProperties();
        properties.setProperty(Profile.GUI, "true");

        Profile profile = new ProfileImpl(properties);

        AgentContainer mainContainer = runtime.createMainContainer(profile);

        try {
            mainContainer.acceptNewAgent("Houari", myAgent).start();

            mainContainer.start();
        } catch (ControllerException ce) {
            ce.printStackTrace();
        }

        launch(args);
    }

}
