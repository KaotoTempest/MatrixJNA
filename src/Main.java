import com.sun.jna.*;
import core.geometry.MutableVector;
import core.geometry.Rule;
import core.geometry.Variable;
import core.geometry.rules.FixedRule;
import core.geometry.rules.LineLengthRule;
import core.geometry.rules.OrthoRule;
import core.geometry.rules.ParallelLinesRule;
import core.objects.Line;
import core.rendering.DrawingComponent;
import lib.matrix.Functions;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 */
public class Main {

    private static final Functions fn = Native.load("Matrix", Functions.class);

    private static final java.util.List<Rule> rules = new LinkedList<>();

    public static void main(String[] args) {

        testMatrixDLL();

        JFrame frame = new JFrame();

        DrawingComponent dc = new DrawingComponent();

        frame.getContentPane().add(dc);

        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MutableVector j1 = new MutableVector(40, 40);
        MutableVector j2 = new MutableVector(80, 80);
        MutableVector j3 = new MutableVector(440, 40);
        MutableVector j4 = new MutableVector(40, 440);
        MutableVector j5 = new MutableVector(480, 120);
        MutableVector j6 = new MutableVector(80, 480);

        Line lA = new Line(j1, j3);
        Line lB = new Line(j1, j4);
        Line lC = new Line(j2, j5);
        Line lD = new Line(j2, j6);

        dc.add(lA, lB, lC, lD);
        dc.add(j1, j2, j3, j4, j5, j6);

        rules.add(new ParallelLinesRule(lA, lC));
        rules.add(new ParallelLinesRule(lB, lD));
        rules.add(new LineLengthRule(lA, lA.getVector().length()));
        rules.add(new LineLengthRule(lB, lB.getVector().length()));
        rules.add(new LineLengthRule(lC, lC.getVector().length()));
        rules.add(new LineLengthRule(lD, lD.getVector().length()));
        rules.add(new OrthoRule(lB));
        rules.add(new OrthoRule(lA));
        rules.add(new FixedRule(j1.getVariables().get(0)));
        rules.add(new FixedRule(j1.getVariables().get(1)));
        rules.add(new FixedRule(j2.getVariables().get(0)));
        rules.add(new FixedRule(j2.getVariables().get(1)));

        Set<Variable> variables = new LinkedHashSet<>();
        variables.addAll(lA.getVariables());
        variables.addAll(lB.getVariables());
        variables.addAll(lC.getVariables());
        variables.addAll(lD.getVariables());

//        variables.removeAll(j1.getVariables());
//        variables.removeAll(j2.getVariables());

        List<Variable> vars = new ArrayList<>(variables);

        QMatrix m = new QMatrix(12);

        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                m.set(x, y, rules.get(y).partial(vars.get(x)));
            }
        }

        System.out.println(m);
        System.out.println(m.determinant());
        System.out.println();
        System.out.println(m.invert());

        frame.setVisible(true);
    }

    private static void testMatrixDLL() {
        Functions.Matrix a = new Functions.Matrix(2, 2);
        a.a.write(0, new double[]{1, 2, 3, 4}, 0, 4);

        a.toString();

        Functions.Matrix b = new Functions.Matrix(2, 2);
        b.a.write(0, new double[]{1, 2, 3, 4}, 0, 4);

        Functions.Matrix c = fn.add(a, b);

        Functions.Matrix d = fn.multiply(a, b);

        Functions.Matrix e = fn.invert(a);

        Functions.Matrix f = fn.transpose(a);

        Functions.Matrix mA = new Functions.Matrix(3, 3);
        mA.a.write(0, new double[]{1, 2, 3, 0, 5, 0, 3, 2, 1}, 0, 9);

        Functions.Matrix mB = fn.minor(mA, 0, 0);

        Functions.Matrix mC = fn.invert(mA);

        double z = fn.determinant(mA);

        Functions.Matrix m23 = new Functions.Matrix(2, 3);
        m23.a.write(0, new double[]{1, 2, 3, 4, 5, 6}, 0, 6);
        Functions.Matrix m32 = new Functions.Matrix(3, 2);
        m32.a.write(0, new double[]{1, 2, 3, 4, 5, 6}, 0, 6);

        Functions.Matrix m22 = fn.multiply(m23, m32);
        Functions.Matrix m33 = fn.multiply(m32, m23);
    }

    private static class QMatrix {
        private static final Functions fn = Native.load("Matrix", Functions.class);

        private final int n;
        private final double[] a;

        public QMatrix(int n) {
            this.n = n;
            this.a = new double[n*n];

            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    set(i, j, i == j ? 1 : 0);
        }

        private QMatrix(int n, double[] a){
            this.n = n;
            this.a = a;
        }

        public void set(int x, int y, double v) {
            a[x + y * n] = v;
        }

        public double determinant(){
            Functions.Matrix m = new Functions.Matrix(n, n);
            m.a.write(0, a, 0, n * n);
            return fn.determinant(m);
        }

        public QMatrix invert(){
            Functions.Matrix m = new Functions.Matrix(n, n);
            m.a.write(0, a, 0, n * n);
            Functions.Matrix i = fn.invert(m);

            QMatrix q = new QMatrix(n);
            i.a.read(0,q.a,0, n * n);
            return q;
        }

        @Override public String toString() {
            StringBuilder b = new StringBuilder();

            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    b.append(String.format("%8.3f\t", a[x + y * n]));
                }
                b.append('\n');
            }

            return b.toString();
        }
    }

    //    private static class Arc extends DrawnObject implements JointedObject {
//        @NotNull private Line a;
//        @NotNull private Line b;
//
//        private Arc(@NotNull Line a, @NotNull Line b){
//            this.a = a;
//            this.b = b;
//        }
//    }

//    private static class Arc extends DrawnObject implements JointedObject {
//        private MutableVector centre;
//        private double radius;
//        private double sweep;
//        private double start;
//
//        public Arc(@NotNull Main.MutableVector centre, double radius, double sweep, double start) {
//            this.centre = centre;
//            this.radius = radius;
//            this.sweep = sweep;
//            this.start = start;
//        }
//
//        @NotNull public Main.MutableVector getCentre() {
//            return centre;
//        }
//
//        public void setCentre(@NotNull Main.MutableVector centre) {
//            this.centre = centre;
//        }
//
//        public double getSweep() {
//            return sweep;
//        }
//
//        public void setSweep(double sweep) {
//            this.sweep = sweep;
//        }
//
//        public double getStart() {
//            return start;
//        }
//
//        public void setStart(double start) {
//            this.start = start;
//        }
//
//        public double getRadius() {
//            return radius;
//        }
//
//        public void setRadius(double radius) {
//            this.radius = radius;
//        }
//
//        @Override public void draw(@NotNull Graphics2D g) {
//            super.draw(g);
//
//            Arc2D arc = new Arc2D.Double(centre.getX() - radius, centre.getY() - radius, radius * 2, radius * 2, start, sweep, Arc2D.OPEN);
//
//            g.draw(arc);
//        }
//
//        @Override public Collection<MutableVector> getJoints() {
//            return null;
//        }
//    }

}
