import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class LaunchAgent {

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance(); // Get jade runtime
        runtime.setCloseVM(true); // Exit the jvm when the are no more containers around

        Properties properties = new ExtendedProperties();
        properties.setProperty(Profile.GUI, "true");

        Profile profile = new ProfileImpl(properties); // Create default profile
        AgentContainer mainContainer = runtime.createMainContainer(profile);


        try {
            mainContainer.start();

            //AgentController myAgent = mainContainer.createNewAgent("Houari", MyAgent.class.getName(), null);
            AgentController myAgent = mainContainer.createNewAgent("Houari", "MyAgent", new Object[]{});
            myAgent.start();
        } catch (ControllerException ce) {
            ce.printStackTrace();
        }

    }
}
