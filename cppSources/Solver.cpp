#include "Solver.h"
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_odeiv2.h>
#include <iostream>

using namespace std;

int func (double t, const double y[], double f[], void *p)
{
  (void)(t);
  double* params = (double *)p;
  double a = *params;
  double b = *(params + 1);
  double c = *(params + 2);
  double d = *(params + 3);
  f[0] = -a*y[0] + c*d*y[0]*y[1];
  f[1] = b*y[1] - d*y[0]*y[1];
  return GSL_SUCCESS;
}

int jac (double t, const double y[], double *dfdy, double dfdt[], void *p)
{
  (void)(t);
  double* params = (double *)p;
  double a = *params;
  double b = *(params + 1);
  double c = *(params + 2);
  double d = *(params + 3);
  gsl_matrix_view dfdy_mat = gsl_matrix_view_array (dfdy, 2, 2);
  gsl_matrix * m = &dfdy_mat.matrix;
  gsl_matrix_set (m, 0, 0, -a + c*d*y[1]);
  gsl_matrix_set (m, 0, 1, c*d*y[0]);
  gsl_matrix_set (m, 1, 0, -d*y[1]);
  gsl_matrix_set (m, 1, 1, b - d*y[0]);
  dfdt[0] = 0.0;
  dfdt[1] = 0.0;
  return GSL_SUCCESS;
}

JNIEXPORT jlong JNICALL Java_Solver_newDriver(JNIEnv * env, jclass cl, jdouble a, jdouble b, jdouble c, jdouble d) {
    double* coefs = new double[4];
    coefs[0] = a;
    coefs[1] = b;
    coefs[2] = c;
    coefs[3] = d;
    cout << coefs << endl;
    gsl_odeiv2_system* sys = new gsl_odeiv2_system();
    sys -> function = func;
    sys -> jacobian = jac;
    sys -> dimension = 2;
    sys -> params = coefs;
    gsl_odeiv2_driver * driver = gsl_odeiv2_driver_alloc_y_new (sys, gsl_odeiv2_step_rk8pd, 1e-6, 1e-6, 0.0);
    return (long)driver;
}

JNIEXPORT void JNICALL Java_Solver_freeDriver(JNIEnv *, jclass cl, jlong driverPointer) {
    gsl_odeiv2_driver * driver = (gsl_odeiv2_driver*)driverPointer;
    gsl_odeiv2_driver_free (driver);
}

JNIEXPORT jlong JNICALL Java_Solver_getValues(JNIEnv * env, jclass cl, jlong driverPointer, jdouble x, jdouble y, jdouble time) {
    double t = 0;
    double* ys = new double[2];
    ys[0] = x;
    ys[1] = y;
    gsl_odeiv2_driver * driver = (gsl_odeiv2_driver*)driverPointer;
    gsl_odeiv2_driver_apply (driver, &t, time, ys);
    return (long) ys;
}

JNIEXPORT void JNICALL Java_Solver_freeValues(JNIEnv * env, jclass cl, jlong valuesPointer) {
    double* ys = (double*) valuesPointer;
    delete ys;
}

JNIEXPORT jdouble JNICALL Java_Solver_getX(JNIEnv * env, jclass cl, jlong valuesPointer) {
    double* ys = (double*) valuesPointer;
    return ys[0];
}

JNIEXPORT jdouble JNICALL Java_Solver_getY(JNIEnv * env, jclass cl, jlong valuesPointer) {
    double* ys = (double*) valuesPointer;
    return ys[1];
}
