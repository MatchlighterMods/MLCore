package ml.core.vec;

/**
 * Generic Matrix class
 * @author Matchlighter
 */
public class Matrix {

	public final int M; //Rows
	public final int N; //Columns

	public double[][] matr;

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
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.matr[i][j] = this.matr[i][j] + B.matr[i][j];
		return this;
	}


	public Matrix minus(Matrix B) {
		if (B.M != this.M || B.N != this.N) throw new RuntimeException("Illegal matrix dimensions.");
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.matr[i][j] = this.matr[i][j] - B.matr[i][j];
		return this;
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
		
		double[][] d = new double[this.M][B.N];
		
		for (int i = 0; i < this.M; i++)
			for (int j = 0; j < B.N; j++)
				for (int k = 0; k < this.N; k++)
					d[i][j] += (this.matr[i][k] * B.matr[k][j]);
		
		matr = d;
		
		return this;
	}

	public Matrix clip(int offm, int offn, int m, int n) {
		if (offm+m > M || offn+n > N) throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(m, n);
		
		for (int i=0; i<m; i++)
			for (int j=0; j<n; j++)
				C.matr[i][j] = this.matr[offm+i][offn+j];
				
		return C;
	}

}
