import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Hashtable;

public class AgentVendeur extends Agent {
    // Catalogue of book u want to sell it
    private Hashtable catalogue;
    // GUI for add books
    //private VendeurGui myGui;

    @Override
    protected void setup() {
        catalogue = new Hashtable();
        //myGui = new VendeurGui(this);
        //myGui.affiche();

        /* Save services of agent in yellow page */
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        // Service
        ServiceDescription sd = new ServiceDescription();
        sd.setType("vente-livre"); // Type of service
        dfd.addServices(sd);

        try { // Add service to yellow page
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Ajouter du comportement de reponse a une demande d'offre
        addBehaviour(new ResponseDemandeOffre(catalogue));
        // Ajouter du comportement de reponse a une demande d'achat
        addBehaviour(new ResponseDemandeAchat(catalogue));
    }

    @Override
    protected void takeDown() {
        try { // Delete service from yellow page
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        //myGui.dispose();
        System.out.println("Vendeur: " + getAID().getLocalName() + " part.");
    }

    public void updateCatalogue(final String title, final int price) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                catalogue.put(title, new Integer(price));
                System.out.println(title + " insere au catalogue, prix: " + price);
            }
        });
    }
}

class ResponseDemandeOffre extends CyclicBehaviour {

    Hashtable catalogue; // Catalogue présent

    public ResponseDemandeOffre(Hashtable catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    public void action() {
        MessageTemplate msgTemp = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(msgTemp);
        if(msg != null) {
            String title = msg.getContent();

            /* Créer la réponse a la demande, le message est alors crée avec les champs appropriés
             (receiver, sender, in-reply-to, ...) */
            ACLMessage response = msg.createReply();
            Integer prix = (Integer) catalogue.get(title);
            if(prix != null) {
                // Le Livre disponible, reponse avec le prix
                response.setPerformative(ACLMessage.PROPOSE);
                response.setContent(String.valueOf(prix.intValue()));
            } else {
                // Le Livre pas disponible, => refuse
                response.setPerformative(ACLMessage.REFUSE);
                response.setContent("Indisponible");
            }
            myAgent.send(response);
        } else
            block();
    }
}

class ResponseDemandeAchat extends CyclicBehaviour {

    Hashtable catalogue; // Catalogue présent

    public ResponseDemandeAchat(Hashtable catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    public void action() {
        MessageTemplate msgTemp = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(msgTemp);
        if(msg != null) {
            String title = msg.getContent();

            ACLMessage response = msg.createReply();

            Integer prix = Integer.valueOf(catalogue.remove(title).toString());
            if(prix != null) {
                // Book disponible, send msg of confirmation
                response.setPerformative(ACLMessage.INFORM);
                System.out.println(title + " vendu a l'agent: " + msg.getSender().getName());
            } else {
                // Book buying to other client between this time
                response.setPerformative(ACLMessage.FAILURE);
                response.setContent("indisponible");
            }
            myAgent.send(response);

        } else
            block();
    }
}
