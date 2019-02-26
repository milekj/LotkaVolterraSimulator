import javafx.scene.chart.XYChart;

public class ChartDataProvider {
    public final static EquationsCoefficients defaultCoeficients = new EquationsCoefficients(0.1, 0.75, 0.5, 0.002);
    public final static XYValues defaultXYValues = new XYValues(100.0, 100.0);
    public final static double defaultStep = 1;

    private XYChart.Series<Double, Double> predatorSeries;
    private XYChart.Series<Double, Double> preySeries;
    private Solver solver;
    private double step;
    private double currentTime;
    private XYValues lastXYValues;

    public ChartDataProvider() {
        currentTime = 0;
        step = defaultStep;
        lastXYValues = defaultXYValues;
        solver = new Solver(defaultCoeficients, defaultXYValues);
        setSeries();
    }

    public void evaluateSolver() {
        XYValues values = solver.evaluateAt(currentTime);
        predatorSeries.getData().add(new XYChart.Data<>(currentTime, values.getX()));
        preySeries.getData().add(new XYChart.Data<>(currentTime, values.getY()));
        lastXYValues = values;
        currentTime += step;
    }

    public void setNewSolver(EquationsCoefficients coefs) {
        this.solver.free();
        this.solver = new Solver(coefs, lastXYValues);
    }

    public void clear(double newStep, XYValues newValues) {
        predatorSeries.getData().clear();
        preySeries.getData().clear();
        currentTime = 0;
        step = newStep;
        lastXYValues = newValues;
    }

    public XYChart.Series<Double, Double> getPredatorSeries() {
        return predatorSeries;
    }

    public XYChart.Series<Double, Double> getPreySeries() {
        return preySeries;
    }

    private void setSeries() {
        predatorSeries = new XYChart.Series<>();
        preySeries = new XYChart.Series<>();
        predatorSeries.setName("Predator");
        preySeries.setName("Prey");
    }


}
