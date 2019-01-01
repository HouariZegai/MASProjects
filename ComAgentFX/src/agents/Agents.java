package agents;

import jade.core.Agent;

public class Agents {
    public static Agent senderAgent, receiverAgent;

    static {
        Agents.senderAgent = new Agent() {
            @Override
            protected void setup() {

            }
        };
        Agents.receiverAgent = new Agent() {
            @Override
            protected void setup() {

            }
        };
    }
}
