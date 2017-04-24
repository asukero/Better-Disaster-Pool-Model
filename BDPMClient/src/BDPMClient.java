import controllers.MFController;
import model.ClientModel;
import view.MainFrame;

public class BDPMClient {

    private MainFrame mainFrame;
    private MFController MFController;
    private ClientModel clientModel;

    public static void main(String[] args) {

        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("[*] Please enter 2 arguments");
            } else {
                System.setProperty("javax.net.ssl.trustStore", args[0]);
                System.setProperty("javax.net.ssl.trustStorePassword", args[1]);
                BDPMClient client = new BDPMClient();
                client.initView();
                client.clientModel = new ClientModel();
                client.initControllers();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
            System.out.println("Usage:\n" +
                    "\tjava -jar BDPMClient.jar [jks cert path] [jks password]\n");
        }
    }

    public void initView(){
        mainFrame = new MainFrame();
    }

    public void initControllers(){
        MFController = new MFController(clientModel);
        mainFrame.getConnectButton().addMouseListener(MFController);
        mainFrame.getDisconnectButton().addMouseListener(MFController);
        mainFrame.getHelpButton().addMouseListener(MFController);
        mainFrame.getRegisterButton().addMouseListener(MFController);
        mainFrame.getLoginButton().addMouseListener(MFController);
        mainFrame.getIpField().getDocument().addDocumentListener(MFController);
        mainFrame.getPortField().getDocument().addDocumentListener(MFController);
        mainFrame.getHelperButton().addMouseListener(MFController);
    }
}
