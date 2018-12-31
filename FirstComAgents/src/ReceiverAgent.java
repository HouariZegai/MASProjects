import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;

public class ReceiverAgent extends Agent {
    JLabel lblMsgReceived;

    public class MyGui extends JFrame {
        JLabel lblName;
        JButton btnKill;

        public MyGui () {
            lblName = new JLabel("I'm the receiver Agent, my name is: " + getLocalName());
            btnKill = new JButton("Kill me");
            lblMsgReceived = new JLabel("Msg Received: Not Yet !");

            btnKill.addActionListener(e -> {
                doDelete();
                this.dispose();
            });

            Container container = getContentPane();
            container.setLayout(new FlowLayout());
            container.add(lblName);
            container.add(lblMsgReceived);
            container.add(btnKill);

            this.setTitle("Agent: " + getLocalName());
            this.setBounds(410, 30, 300, 300);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    @Override
    public void setup() {
        new MyGui().setVisible(true);
        addBehaviour(new ReveiverBehaviour());
    }

    public class ReveiverBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();

            if(msg != null) {
                String msgReceived = msg.getContent();
                lblMsgReceived.setText("Msg Received: " + msgReceived);
            } else {
                block();
            }
        }
    }
}
