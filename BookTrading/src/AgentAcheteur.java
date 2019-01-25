import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.introspection.SuspendedAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sun.plugin2.message.Message;

public class AgentAcheteur extends Agent {
    // Title of book we want to buy it
    private String bookTitle;

    @Override
    protected void setup() {
        System.out.println("Acheteur " + getAID().getLocalName() + " est pret.");
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            bookTitle = args[0].toString();

            addBehaviour(new TickerBehaviour(this, 15000) {
                @Override
                protected void onTick() {
                    myAgent.addBehaviour(new EffectuerDemande(bookTitle));
                }
            });
        } else {
            System.out.println("Aucune livre trouvé");
            doDelete(); // remove agent
        }
    }

    @Override
    protected void takeDown() { // Nettoyer de l'agent
        System.out.println("Agent acheteur " + getAID().getLocalName() + " quitte la platetforme.");
    }
}

class EffectuerDemande extends Behaviour {

    private AID bestSeller;
    private int bestPrice;
    private int nbResponse = 0;
    private MessageTemplate msgTemp;
    private int step = 0;
    private DFAgentDescription[] sellerAgents;
    private String bookTitle;

    public EffectuerDemande(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Override
    public void action() {
        /* Search for Seller Agents */
        // Search for Seller has the service: vente-livre
        DFAgentDescription model = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("vente-livre");
        model.addServices(sd);
        try {
            sellerAgents = DFService.search(myAgent, model);
            System.out.println("Sellet Agent found !");
            for (int i = 0; i < sellerAgents.length; i++)
                System.out.println(sellerAgents[i].getName());

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        switch (step) {
            case 0: // Send CFP to all seller
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < sellerAgents.length; i++) {
                    cfp.addReceiver(sellerAgents[i].getName());
                }
                cfp.setContent(bookTitle);
                cfp.setConversationId("vente-livre");
                // placer un id unique, ici cfp + date en ms
                cfp.setReplyWith("cfp" + System.currentTimeMillis());
                myAgent.send(cfp); // Send CFP msg
                // define filter for reception
                msgTemp = MessageTemplate.and(MessageTemplate.MatchConversationId("vente-livre"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
                break;
            case 1: // Receive response from all sellers agents (PROPOSE or REFUSE)
                ACLMessage response = myAgent.receive();
                if (response != null) {
                    if (response.getPerformative() == ACLMessage.PROPOSE) {
                        int price = Integer.parseInt(response.getContent());
                        if (bestSeller == null || price < bestPrice) {
                            bestPrice = price;
                            bestSeller = response.getSender();
                        }
                    }
                    nbResponse++;

                    // If all response are received pass to step 2 (for accept book)
                    if (nbResponse >= sellerAgents.length)
                        step = 2;
                } else
                    block();
                break;
            case 2: // Send accept book to best seller
                ACLMessage commande = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                commande.addReceiver(bestSeller);
                commande.setContent(bookTitle);
                commande.setConversationId("vente-livre");
                commande.setReplyWith("Commande " + System.currentTimeMillis());
                myAgent.send(commande);

                // Prepare filter for next stêp
                msgTemp = MessageTemplate.and(MessageTemplate.MatchConversationId("vente-livre"),
                        MessageTemplate.MatchInReplyTo(commande.getReplyWith()));
                step = 3;
                break;
            case 3: // Receive confirmation of buying
                response = myAgent.receive(msgTemp);
                if(response != null) {
                    if(response.getPerformative() == ACLMessage.INFORM) { // Success buying book =D
                        System.out.println(bookTitle + " buying with success.");
                        System.out.println("Prix: " + bestPrice);

                        myAgent.doDelete();
                    }
                    step = 4;
                } else
                    block();
                break;
        }
    }

    @Override
    public boolean done() {
        if(step == 2 && bestSeller == null) {
            System.out.println("Buying impossible !");
        }

        return (step == 2 && bestSeller == null) || step == 4;
    }
}
