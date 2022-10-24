#include "pch.h"
#include "functions.h"

#include <iostream>

Matrix operator*(Matrix m, double v) {
	Matrix r;
	r.cols = m.cols;
	r.rows = m.rows;
	r.a = new double[r.cols * r.rows];

	for (int i = 0; i < r.cols * r.rows; i++)
		r.a[i] = m.a[i] * v;

	return r;
}

class noninvertible_matrix_error : public std::logic_error {
public:
	noninvertible_matrix_error(const char* str) : logic_error(str) {}
};

class Invertor {
public:
	virtual Matrix invert(Matrix) = 0;
};

class CofactorInvertor : public Invertor {
public:
	Matrix cofactorInv(Matrix m) {
		Matrix r;
		r.cols = m.cols;
		r.rows = m.rows;
		r.a = new double[r.cols * r.rows];

		double d = determinant(m);

		if (d == 0) throw noninvertible_matrix_error("");

		for (int x = 0; x < m.cols; x++)
			for (int y = 0; y < m.rows; y++)
				r.a[x + y * m.cols] = ((x + y) % 2 == 0 ? 1 : -1) * determinant(minor(m, x, y));

		r = transpose(r);
		r = r * (1 / d);

		return r;
	}

	virtual Matrix invert(Matrix m) {
		std::cout << "Cofactor Method:\n";

		return cofactorInv(m);
	}
};

class GaussJordanInvertor : public Invertor {
public:
	void gaussJordanInv(Matrix a, Matrix b) {
		if (a.rows != b.rows)
			throw std::invalid_argument("Mismatched matrices");

		//if (Matrix::verbose) {
		//	std::cout << "\tPhase 0:\n";
		//	Matrix::printConjoined(a, b); std::cout << "\n";
		//}

		// Instead of swapping rows, we can just add the first row with a value in the correct column
		for (int y = 0; y < a.rows; y++) {
			// if the diagonal element for this row is zero then add on a row that has a non-zero element in the column //
			if (a.a[y + y * a.cols] == 0)
				for (int j = 0; j < a.rows; j++)
					if (y != j && a.a[j * a.cols + y] != 0) {
						for (int k = 0; k < a.cols; k++) a.a[y * a.cols + k] += a.a[j * a.cols + k];
						for (int k = 0; k < b.cols; k++) b.a[y * b.cols + k] += b.a[j * b.cols + k];
						break;
					}
		}

		//if (Matrix::verbose) {
		//	std::cout << "\tPhase 1:\n";
		//	Matrix::printConjoined(a, b); std::cout << "\n";
		//}

		// subtract rows
		for (int i = 0; i < a.rows; i++)
			for (int j = 0; j < a.rows; j++)
				if (i != j && a.a[j * a.cols + i] != 0) {
					double f = a.a[j * a.cols + i] / a.a[i * a.cols + i];
					for (int k = 0; k < a.cols; k++) a.a[j * a.cols + k] -= a.a[i * a.cols + k] * f;
					for (int k = 0; k < b.cols; k++) b.a[j * b.cols + k] -= b.a[i * b.cols + k] * f;
				}

		//if (Matrix::verbose) {
		//	std::cout << "\tPhase 2:\n";
		//	Matrix::printConjoined(a, b); std::cout << "\n";
		//}

		// scale each row by the diagonal //
		for (int i = 0; i < a.rows; i++) {
			double v = a.a[i * a.cols + i];
			for (int k = 0; k < a.cols; k++)  a.a[i * a.cols + k] /= v;
			for (int k = 0; k < b.cols; k++)  b.a[i * a.cols + k] /= v;
		}

		//if (Matrix::verbose) {
		//	std::cout << "\tPhase 3:\n";
		//	Matrix::printConjoined(a, b); std::cout << "\n";
		//}

		//		return b;
	}

	virtual Matrix invert(Matrix m) {
		std::cout << "Gauss-Jordan Elimination:\n";

		Matrix I;
		I.cols = m.cols;
		I.rows = m.rows;
		I.a = new double[I.cols * I.rows];

		gaussJordanInv(m, I);
		return I;
	}
};

DLL Matrix multiply(Matrix a, Matrix b) {
	if (a.cols != b.rows)
		throw std::invalid_argument("Matrix mistmatch");

	// cD = commond dimension //
	int cD = a.cols;

	int rows = a.rows;
	int cols = b.cols;

	Matrix c;
	c.rows = rows;
	c.cols = cols;
	c.a = new double[rows * cols];

	for(int x = 0; x < cols; x++)
		for (int y = 0; y < rows; y++) {
			double v = 0;
			for (int i = 0; i < cD; i++)
				v += a.a[i + y * a.cols] * b.a[x + i * b.cols];			
			c.a[x + y * cols] = v;
		}

	return c;
}

DLL Matrix add(Matrix a, Matrix b) {
	if (a.cols != b.cols || a.rows != b.rows)
		throw std::invalid_argument("Matrix mismatch");

	Matrix c;
	c.cols = a.cols;
	c.rows = a.rows;

	c.a = new double[a.cols * a.rows];

	for (int i = 0; i < a.cols * a.rows; i++)
		c.a[i] = a.a[i] + b.a[i];

	return c;
}

DLL Matrix invert(Matrix m) {
	if (m.cols != m.rows)
		throw std::invalid_argument("Non-square matrix");

	if (m.rows <= 3) {
		CofactorInvertor inv;
		return inv.invert(m);
	}
	GaussJordanInvertor inv;
	return inv.invert(m);
}

DLL Matrix transpose(Matrix m) {
	if (m.cols != m.rows)
		throw std::invalid_argument("Non-square matrix");

	Matrix r;
	r.cols = m.cols;
	r.rows = m.rows;

	r.a = new double[m.cols * m.rows];

	for (int x = 0; x < m.cols; x++)
		for (int y = 0; y < m.rows; y++)
			r.a[y + x * r.rows] = m.a[x + y * r.cols];

	return r;
}

DLL Matrix minor(Matrix m, int x, int y) {
	Matrix r;
	r.cols = m.cols - 1;
	r.rows = m.rows - 1;
	r.a = new double[r.cols * r.rows];

	int dx = 0;
	for (int i = 0; i < m.cols; i++)
		if (i != x) {
			int dy = 0;
			for (int j = 0; j < m.rows; j++)
				if (j != y) {
					r.a[dx + dy * r.cols] = m.a[i + j * m.cols];
					dy++;
				}
			dx++;
		}

	return r;
}

DLL double determinant(Matrix m) {
	if (m.cols != m.rows)
		throw std::invalid_argument("Non-square matrix"); 

	int w = m.cols;
	if (w == 1)
		return m.a[0];

	double v = 0;
	for (int i = 0; i < w; i++) {
		v += (i % 2 == 0 ? 1 : -1) * m.a[i] * determinant(minor(m, i, 0));
	}
	return v;

}