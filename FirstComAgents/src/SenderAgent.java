import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;

public class SenderAgent extends Agent {

    public class MyGui extends JFrame {
        JLabel lblName;
        JButton btnSend, btnKill;

        public MyGui () {
            lblName = new JLabel("I'm the sender Agent, my name is: " + getLocalName());
            btnSend = new JButton("Send Msg");
            btnKill = new JButton("Kill me");

            btnSend.addActionListener(e -> {
                addBehaviour(new SenderBehaviour());
            });

            btnKill.addActionListener(e -> {
                doDelete();
                this.dispose();
            });

            JPanel panelBtns = new JPanel();
            panelBtns.setLayout(new FlowLayout());
            panelBtns.add(btnSend);
            panelBtns.add(btnKill);

            Container container = getContentPane();
            container.setLayout(new FlowLayout());
            container.add(lblName);
            container.add(panelBtns);

            this.setTitle("Agent: " + getLocalName());
            this.setBounds(100, 30, 300, 300);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    @Override
    protected void setup() {
        new MyGui().setVisible(true);
    }

    public class SenderBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Hi, i'm " + getLocalName() + " i send to you this message !");
            msg.addReceiver(new AID("mohamed", AID.ISLOCALNAME));
            send(msg);
        }
    }
}
