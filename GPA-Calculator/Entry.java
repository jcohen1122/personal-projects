/* Represents an entry into the calculator */
public class Entry {
	
	String course, grade;
	int credits;
	
	@Override
	public String toString() {
		return "Course: " + course + " \t"
				+ "Credits: " + credits + "\t"
				+ "Grade: " + grade;
	}

}
