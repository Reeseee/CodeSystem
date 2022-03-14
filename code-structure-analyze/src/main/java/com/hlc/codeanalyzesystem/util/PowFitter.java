package com.hlc.codeanalyzesystem.util;

import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PowFitter extends AbstractCurveFitter {
    protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> points) {
        final int len = points.size();
        final double[] target  = new double[len];
        final double[] weights = new double[len];
        final double[] initialGuess = { 1.0, 1.0};

        int i = 0;
        for(WeightedObservedPoint point : points) {
            target[i]  = point.getY();
            weights[i] = point.getWeight();
            i += 1;
        }

        final AbstractCurveFitter.TheoreticalValuesFunction model = new
                AbstractCurveFitter.TheoreticalValuesFunction(new PowFunction(), points);

        return new LeastSquaresBuilder().
                maxEvaluations(Integer.MAX_VALUE).
                maxIterations(Integer.MAX_VALUE).
                start(initialGuess).
                target(target).
                weight(new DiagonalMatrix(weights)).
                model(model.getModelFunction(), model.getModelFunctionJacobian()).
                build();
    }

    public static String complexityFit(List<List<Double>> arr){
        PowFitter fitter = new PowFitter();
        ArrayList<WeightedObservedPoint> points = new ArrayList<WeightedObservedPoint>();

        // Add points here; for instance,
        for (List<Double> list : arr)
        {
            WeightedObservedPoint point = new WeightedObservedPoint(1.0,list.get(0), list.get(1));
            points.add(point);
        }

        final double coeffs[] = fitter.fit(points);
        return (Arrays.toString(coeffs));
    }

    public static void main(String[] args) {
        PowFitter fitter = new PowFitter();
        ArrayList<WeightedObservedPoint> points = new ArrayList<WeightedObservedPoint>();

        // Add points here; for instance,
        WeightedObservedPoint point = new WeightedObservedPoint(1.0,
                1.0,
                1.0);
        points.add(point);

        final double coeffs[] = fitter.fit(points);
        System.out.println(Arrays.toString(coeffs));
    }
}

