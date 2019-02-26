import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.DoubleStringConverter;


public class SwitchesController {
    @FXML
    private Spinner<Double> aSpinner;

    @FXML
    private Spinner<Double> bSpinner;

    @FXML
    private Spinner<Double> cSpinner;

    @FXML
    private Spinner<Double> dSpinner;

    @FXML
    private Spinner<Double> predatorSpinner;

    @FXML
    private Spinner<Double> preySpinner;

    @FXML
    private Spinner<Double> stepSpinner;

    @FXML
    private Button resetButton;

    private ChartDataProvider provider;

    @FXML
    private void onReset() {
        provider.clear(stepSpinner.getValue(), new XYValues(predatorSpinner.getValue(), preySpinner.getValue()));
        onSpinnerChanged(null, null, null);
    }

    public void init() {
        setSpinnersProperties();
    }

    public void setProvider(ChartDataProvider provider) {
        this.provider = provider;
    }

    private void setSpinnersProperties() {
        setSingleSpinnerProperties(aSpinner, ChartDataProvider.defaultCoeficients.getA(), 0, Double.MAX_VALUE, 0.1);
        setSingleSpinnerProperties(bSpinner, ChartDataProvider.defaultCoeficients.getB(), 0, Double.MAX_VALUE, 0.05);
        setSingleSpinnerProperties(cSpinner, ChartDataProvider.defaultCoeficients.getC(), 0, 1, 0.1);
        setSingleSpinnerProperties(dSpinner, ChartDataProvider.defaultCoeficients.getD(), 0, Double.MAX_VALUE, 0.001);
        setSingleSpinnerProperties(predatorSpinner, ChartDataProvider.defaultXYValues.getX(), 0, Double.MAX_VALUE, 1);
        setSingleSpinnerProperties(preySpinner, ChartDataProvider.defaultXYValues.getY(), 0, Double.MAX_VALUE, 1);
        setSingleSpinnerProperties(stepSpinner, ChartDataProvider.defaultStep, Double.MIN_VALUE, Double.MAX_VALUE, 1);
        setSpinnersListeners();
    }

    private void setSpinnersListeners() {
        aSpinner.valueProperty().addListener(this::onSpinnerChanged);
        bSpinner.valueProperty().addListener(this::onSpinnerChanged);
        cSpinner.valueProperty().addListener(this::onSpinnerChanged);
        dSpinner.valueProperty().addListener(this::onSpinnerChanged);
    }

    private void setSingleSpinnerProperties(Spinner<Double> spinner, double defaultValue, double minValue, double maxValue, double step) {
        SpinnerValueFactory<Double> f = new SpinnerValueFactory.DoubleSpinnerValueFactory(minValue, maxValue, defaultValue, step);
        f.setConverter(new DoubleSpinnerValidatingConverter(spinner));
        spinner.setValueFactory(f);
        spinner.setEditable(true);
    }

    private void onSpinnerChanged(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
        provider.setNewSolver(new EquationsCoefficients(aSpinner.getValue(), bSpinner.getValue(), cSpinner.getValue(), dSpinner.getValue()));
    }

    private class DoubleSpinnerValidatingConverter extends DoubleStringConverter {
        private Spinner<Double> spinner;

        public DoubleSpinnerValidatingConverter(Spinner<Double> spinner) {
            this.spinner = spinner;
        }

        @Override
        public Double fromString(String value) {
            try {
                return super.fromString(value);
            } catch (Exception e) {
                Double lastValue = spinner.getValue();
                spinner.getEditor().setText(lastValue.toString());
                return lastValue;
            }

        }
    }
}

