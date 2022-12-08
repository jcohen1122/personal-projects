import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/* Class to calculate GPA */
public class Calculate {
	private static ArrayList<String> entries = new ArrayList<String>();

	/* Format entry into JSON */
	public static String format(String course, int credits, String grade) {
		return "\t\t{\n"
				+ "\t\t\"Course\"	:	\"" + course + "\",\n"
				+ "\t\t\"Credits\"	:	\"" + credits + "\",\n"
				+ "\t\t\"Grade\"		:	\"" + grade + "\",\n"
				+ "\t\t}";
	}

	/* Add entry to entries ArrayList */
	public static void push(String entry) {
		/* Only add if entry is not null and was not already added */
		if (entry != null && !entries.contains(entry)) {
			entries.add(entry);
		}
	}

	/* Remove entry from entries */
	public static void pop(String entry) {
		/* Remove only if entry is not null - will not do anything if not present */
		if (entry != null) {
			entries.remove(entry);
		}
	}

	/* Clear all entries */
	public static void clear() {
		entries = new ArrayList<String>();
	}

	/* Load a singular entry from JSON to object */
	public static Entry loadEntry(String entry) {
		if (entry != null) {

			try {
				Entry e = new Entry();
				entry = entry.substring(1);
				String[] parsed = entry.split(",", -1);

				String course = parsed[0].split(":", -1)[1];
				e.course = course.split("\"")[1];

				String credits = parsed[1].split(":", -1)[1];
				e.credits = Integer.valueOf(credits.substring(2, credits.length() - 1));

				String grade = parsed[2].split(":", -1)[1];
				e.grade = grade.substring(2, grade.length() - 1);

				return e;
			} catch (Exception e) {

			}
		}
		return new Entry();
	}

	/* Calculate cumulative GPA */
	public static double calculateCumulativeGPA(int cumulativeCredits, double cumulativeGPA) {
		/* Begin QPs with prior cumulatives */
		double qualityPoints = cumulativeCredits * cumulativeGPA;
		int credits = cumulativeCredits;
		double cumGPA = 0;

		for (String entry : entries) {
			Entry e = loadEntry(entry);
			qualityPoints += Grades.match(e.grade).qps * e.credits;
			credits += e.credits;
		}

		cumGPA = (double) qualityPoints / credits;

		return (double)Math.round(1000*cumGPA)/1000;
	}

	/* Prints all entries in json format */
	public static String jsonEntries() {
		StringBuffer allEntries = new StringBuffer();

		allEntries.append("{\n");
		allEntries.append("\t\"entries\":  [\n");
		for (int i = 0; i < entries.size(); i++) {
			if (i != entries.size() - 1) {
				allEntries.append(entries.get(i) + ",\n");
			} else {
				allEntries.append(entries.get(i) + "\n");
			}
		}
		allEntries.append("\t]\n");
		allEntries.append("}");

		return allEntries.toString();
	}

	/* Prints all entries in a readable format */
	public static String cleanEntries() {
		StringBuffer allEntries = new StringBuffer();
		int numClasses = 0, numCredits = 0;

		for (String entry : entries) {
			Entry e = loadEntry(entry);
			allEntries.append(e + "\n");

			numClasses++;
			numCredits += e.credits;
		}
		allEntries.append("Classes: " + numClasses + "\t");
		allEntries.append("Credits: " + numCredits + "\n");

		return allEntries.toString();
	}

	/* Load entries from a file */
	public static void load(String fromFile) throws IOException {
		File file = new File(fromFile);
		FileReader fr = new FileReader(file);

		/* Read in file */
		StringBuffer buff = new StringBuffer();
		int read = fr.read();

		while (read != -1) {
			buff.append((char)read);
			read = fr.read();
		}

		fr.close();

		/* Load in entries */
		String[] parsedEntries = buff.toString().split("\\[", -1)[1].split("}", -1);
		for (String entry : parsedEntries) {
			try {
				Entry e = loadEntry(entry);
				if (e.course != null || e.credits > 0 || e.grade != null) {
					push(format(e.course, e.credits, e.grade));
				}
			} catch (Exception e) {

			}
		}
	}

	/* Save entries to a file */
	public static void save(String toFile) throws IOException {
		File file = new File(toFile);
		FileWriter writer;

		file.createNewFile();
		writer = new FileWriter(file);

		writer.write(jsonEntries());

		writer.close();
	}

	/* Get copy of entries */
	public static ArrayList<Entry> getEntries() {
		ArrayList<Entry> objEntries = new ArrayList<>();

		for (String entry : entries) {
			objEntries.add(loadEntry(entry));
		}

		return objEntries;
	}

}
