/* The quality points assigned to each grade */
public enum Grades {
	AP(4.0), A(4.0), AM(3.7), 
	BP(3.3), B(3.0), BM(2.7), 
	CP(2.3), C(2.0), CM(1.7), 
	DP(1.3), D(1.0), DM(0.7), 
	F(0.0);

	double qps;

	Grades(double e) {
		qps = e;
	}

	/* Match string grade with enum grade */
	public static Grades match(String grade) {
		switch (grade) {
		case "A+":
			return AP;
		case "A":
			return A;
		case "A-":
			return AM;
		case "B+":
			return BP;
		case "B":
			return B;
		case "B-":
			return BM;
		case "C+":
			return CP;
		case "C":
			return C;
		case "C-":
			return CM;
		case "D+":
			return DP;
		case "D":
			return D;
		case "D-":
			return DM;
		case "F":
			return F;
		default:
			return null;
		}
	}
}
