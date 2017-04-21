import view.MainFrame;

public class BDPMClient {
    private MainFrame mainFrame;
    public static void main(String[] args) {
        BDPMClient client = new BDPMClient();
        client.initview();


    }

    public void initview(){
        mainFrame = new MainFrame();
    }

    public void initControllers(){

    }
}
