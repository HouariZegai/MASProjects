package sender;

import agents.Agents;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SenderController implements Initializable {
    @FXML
    private VBox root;
    @FXML
    private Label lblAgentName;
    @FXML
    private JFXTextField fieldMsgSend;

    private JFXSnackbar toastErroMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toastErroMsg = new JFXSnackbar(root);

        lblAgentName.setText(Agents.senderAgent.getLocalName());
    }

    @FXML
    private void onSend() {
        if(fieldMsgSend.getText().isEmpty()) {
            toastErroMsg.show("Please type message to send it !", 2000);
            return;
        }

        Agents.senderAgent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent(Agents.senderAgent.getLocalName() + " said: " + fieldMsgSend.getText().trim());
                AID receiverAgent = new AID("Mohammed", AID.ISLOCALNAME);
                msg.addReceiver(receiverAgent);
                Agents.senderAgent.send(msg);
            }
        });
    }
}
