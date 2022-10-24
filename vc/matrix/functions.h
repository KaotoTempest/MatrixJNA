#pragma once

#include "structs.h"

extern "C" DLL Matrix _stdcall multiply(Matrix, Matrix);

extern "C" DLL Matrix _stdcall add(Matrix, Matrix);

extern "C" DLL Matrix _stdcall invert(Matrix);

extern "C" DLL Matrix _stdcall transpose(Matrix);

extern "C" DLL double _stdcall determinant(Matrix);

extern "C" DLL Matrix _stdcall minor(Matrix, int, int);