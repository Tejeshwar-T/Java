import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
public class StudentResultSystem {
    private static final String STUDENTS_FILE = "students.csv";
    private static final String GRADES_FILE = "grades.csv";
    private static final Map<String, Double> GRADE_POINTS;
    static {
        Map<String, Double> m = new HashMap<>();
        m.put("A+", 10.0);
        m.put("A", 9.0);
        m.put("B+", 8.0);
        m.put("B", 7.0);
        m.put("C", 6.0);
        m.put("D", 5.0);
        m.put("F", 0.0);
        GRADE_POINTS = Collections.unmodifiableMap(m);
    }
    private static final Map<String, Student> students = new TreeMap<>(); // studentId -> Student
    private static final List<CourseRecord> records = new ArrayList<>();
    public static void main(String[] args) {
        loadAll();
        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addStudent(sc); break;
                    case "2": addCourseRecord(sc); break;
                    case "3": editStudent(sc); break;
                    case "4": deleteStudent(sc); break;
                    case "5": listStudents(); break;
                    case "6": listStudentRecords(sc); break;
                    case "7": generateReportCard(sc); break;
                    case "8": saveAll(); System.out.println("Saved. Exiting."); sc.close(); return;
                    case "9": saveAll(); System.out.println("Data saved."); break;
                    default: System.out.println("Invalid choice. Try again."); 
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }
    private static void printMenu() {
        System.out.println("=== Student Result Management System ===");
        System.out.println("1) Add new student");
        System.out.println("2) Add course/grade record for a student");
        System.out.println("3) Edit student name");
        System.out.println("4) Delete student (and their records)");
        System.out.println("5) List all students");
        System.out.println("6) List all records for a student");
        System.out.println("7) Generate report card (semester GPA + CGPA)");
        System.out.println("8) Save and Exit");
        System.out.println("9) Save (stay)");
        System.out.print("Choose an option: ");
    }
    private static void addStudent(Scanner sc) throws IOException {
        System.out.print("Enter student ID (unique): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (students.containsKey(id)) {
            System.out.println("Student already exists with that ID.");
            return;
        }
        System.out.print("Enter full name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }
        Student s = new Student(id, name);
        students.put(id, s);
        System.out.println("Student added: " + s);
    }
    private static void editStudent(Scanner sc) {
        System.out.print("Enter student ID to edit: ");
        String id = sc.nextLine().trim();
        Student s = students.get(id);
        if (s == null) { System.out.println("Not found."); return; }
        System.out.println("Current name: " + s.name);
        System.out.print("Enter new name: ");
        String newName = sc.nextLine().trim();
        if (newName.isEmpty()) { System.out.println("No change."); return; }
        s.name = newName;
        System.out.println("Updated: " + s);
    }
    private static void deleteStudent(Scanner sc) {
        System.out.print("Enter student ID to delete: ");
        String id = sc.nextLine().trim();
        Student s = students.remove(id);
        if (s == null) { System.out.println("Not found."); return; }
        records.removeIf(r -> r.studentId.equals(id));
        System.out.println("Deleted student and their records: " + id);
    }
    private static void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("Students:");
        students.values().forEach(System.out::println);
    }
    private static void addCourseRecord(Scanner sc) {
        System.out.print("Enter student ID: ");
        String id = sc.nextLine().trim();
        if (!students.containsKey(id)) { System.out.println("Student not found. Add student first."); return; }
        System.out.print("Enter semester (e.g., S1, S2, 2024-1): ");
        String semester = sc.nextLine().trim();
        System.out.print("Course code (e.g., CS101): ");
        String code = sc.nextLine().trim();
        System.out.print("Course name: ");
        String cname = sc.nextLine().trim();
        System.out.print("Credit (e.g., 3): ");
        double credit;
        try { credit = Double.parseDouble(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("Invalid credit."); return; }
        System.out.print("Grade letter (A+, A, B+, B, C, D, F): ");
        String grade = sc.nextLine().trim().toUpperCase();
        if (!GRADE_POINTS.containsKey(grade)) { System.out.println("Invalid grade. Allowed: " + GRADE_POINTS.keySet()); return; }
        CourseRecord cr = new CourseRecord(id, semester, code, cname, credit, grade);
        records.add(cr);
        System.out.println("Record added: " + cr);
    }
    private static void listStudentRecords(Scanner sc) {
        System.out.print("Enter student ID: ");
        String id = sc.nextLine().trim();
        if (!students.containsKey(id)) { System.out.println("Student not found."); return; }
        List<CourseRecord> recs = records.stream()
                .filter(r -> r.studentId.equals(id))
                .sorted(Comparator.comparing((CourseRecord r) -> r.semester).thenComparing(r -> r.courseCode))
                .collect(Collectors.toList());
        if (recs.isEmpty()) { System.out.println("No records for this student."); return; }
        System.out.println("Records for " + id + " - " + students.get(id).name);
        for (CourseRecord r : recs) System.out.println("  " + r);
    }
    private static void generateReportCard(Scanner sc) {
        System.out.print("Enter student ID: ");
        String id = sc.nextLine().trim();
        Student s = students.get(id);
        if (s == null) { System.out.println("Student not found."); return; }
        Map<String, List<CourseRecord>> bySem = records.stream()
                .filter(r -> r.studentId.equals(id))
                .collect(Collectors.groupingBy(r -> r.semester));
        if (bySem.isEmpty()) { System.out.println("No academic records for student."); return; }
        List<String> semesters = new ArrayList<>(bySem.keySet());
        Collections.sort(semesters); // lexicographic; user-friendly enough if semesters named S1,S2...
        System.out.println("Semesters found: " + semesters);
        System.out.print("Enter semester to generate report for (or type ALL for full transcript): ");
        String semChoice = sc.nextLine().trim();
        if (semChoice.equalsIgnoreCase("ALL")) {
            double totalPoints = 0.0;
            double totalCredits = 0.0;
            System.out.println("\n===== Transcript for " + s.name + " (" + s.id + ") =====");
            for (String sem : semesters) {
                System.out.println("\n--- Semester: " + sem + " ---");
                List<CourseRecord> list = bySem.get(sem);
                printCourseTable(list);
                double semPoints = 0.0, semCredits = 0.0;
                for (CourseRecord cr : list) {
                    double gp = GRADE_POINTS.get(cr.gradeLetter);
                    semPoints += gp * cr.credit;
                    semCredits += cr.credit;
                }
                double semGPA = semCredits == 0 ? 0.0 : round(semPoints / semCredits, 3);
                System.out.println("Semester GPA: " + semGPA + "  (Credits: " + semCredits + ")");
                totalPoints += semPoints;
                totalCredits += semCredits;
            }
            double cgpa = totalCredits == 0 ? 0.0 : round(totalPoints / totalCredits, 3);
            System.out.println("\nCumulative CGPA: " + cgpa + "  (Total Credits: " + totalCredits + ")");
            System.out.println("============================================\n");
        } else {
            String sem = semChoice;
            List<CourseRecord> list = bySem.get(sem);
            if (list == null) { System.out.println("No records found for semester: " + sem); return; }
            printCourseTable(list);
            double semPoints = 0.0, semCredits = 0.0;
            for (CourseRecord cr : list) {
                double gp = GRADE_POINTS.get(cr.gradeLetter);
                semPoints += gp * cr.credit;
                semCredits += cr.credit;
            }
            double semGPA = semCredits == 0 ? 0.0 : round(semPoints / semCredits, 3);
            double totalPoints = semPoints;
            double totalCredits = semCredits;
            for (String osem : semesters) {
                if (osem.equals(sem)) continue;
                for (CourseRecord cr : bySem.get(osem)) {
                    totalPoints += GRADE_POINTS.get(cr.gradeLetter) * cr.credit;
                    totalCredits += cr.credit;
                }
            }
            double cgpa = totalCredits == 0 ? 0.0 : round(totalPoints / totalCredits, 3);
            System.out.println("\nSemester GPA: " + semGPA);
            System.out.println("Cumulative CGPA (all semesters): " + cgpa);
        }
    }
    private static void printCourseTable(List<CourseRecord> list) {
        System.out.printf("%-10s %-30s %-6s %-8s %-6s\n", "Code", "Course Name", "Credit", "Grade", "Points");
        System.out.println("---------------------------------------------------------------------");
        for (CourseRecord cr : list) {
            double gp = GRADE_POINTS.getOrDefault(cr.gradeLetter, 0.0);
            System.out.printf("%-10s %-30s %-6.2f %-8s %-6.2f\n",
                    cr.courseCode, cr.courseName, cr.credit, cr.gradeLetter, gp);
        }
    }
    private static void loadAll() {
        loadStudents();
        loadGrades();
    }
    private static void loadStudents() {
        students.clear();
        Path p = Paths.get(STUDENTS_FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                String id = parts[0].trim();
                String name = parts[1].trim();
                students.put(id, new Student(id, name));
            }
        } catch (IOException e) {
            System.out.println("Failed to load students: " + e.getMessage());
        }
    }

    private static void loadGrades() {
        records.clear();
        Path p = Paths.get(GRADES_FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                // studentId,semester,courseCode,courseName,credit,gradeLetter
                String[] parts = splitCsv(line, 6);
                if (parts.length < 6) continue;
                String sid = parts[0].trim();
                String sem = parts[1].trim();
                String ccode = parts[2].trim();
                String cname = parts[3].trim();
                double credit = Double.parseDouble(parts[4].trim());
                String grade = parts[5].trim().toUpperCase();
                records.add(new CourseRecord(sid, sem, ccode, cname, credit, grade));
            }
        } catch (IOException e) {
            System.out.println("Failed to load grades: " + e.getMessage());
        } catch (NumberFormatException ne) {
            System.out.println("Malformed credit value in grades.csv: " + ne.getMessage());
        }
    }
    private static void saveAll() {
        saveStudents();
        saveGrades();
    }
    private static void saveStudents() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(STUDENTS_FILE))) {
            for (Student s : students.values()) {
                bw.write(escapeCsv(s.id) + "," + escapeCsv(s.name));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save students: " + e.getMessage());
        }
    }
    private static void saveGrades() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(GRADES_FILE))) {
            for (CourseRecord r : records) {
                bw.write(String.join(",",
                        escapeCsv(r.studentId),
                        escapeCsv(r.semester),
                        escapeCsv(r.courseCode),
                        escapeCsv(r.courseName),
                        String.valueOf(r.credit),
                        escapeCsv(r.gradeLetter)
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save grades: " + e.getMessage());
        }
    }
    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
    private static String[] splitCsv(String line, int expected) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"'); i++; // double quote -> literal quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        while (out.size() < expected) out.add("");
        return out.toArray(new String[0]);
    }
    private static double round(double v, int places) {
        double factor = Math.pow(10, places);
        return Math.round(v * factor) / factor;
    }
    private static class Student {
        String id;
        String name;
        Student(String id, String name) { this.id = id; this.name = name; }
        public String toString() { return id + " : " + name; }
    }
    private static class CourseRecord {
        String studentId;
        String semester;
        String courseCode;
        String courseName;
        double credit;
        String gradeLetter;
        CourseRecord(String studentId, String semester, String courseCode, String courseName, double credit, String gradeLetter) {
            this.studentId = studentId;
            this.semester = semester;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credit = credit;
            this.gradeLetter = gradeLetter.toUpperCase();
        }
        public String toString() {
            return "[" + semester + "] " + courseCode + " - " + courseName + " (" + credit + "cr) : " + gradeLetter;
        }
    }
}


