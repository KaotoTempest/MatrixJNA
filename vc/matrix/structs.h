#pragma once

#define DLL __declspec(dllexport)

DLL typedef struct Matrix {
	int cols;
	int rows;

	double* a;
} Matrix;
