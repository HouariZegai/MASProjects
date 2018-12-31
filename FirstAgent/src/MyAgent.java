import jade.core.Agent;

import javax.swing.*;
import java.awt.*;

public class MyAgent extends Agent {

    public class MyGui extends JFrame {
        private JLabel lblName;
        private JButton btnKill;

        public MyGui() {
            lblName = new JLabel("Hello, i'm the first agent my name is: " + getLocalName());
            btnKill = new JButton("Kill");

            btnKill.addActionListener(e -> {
                doDelete();
                this.dispose();
            });

            Container container = getContentPane();
            container.setLayout(new FlowLayout());
            container.add(lblName);
            container.add(btnKill);

            this.setTitle("Agent: " + getLocalName());
            this.setBounds(100, 100, 300, 150);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    @Override
    protected void setup() {
        System.out.println("Hello, i'm the first agent my name is: " + getLocalName());
        new MyGui().setVisible(true);
    }
}