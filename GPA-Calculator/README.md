<h1>Run Folder</h1>

<h2>Grade Calculator</h2>
The Grade Calculator file is an Unix executable file intended to execute the GradeCalc.jar file from the terminal. The benefit of doing this roundabout way of executing the jar file is because executing it from the terminal allows the program to have access to personal folders, which is needed when saving/loading a grade file. When the program is loaded just through the jar, the program does not have access to any folders.

<h2>GradeCalc.jar</h2>
This file will execute the program.

<h2>Fall 22 Grades.txt</h2>
This is a JSON text file containing all of my current grades for the Fall 22 semester. The format was created by saving through the program and not done by hand, and the file's purpose is to allow the testing of the save/load methods within the program.

<h1>Java Classes</h1>

<h2>Driver.java</h2>
This class contains the main method of the program, which launches a new instance of the program's UI.

<h2>GPA_UI.java</h2>
This class creates the UI of the program.
<br><br>
The UI contains many features, including:
<ul>
  <li>Base Frame</li>
  <ul>
    <li>Features a 3x6 default grid of text boxes, where rows correspond to each course, and columns correspond to the course name, the number of credits, and the final grade. The user enters their information into the grid, and the program pulls the information for later calculation.</li>
  </ul>
  <li>Calculate Button</li>
  <ul>
    <li>When this button is clicked, the GPA of the user is calculated based off of the information in the grid, and the information in the Cumulative GPA section of the pop up menu.</li>
  </ul>
  <li>Menu Button</li>
    <ul>
    <li>When this button is clicked, the pop up menu frame will appear.</li>
  </ul>
  <li>Menu Frame</li>
    <ul>
    <li>Features 6 buttons, each of which has their own functions.</li>
      <ol type = "1">
        <li>Load - loads a grade file into the course grid, allowing GPA calculation. Incorrect format will have no result. If the file has more courses than there are rows in the course grid, the user will be asked to add more courses to the grid.</li>
        <li>Save - saves the information stored in the course grid into a JSON grade file. Assumes user will enter .txt extension.</li>
        <li>Add Course - adds a row to the course grid, allowing the user to enter information for another course.</li>
        <li>Remove Course - removes a row from the course grid.</li>
        <li>Cumulative GPA - allows a user to enter their cumulative credits and cumulative GPA, allowing the calculation of a new cumulative GPA with the newly entered course information.</li>
        <li>Reset - clears the entire course grid and entered cumulative credit/GPA information.</li>
      </ol>
  </ul>
</ul>

<h2>Calculate.java</h2>
This class does the leg-work of calculating the user's GPA. Various methods are featured.
<br><br>

```java
public static String format(String course, int credits, String grade);
```
<ul>
  <li>This method formats a single row of course information into a JSON entry.</li>
</ul>

```java
public static void push(String entry);
```
<ul>
  <li>This method pushes a single course entry into the entries ArrayList. Assumes entry is in JSON.</li>
</ul>

```java
public static void pop(String entry);
```
<ul>
  <li>This method pops a single course entry from the entries ArrayList. Used to clear one user entry.</li>
</ul>

```java
public static void clear();
```
<ul>
  <li>This method resets the entire entries ArrayList. Used to clear all user entries.</li>
</ul>

```java
public static Entry loadEntry(String entry);
```
<ul>
  <li>This method takes a course entry, assumed to be in JSON, and constructs its Entry object counterpart, parsing information for each category such as name, credits, and grade to create instance variables.</li>
</ul>

```java
public static double calculateCumulativeGPA(int cumulativeCredits, double cumulativeGPA);
```
<ul>
  <li>This method uses prior cumulative credits and GPA, and combines them with the current entries to calculate a new GPA. If the user does not enter any cumulative information, they are defaulted to 0 and 0.0 respectively.</li>
</ul>

```java
public static String jsonEntries();
```
<ul>
  <li>This method takes each entry and compiles them into a finalized JSON format. Used to save to a file.</li>
</ul>

```java
public static String cleanEntries();
```
<ul>
  <li>This method compiles the Entry object counterpart of each string course entry.</li>
</ul>

```java
public static void load(String fromFile);
```
<ul>
  <li>This method parses through the JSON grade file, deconstructing it to access individual values and store them, and then reconstructing the format to create the entries ArrayList.</li>
</ul>

```java
public static void save(String toFile);
```
<ul>
  <li>This method writes the result of jsonEntries() into a new file, allowing for a successful load execution later on.</li>
</ul>

<h2>Entry.java</h2>
The object representation of a course entry. Features instance variables for class name, number of credits, and final grade.

<h2>Grades.java</h2>
The enum representation of all possible final grades for a course, ranging from A+ to F. The constructor assigns each grade the appropriate number of Quality Points per credit hour, which is used in the calculation of the user's GPA.

```java
public static Grades match(String grade);
```
<ul>
  <li>Because it is unreasonable to ask the user to provide input such as "Grades.A" for an A or "Grades.BM" for a B-, the user is allowed to enter the natural format of their grade. This function will take that input, and using a switch statement, assign it to the appropriate enum representation. This allows the program to access the amount of Quality Points per credit hour for that entry.</li>
</ul>
