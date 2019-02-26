import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;

public class ChartController {
    private final static int animationStep = 250;

    @FXML
    private AreaChart<Double, Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private ChartDataProvider provider;

    public void setProvider(ChartDataProvider provider) {
        this.provider = provider;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        chart.getData().setAll(provider.getPredatorSeries(), provider.getPreySeries());
        xAxis.setLabel("time");
        yAxis.setLabel("population");
        startUpdateTask();
    }

    private void startUpdateTask() {
        Thread t = new Thread(newUpdateTask());
        t.setDaemon(true);
        t.start();
    }

    private Task<Void> newUpdateTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(true) {
                    Platform.runLater(() -> provider.evaluateSolver());
                    Thread.sleep(animationStep);
                }
            }
        };
    }

}
