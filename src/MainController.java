import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ChartController chartController;

    @FXML
    private SwitchesController switchesController;

    @FXML
    public void initialize() {
        ChartDataProvider provider = new ChartDataProvider();
        chartController.setProvider(provider);
        chartController.init();
        switchesController.setProvider(provider);
        switchesController.init();
    }
}
