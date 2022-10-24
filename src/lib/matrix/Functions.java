package lib.matrix;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.text.DecimalFormat;

public interface Functions extends Library {

    Matrix multiply(Matrix a, Matrix b);

    Matrix add(Matrix a, Matrix b);

    Matrix invert(Matrix m);

    Matrix transpose(Matrix m);

    double determinant(Matrix m);

    Matrix minor(Matrix m, int x, int y);

    @Structure.FieldOrder({"cols", "rows", "a"})
    class Matrix extends Structure implements Structure.ByValue {
        public int cols;
        public int rows;
        public Pointer a;

        /**
         * Required for the JNA
         */
        public Matrix() {
            this(1, 1);
        }

        public Matrix(int rows, int cols) {
            this.cols = cols;
            this.rows = rows;

            a = new Memory(rows * cols * 8L);
        }

        public double[] get() {
            return a.getDoubleArray(0, rows * cols);
        }

        @Override public String toString() {
            StringBuilder b = new StringBuilder();
            b.append('{');
            for (int i = 0; i < rows; i++) {
                if (i > 0) b.append(' ');
                StringBuilder rb = new StringBuilder();
                rb.append('{');
                for (int j = 0; j < cols; j++) {
                    if (j > 0) rb.append(' ');
                    double d = get()[j + i * cols];
                    rb.append(new DecimalFormat("#,##0.0##").format(d));
                }
                rb.append("}");
                b.append(rb.toString());
            }
            b.append('}');

            return String.format("%dx%d %s", rows, cols, b.toString());
        }
    }
}
