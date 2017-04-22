import controllers.Controller;
import model.ClientModel;
import view.MainFrame;

public class BDPMClient {

    private MainFrame mainFrame;
    private Controller controller;
    private ClientModel clientModel;

    public static void main(String[] args) {
        BDPMClient client = new BDPMClient();
        client.initView();
        client.clientModel = new ClientModel();
        client.initControllers();
    }

    public void initView(){
        mainFrame = new MainFrame();
    }

    public void initControllers(){
        controller = new Controller(clientModel);
        mainFrame.getConnectButton().addMouseListener(controller);
        mainFrame.getDisconnectButton().addMouseListener(controller);
        mainFrame.getHelpButton().addMouseListener(controller);
        mainFrame.getIpField().getDocument().addDocumentListener(controller);
        mainFrame.getPortField().getDocument().addDocumentListener(controller);
    }
}
