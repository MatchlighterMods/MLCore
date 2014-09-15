package ml.core.vec;

/**
 * Generic Matrix class
 * @author Matchlighter
 */
public class Matrix {

	public final int M; //Rows
	public final int N; //Columns

	public final double[][] matr;

	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		matr = new double[M][N];
	}

	public Matrix(double[][] data) {
		M = data.length;
		N = data[0].length;
		this.matr = new double[M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.matr[i][j] = data[i][j];
	}

	public Matrix(Matrix A) { this(A.matr); }

	public static Matrix identity(int N) {
		Matrix I = new Matrix(N, N);
		for (int i = 0; i < N; i++)
			I.matr[i][i] = 1;
		return I;
	}

	public Matrix transpose() {
		Matrix A = new Matrix(N, M);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				A.matr[j][i] = this.matr[i][j];
		return A;
	}

	public Matrix add(Matrix B) {
		if (B.M != this.M || B.N != this.N) throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.setAt(i, j, getAt(i, j) + B.getAt(i, j));
		
		return C;
	}


	public Matrix minus(Matrix B) {
		if (B.M != this.M || B.N != this.N) throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.setAt(i, j, getAt(i, j) - B.getAt(i, j));
		
		return C;
	}

	public boolean equals(Matrix B) {
		if (B.M != this.M || B.N != this.N) throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				if (this.matr[i][j] != B.matr[i][j]) return false;
		
		return true;
	}

	public Matrix mult(Matrix B) {
		if (this.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
		
		Matrix C = new Matrix(M, B.N);
		
		for (int i = 0; i < this.M; i++)
			for (int j = 0; j < B.N; j++)
				for (int k = 0; k < this.N; k++)
					C.matr[i][j] += (this.matr[i][k] * B.matr[k][j]);
		
		return C;
	}
	
	public Matrix mult(double d) {
		Matrix C = new Matrix(M, N);
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.setAt(i, j, getAt(i, j)*d);
		
		return C;
	}

	public Matrix clip(int offm, int offn, int m, int n) {
		if (offm+m > M || offn+n > N) throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(m, n);
		
		for (int i=0; i<m; i++)
			for (int j=0; j<n; j++)
				C.matr[i][j] = this.matr[offm+i][offn+j];
				
		return C;
	}
	
	public Matrix minor(int exR, int exC) {
		Matrix C = new Matrix(M-1, N-1);
		int r = -1;
		for (int i=0; i<M; i++) {
			if (i == exR) continue;
			r++;
			int c = -1;
			for (int j=0; j<N; j++) {
				if (j == exC) continue;
				c++;
				C.setAt(r, c, getAt(i, j));
			}
		}
		return C;
	}
	
	public void setAt(int r, int c, double v) {
		matr[r][c] = v;
	}
	
	public double getAt(int r, int c) {
		return matr[r][c];
	}
	
	private double getWrap(int r, int c) {
		return matr[r%M][c%N];
	}
	
	public double det() throws NonSquareException {
		if (M != N) throw new NonSquareException();
		
		if (M == 1) {
			return getAt(0,0);
		} else if (M == 2) {
			return (getAt(0, 0) * getAt(1, 1)) - (getAt(0, 1) * getAt(1, 0));
		} else {
			double det = 0;
			for (int offset=0; offset<M; offset++) {
				double a = 1, b = 1;
				for (int rc=0; rc<N; rc++) {
					a *= getWrap(rc, rc+offset);
					b *= getWrap((M-1-rc), rc+offset);
				}
				det += a - b;
			}
			
			return det;
		}
	}
	
	private int changeSign(int x) {
		if (x%2 == 0) return 1;
		return -1;
	}
	
	public Matrix cofactor() {
		Matrix C = new Matrix(M, N);
		for (int i=0; i<M; i++) {
			for (int j=0; j<N; j++) {
				C.setAt(i, j, changeSign(i) * changeSign(j) * minor(i,j).det());
			}
		}
		return C;
	}
	
	public Matrix inverse() throws NonSquareException {
		return cofactor().transpose().mult(1.0D / det());
	}
	
	@Override
	public String toString() {
		String str = "[";
		for (int i=0; i<M; i++) {
			str += "[";
			for (int j=0; j<N; j++) {
				str += getAt(i, j) + ", ";
			}
			str += "], ";
		}
		str += "]";
		return str;
	}
	
	public class NonSquareException extends RuntimeException {}

}
