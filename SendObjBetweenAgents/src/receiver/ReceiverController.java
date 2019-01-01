package receiver;

import agents.Agents;
import com.jfoenix.controls.JFXTextField;
import dao.Person;
import dao.PersonDao;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReceiverController implements Initializable {
    @FXML
    private Label lblAgentName;
    @FXML
    private JFXTextField fieldQueryReceived;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblAgentName.setText(Agents.receiverAgent.getLocalName());

        Agents.receiverAgent.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                Agents.receiverAgent.doWait();

                ACLMessage msgReceived = Agents.receiverAgent.receive();
                if(msgReceived != null) {
                    String queryReceived = msgReceived.getContent();
                    System.out.println("Received Query: " + queryReceived);
                    fieldQueryReceived.setText(queryReceived);

                    // Get Person from Database
                    List<Person> persons = PersonDao.getPersonsFromQuery(queryReceived);

                    // Send number of person
                    ACLMessage msgNbrData = new ACLMessage(ACLMessage.INFORM);
                    msgNbrData.setContent(String.valueOf(persons.size()));
                    msgNbrData.addReceiver(Agents.senderAgent.getAID());
                    Agents.receiverAgent.send(msgNbrData);

                    // Send all person
                    for(Person p : persons) {
                        try {
                            ACLMessage msgDataOfPerson = new ACLMessage(ACLMessage.INFORM);
                            msgDataOfPerson.setContentObject(p);
                            msgDataOfPerson.addReceiver(Agents.senderAgent.getAID());
                            Agents.receiverAgent.send(msgDataOfPerson);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                    Agents.receiverAgent.doDelete();
                } else
                    block();
            }
        });
    }
}
