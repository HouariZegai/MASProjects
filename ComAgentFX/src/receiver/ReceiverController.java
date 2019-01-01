package receiver;

import agents.Agents;
import com.jfoenix.controls.JFXTextArea;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceiverController implements Initializable {
    @FXML
    private Label lblAgentName;
    @FXML
    private JFXTextArea areaMsgReceived;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblAgentName.setText(Agents.receiverAgent.getLocalName());

        Agents.receiverAgent.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = Agents.receiverAgent.receive();
                if(msg != null) {
                    areaMsgReceived.setText(msg.getContent());
                } else {
                    block();
                }
            }
        });
    }
}
