import agents.Agents;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent senderView = FXMLLoader.load(getClass().getResource("/sender/Sender.fxml"));
            Stage stageSender = new Stage();
            stageSender.setScene(new Scene(senderView));
            stageSender.setTitle("Sender Agent");
            stageSender.setX(50d);
            stageSender.setY(20d);
            stageSender.show();
            stageSender.setOnCloseRequest(e -> {
                Agents.senderAgent.doDelete();
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            Parent receiverView = FXMLLoader.load(getClass().getResource("/receiver/Receiver.fxml"));
            Stage stageReceiver = new Stage();
            stageReceiver.setScene(new Scene(receiverView));
            stageReceiver.setTitle("Receiver Agent");
            stageReceiver.setX(600d);
            stageReceiver.setY(50d);
            stageReceiver.show();
            stageReceiver.setOnCloseRequest(e -> {
                agents.Agents.receiverAgent.doDelete();
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initAgents();
        launch(args);
    }

    public static void initAgents() {
        Runtime runtime = Runtime.instance();

        Properties properties = new ExtendedProperties();
        properties.setProperty(Profile.GUI, "true");

        Profile profile = new ProfileImpl(properties);

        AgentContainer mainContainer = runtime.createMainContainer(profile);

        try {
            mainContainer.acceptNewAgent("Houari", Agents.senderAgent).start();
            mainContainer.acceptNewAgent("Mohammed", Agents.receiverAgent).start();
        } catch (ControllerException ce) {
            ce.printStackTrace();
        }

    }
}
