package sender;

import agents.Agents;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.Person;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SenderController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private Label lblAgentName;

    @FXML
    private JFXTextField fieldQuerySend;

    private JFXSnackbar toastErrorMsg;

    @FXML
    private JFXTreeTableView<TablePerson> tablePerson;

    private JFXTreeTableColumn<TablePerson, String> colName, colGender, colDateOfBirth;

    private ObservableList<TablePerson> dataOfTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblAgentName.setText(Agents.senderAgent.getLocalName());

        toastErrorMsg = new JFXSnackbar(root);
        initTablePerson();
        initDataTablePerson();

        // only for test
        fieldQuerySend.setText("SELECT * FROM Person;");
    }

    private void initTablePerson() {
        colName = new JFXTreeTableColumn<>("Name");
        colName.setPrefWidth(150d);
        colName.setCellValueFactory((JFXTreeTableColumn.CellDataFeatures<TablePerson, String> param) -> param.getValue().getValue().name);

        colGender = new JFXTreeTableColumn<>("Gender");
        colGender.setPrefWidth(80d);
        colGender.setCellValueFactory((JFXTreeTableColumn.CellDataFeatures<TablePerson, String> param) -> param.getValue().getValue().gender);

        colDateOfBirth = new JFXTreeTableColumn<>("Date of birth");
        colDateOfBirth.setPrefWidth(150d);
        colDateOfBirth.setCellValueFactory((JFXTreeTableColumn.CellDataFeatures<TablePerson, String> param) -> param.getValue().getValue().dataOfBirth);

        tablePerson.getColumns().addAll(colName, colGender, colDateOfBirth);
        tablePerson.setShowRoot(false);
    }

    private void initDataTablePerson() {
        dataOfTable = FXCollections.observableArrayList(); // init data of table
        final TreeItem treeItem = new RecursiveTreeItem<>(dataOfTable, RecursiveTreeObject::getChildren);
        try {
            tablePerson.setRoot(treeItem);
        } catch (Exception e) {
            System.out.println("Error catched !");
        }
    }

    private void addToTablePerson(Person person) {
        dataOfTable.add(new TablePerson(person.getName(), person.isMale(), person.getBirthdate()));
    }

    public class TablePerson extends RecursiveTreeObject<TablePerson> {
        StringProperty name;
        StringProperty gender;
        StringProperty dataOfBirth;

        public TablePerson(String name, boolean gender, Date dataOfBirth) {
            this.name = new SimpleStringProperty(name);
            this.gender = new SimpleStringProperty(gender ? "Male" : "Female");
            this.dataOfBirth = new SimpleStringProperty(new SimpleDateFormat("dd-mm-yyyy").format(dataOfBirth));
        }
    }

    @FXML
    private void onSend() {
        if (fieldQuerySend.getText().isEmpty()) {
            toastErrorMsg.show("Please type query to send it !", 2000);
            return;
        }
        Agents.senderAgent.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                Agents.receiverAgent.doWake(); // Activate receiver agent

                ACLMessage queryMsg = new ACLMessage(ACLMessage.INFORM);
                queryMsg.setContent(fieldQuerySend.getText().trim());
                queryMsg.addReceiver(Agents.receiverAgent.getAID());
                Agents.senderAgent.send(queryMsg);

                ACLMessage msgNbrPerson = Agents.senderAgent.receive();
                if (msgNbrPerson != null) {
                    int nbrPerson = Integer.parseInt(msgNbrPerson.getContent());
                    System.out.println("Sender Nbr Person: " + nbrPerson);

                    ACLMessage msgPerson;
                    while (nbrPerson > 0) {
                        msgPerson = Agents.senderAgent.receive();
                        if(msgPerson != null) {
                            try {
                                Person person = (Person) msgPerson.getContentObject();
                                System.out.println(person);
                                addToTablePerson(person);
                            } catch (UnreadableException ue) {
                                ue.printStackTrace();
                            }
                            nbrPerson--;
                        } else
                            block();

                    }
                    Agents.senderAgent.doDelete();
                } else
                    block();
            }
        });
    }
}
