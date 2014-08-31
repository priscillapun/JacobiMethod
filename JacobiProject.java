import java.util.Random;
import java.util.Arrays;

public class JacobiProject {
	private static double[] bijList = new double[0];
	private static double[] aijList = new double[0];

	public static void makeMatrixA(double[][] matrixA) {
		//generate symmetric matrix A
		for (int x = 0; x < matrixA.length; x++) {
			for (int y = 0 + x; y < matrixA[x].length; y++) {
				Random rand = new Random();
				matrixA[x][y] = rand.nextInt(1000);
				matrixA[y][x] = matrixA[x][y];
			}
		}
	}

	public static double[][] makeIterationA(double[][] matrixA) {
		aijsum(matrixA);
		//Find the off-diagonal of B with largest absoltute balue
		double elementB = 0;
		int elementBRow = 0;
		int elementBCol = 0;
		for (int i = 0; i < matrixA.length; i++) {
			for (int j = 0; j< matrixA[0].length; j++) {
				if (Math.abs(matrixA[i][j]) > Math.abs(elementB) && i < j && j != i) {
					elementB = matrixA[i][j];
					elementBRow = i;
					elementBCol = j;
				}

			}
		}
		System.out.println("B = " + elementB);

		double[][] considerA = new double[2][2];
		considerA[0][0] = matrixA[elementBRow][elementBRow];
		considerA[0][1] = matrixA[elementBRow][elementBCol];
		considerA[1][0] = matrixA[elementBCol][elementBRow];
		considerA[1][1] = matrixA[elementBCol][elementBCol];

		print("B", considerA);

		double a = considerA[0][0];
		double b = considerA[0][1];
		double c = considerA[1][0];
		double d = considerA[1][1];
		double eigenvalue1 = ((a+d)/2) + Math.sqrt(Math.pow(b, 2) + Math.pow(((a-d)/2),2));
		double eigenvalue2 = ((a+d)/2) - Math.sqrt(Math.pow(b, 2) + Math.pow(((a-d)/2),2));;

		double[] eigenvector1 = {(-considerA[0][1])/(considerA[0][0] - eigenvalue1), 1};
		double[] eigenvector2 = {(-considerA[0][1])/(considerA[0][0] - eigenvalue2), 1};
		double[] u1 = {eigenvector1[0] / (Math.sqrt((eigenvector1[0] * eigenvector1[0]) +
				(eigenvector1[1] * eigenvector1[1]))),
				eigenvector1[1] / (Math.sqrt((eigenvector1[0] * eigenvector1[0]) +
				(eigenvector1[1] * eigenvector1[1])))};
		double[] u2 = {eigenvector2[0] / (Math.sqrt((eigenvector2[0] * eigenvector2[0]) +
				(eigenvector2[1] * eigenvector2[1]))),
				eigenvector2[1] / (Math.sqrt((eigenvector2[0] * eigenvector2[0]) +
				(eigenvector2[1] * eigenvector2[1])))};
		System.out.println("eigenvalues: " + eigenvalue1 + " " + eigenvalue2);

		double[][] matrixG =  new double[matrixA.length][matrixA[0].length];
		for (int o = 0; o < matrixA.length; o ++) {
			for (int p = 0; p < matrixA[0].length; p ++) {
				if (o == p) {
					matrixG[o][p] = 1;
				} else {
					matrixG[o][p] = 0;
				}
			}
		}
		matrixG[elementBRow][elementBRow] = u1[0];
		matrixG[elementBRow][elementBCol] = u2[0];
		matrixG[elementBCol][elementBRow] = u1[1];
		matrixG[elementBCol][elementBCol] = u2[1];

		print("G(0," + (elementBRow + 1) + "," + (elementBCol + 1) + ")", matrixG);

		double[][] matrixGt = transpose(matrixG);
		//print("Gt(0," + elementBRow + "," + elementBCol + ")", matrixGt);

		double[][] iterationA = multiplyBy(multiplyBy(matrixGt, matrixA), matrixG);
		print("GtAG", iterationA);

		return iterationA;
	}

	public static void print(String name, double[][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			if (x == 0) {
				System.out.print(name + " = |");
			} else {
				for (int z = 0; z < name.length(); z++) {
					System.out.print(" ");
				}
				System.out.print("   |");
			}
				for (int y = 0; y < matrix[0].length; y++) {
					System.out.print("\t");
					System.out.print(Math.round(matrix[x][y]*1000.0)/1000.0);
				}
			System.out.println("\t|");
		}
	}

	public static void printAsInt(String name, double[][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			if (x == 0) {
				System.out.print(name + " = |");
			} else {
				for (int z = 0; z < name.length(); z++) {
					System.out.print(" ");
				}
				System.out.print("   |");
			}
				for (int y = 0; y < matrix[0].length; y++) {
					System.out.print("\t");
					System.out.print((int) (Math.round(matrix[x][y]*1000.0)/1000.0));
				}
			System.out.println("\t|");
		}
	}

	public static double bijsum(double[][] iterationA) {
		double sum = 0;
		for (int e = 0; e < iterationA.length; e++) {
			for (int y = 0; y <iterationA[0].length; y++) {
				if (e != y) {
					sum += iterationA[e][y] * iterationA[e][y];
				}
			}
		}
		
		addBijSum(sum);
		return sum;
	}

	public static double aijsum(double[][] iterationA) {
		double sum = 0;
		for (int e = 0; e < iterationA.length; e++) {
			for (int y = 0; y <iterationA[0].length; y++) {
				if (e != y) {
					sum += iterationA[e][y] * iterationA[e][y];
				}
			}
		}
		
		addAijSum(sum);
		return sum;
	}

	public static void addAijSum(double sum) {
		double[] newSum =  Arrays.copyOf(aijList, aijList.length + 1);
		newSum[newSum.length - 1] = sum;
		aijList = newSum;
	}

	public static void addBijSum(double sum) {
		double[] newSum =  Arrays.copyOf(bijList, bijList.length + 1);
		newSum[newSum.length - 1] = sum;
		bijList = newSum;
	}

	public static double[][] transpose(double[][] matrixB) {
		double[][] newMatrix = new double[matrixB.length][matrixB[0].length];
		for (int h = 0; h < newMatrix.length; h++) {
			for (int j = 0; j < newMatrix[0].length; j++) {
				newMatrix[h][j] = matrixB[j][h];
			}
		}
		return newMatrix;
	}

	public static double[][] multiplyBy(double[][] matrixB, double[][] matrixC) {
		double[][] newMatrix = new double[matrixB.length][matrixC[0].length];
		for (int u = 0; u < matrixB.length; u++) {
			for (int m =0; m < matrixC[0].length; m++) {
				newMatrix[u][m] = 0;
			}
		}
		for (int v = 0; v < matrixB.length; v++) {
			for (int f = 0; f < matrixC.length; f++) {
				double sum = 0;
				for (int d = 0; d < matrixC[0].length; d++) {
					sum += matrixB[v][d] * (double)matrixC[d][f];	
				}
				newMatrix[v][f] += sum;			
			}
		}
		return newMatrix;
	}

	public static void main(String[] args) {
		double[][] matrixA = new double[5][5];
		makeMatrixA(matrixA);
		
		printAsInt("A", matrixA);


		double[][] iteration = makeIterationA(matrixA);
		double bij = bijsum(iteration);
		System.out.println("|Bij| = " + bij);
		while (bij >= (Math.pow(10,-9))) {
			iteration = makeIterationA(iteration);
			bij = bijsum(iteration);
			System.out.println("|Bij| = " + bij);
			System.out.println("\n\n");
		}

		System.out.println("Iterations : Off(B) : Off(A)");
		for (int z  = 0; z < bijList.length; z ++) {
			System.out.println("   " + (z + 1) + "\t   " + bijList[z] + " \t" + aijList[0]);
		}


	}

}
