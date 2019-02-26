import java.io.File;

public class Solver {
    private double x0;
    private double y0;
    private long driverPointer;

    static {
        File lib = new File("lib/liblvs.so");
        System.load(lib.getAbsolutePath());
    }

    public Solver(EquationsCoefficients coefs, XYValues initial) {
        this.x0 = initial.getX();
        this.y0 = initial.getY();
        this.driverPointer = newDriver(coefs.getA(), coefs.getB(), coefs.getC(), coefs.getD());
    }

    public XYValues evaluateAt(double t) {
        long valuesPointer = getValues(driverPointer, x0, y0, t);
        XYValues r = new XYValues(getX(valuesPointer), getY(valuesPointer));
        freeValues(valuesPointer);
        return r;
    }

    public void free() {
        freeDriver(driverPointer);
    }

    private static native long newDriver(double a, double b, double c, double d);
    private static native void freeDriver(long solverPointer);
    private static native long getValues(long driverPointer, double x, double y, double t);
    private static native void freeValues(long resultPointer);
    private static native double getX(long resultPointer);
    private static native double getY(long resultPointer);

}
