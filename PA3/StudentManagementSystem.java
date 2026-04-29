import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Main system for managing students, academic members, departments,
 * programs, courses, and related data by reading from text files
 * and providing listing/reporting functionality.
 */
public class StudentManagementSystem {
    /** Map of student ID to Student objects */
    public static final TreeMap<String, Student> students = new TreeMap<>();
    /** Map of academic member ID to AcademicMember objects */
    public static final TreeMap<String, AcademicMember> academicMembers = new TreeMap<>();
    /** Map of department name to Department objects */
    public static final TreeMap<String, Department> departments = new TreeMap<>();
    /** Map of program code to Program objects */
    public static final TreeMap<String, Program> programs = new TreeMap<>();
    /** Map of course code to Course objects */
    public static final TreeMap<String, Course> courses = new TreeMap<>();

    /** File path for person data */
    String personstxtfilepath;
    /** File path for department data */
    String departmentstxtfilepath;
    /** File path for program data */
    String programtxtfilepath;
    /** File path for course data */
    String coursetxtfilepath;
    /** File path for assignment data */
    String assignmenttxtfilepath;
    /** File path for grades data */
    String gradestxtfilepath;

    /**
     * Adds a student to the system.
     *
     * @param student the Student object to add
     * @throws NonExistentStudent if the student is null
     */
    public static void AddStudent(Student student) throws NonExistentStudent {
        if (student == null) {
            throw new NonExistentException("Student is null");
        }
        students.put(student.getID(), student);
    }

    /**
     * Removes a student from the system.
     *
     * @param student the Student object to remove
     * @throws NonExistentStudent if the student does not exist
     */
    public static void RemoveStudent(Student student) throws NonExistentStudent {
        if (!students.containsKey(student.getID())) {
            throw new NonExistentStudent("Student does not exist");
        }
        students.remove(student.getID());
    }

    /**
     * Adds an academic member to the system.
     *
     * @param academicMember the AcademicMember object to add
     * @throws NonExistentAcademicMember if the member is null
     */
    public static void AddAcademicMember(AcademicMember academicMember) throws NonExistentAcademicMember {
        if (academicMember == null) {
            throw new NonExistentAcademicMember("Academic Member is null");
        }
        academicMembers.put(academicMember.getID(), academicMember);
    }

    /**
     * Removes an academic member from the system.
     *
     * @param academicMember the AcademicMember object to remove
     * @throws NonExistentAcademicMember if the member does not exist
     */
    public static void RemoveAcademicMember(AcademicMember academicMember) throws NonExistentAcademicMember {
        if (!academicMembers.containsKey(academicMember.getID())) {
            throw new NonExistentAcademicMember("Academic Member does not exist");
        }
        academicMembers.remove(academicMember.getID());
    }

    /**
     * Adds a department to the system.
     *
     * @param department the Department object to add
     */
    public static void AddDepartment(Department department) {
        departments.put(department.getName(), department);
    }

    /**
     * Removes a department from the system.
     *
     * @param department the Department object to remove
     */
    public static void RemoveDepartment(Department department) {
        departments.remove(department.getName());
    }

    /**
     * Adds a program to the system.
     *
     * @param program the Program object to add
     * @throws NonExistentProgram if the program is null
     */
    public static void AddProgram(Program program) throws NonExistentProgram {
        if (program == null) {
            throw new NonExistentProgram("Program is null");
        }
        programs.put(program.getCode(), program);
    }

    /**
     * Removes a program from the system.
     *
     * @param program the Program object to remove
     * @throws NonExistentProgram if the program does not exist
     */
    public static void RemoveProgram(Program program) throws NonExistentProgram {
        if (!programs.containsKey(program.getCode())) {
            throw new NonExistentProgram("Program does not exist");
        }
        programs.remove(program.getName());
    }

    /**
     * Adds a course to the system.
     *
     * @param course the Course object to add
     * @throws NonExistentCourse if the course is null
     */
    public static void AddCourse(Course course) throws NonExistentCourse {
        if (course == null) {
            throw new NonExistentCourse("Course is null");
        }
        courses.put(course.getCode(), course);
    }

    /**
     * Removes a course from the system.
     *
     * @param course the Course object to remove
     * @throws NonExistentCourse if the course does not exist
     */
    public static void RemoveCourse(Course course) throws NonExistentCourse {
        if (!courses.containsKey(course.getCode())) {
            throw new NonExistentCourse("Course does not exist");
        }
        courses.remove(course.getCode());
    }

    /**
     * Constructs the management system, reads all data files,
     * populates the maps, and displays initial listings.
     *
     * @param personstxtfilepath    path to the persons file
     * @param departmentstxtfilepath path to the departments file
     * @param programtxtfilepath    path to the programs file
     * @param coursetxtfilepath     path to the courses file
     * @param assignmenttxtfilepath path to the assignments file
     * @param gradestxtfilepath     path to the grades file
     */
    public StudentManagementSystem(
            String personstxtfilepath,
            String departmentstxtfilepath,
            String programtxtfilepath,
            String coursetxtfilepath,
            String assignmenttxtfilepath,
            String gradestxtfilepath) {

        this.personstxtfilepath = personstxtfilepath;
        this.departmentstxtfilepath = departmentstxtfilepath;
        this.programtxtfilepath = programtxtfilepath;
        this.coursetxtfilepath = coursetxtfilepath;
        this.assignmenttxtfilepath = assignmenttxtfilepath;
        this.gradestxtfilepath = gradestxtfilepath;

        System.out.println("Reading Person Information");
        try (BufferedReader br = new BufferedReader(new FileReader(personstxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    try {
                        Major m = Major.valueOf(parts[4]);
                    } catch (Exception e) {
                        throw new NonExistentDepartment("Department "+parts[4]+" Not Found");
                    }
                    switch (parts[0]) {
                        case "F":
                            AddAcademicMember(new AcademicMember(
                                    parts[1], parts[2], parts[3], Major.valueOf(parts[4])));
                            break;
                        case "S":
                            AddStudent(new Student(
                                    parts[1], parts[2], parts[3], Major.valueOf(parts[4])));
                            break;
                        default:
                            throw new InvalidPersonType("Invalid Person Type");
                    }
                } catch (InvalidPersonType e) {
                    System.out.println(e.getMessage());
                } catch (NonExistentDepartment e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading Departments");
        try (BufferedReader br = new BufferedReader(new FileReader(departmentstxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                AddDepartment(new Department(parts[0], parts[1], "", parts[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading Programs");
        try (BufferedReader br = new BufferedReader(new FileReader(programtxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    Program p = new Program(
                            parts[0], parts[1], "", parts[3],
                            DegreeLevel.valueOf(parts[4]), Integer.parseInt(parts[5]));
                    AddProgram(p);
                } catch (NonExistentProgram e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading Courses");
        try (BufferedReader br = new BufferedReader(new FileReader(coursetxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (!departments.containsKey(parts[2])) {
                        throw new NonExistentDepartment("Department "+parts[2]+" Not Found");
                    }
                    Course c = new Course(
                            parts[0], parts[1], "",
                            departments.get(parts[2]),
                            Integer.parseInt(parts[3]),
                            parts[4], parts[5]);
                    programs.get(parts[5]).AddCourse(c);
                    AddCourse(c);
                } catch (NonExistentProgram e) {
                    System.out.println(e.getMessage());
                } catch (NonExistentDepartment e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading Course Assignments");
        try (BufferedReader br = new BufferedReader(new FileReader(assignmenttxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    switch (parts[0]) {
                        case "F":
                            if (!academicMembers.containsKey(parts[1])) {
                                throw new NonExistentAcademicMember(
                                        "Academic Member Not Found with ID " + parts[1]);
                            }
                            if (!courses.containsKey(parts[2])) {
                                throw new NonExistentCourse("Course " + parts[2] + " Not Found");
                            }
                            academicMembers.get(parts[1]).AddTeachingCourse(parts[2]);
                            courses.get(parts[2]).setInstructor(parts[1]);
                            break;
                        case "S":
                            if (!students.containsKey(parts[1])) {
                                throw new NonExistentStudent("Student Not Found with ID " + parts[1]);
                            }
                            if (!courses.containsKey(parts[2])) {
                                throw new NonExistentCourse("Course " + parts[2] + " Not Found");
                            }
                            students.get(parts[1]).enrollCourse(parts[2]);
                            courses.get(parts[2]).EnrollStudent(parts[1]);
                            break;
                        default:
                            throw new InvalidPersonType("Invalid Person Type");
                    }
                } catch (NonExistentPerson e) {
                    System.out.println(e.getMessage());
                } catch (NonExistentCourse e) {
                    System.out.println(e.getMessage());
                } catch (InvalidPersonType e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Reading Grades");
        try (BufferedReader br = new BufferedReader(new FileReader(gradestxtfilepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    if (!students.containsKey(parts[1])) {
                        throw new NonExistentStudent("Student Not Found with ID " + parts[1]);
                    }
                    if (!courses.containsKey(parts[2])) {
                        throw new NonExistentCourse("Course " + parts[2] + " Not Found");
                    }
                    try {
                        LetterGrade.valueOf(parts[0]);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidLetterGrades("Invalid Letter Grade: " + parts[0]);
                    }
                    students.get(parts[1]).getCompletedGrades()
                            .put(courses.get(parts[2]), LetterGrade.valueOf(parts[0]));
                    students.get(parts[1]).getEnrolledCourse()
                            .remove(courses.get(parts[2]));
                    courses.get(parts[2]).AddGrade(
                            students.get(parts[1]), LetterGrade.valueOf(parts[0]));
                } catch (NonExistentStudent e) {
                    System.out.println(e.getMessage());
                } catch (NonExistentCourse e) {
                    System.out.println(e.getMessage());
                } catch (InvalidLetterGrades e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initial listings after loading all data
        ListAcademicMembers();
        ListStudents();
        ListDepartments();
        ListPrograms();
        ListCourses();
        ListCourseReports();
        ListStudentReports();
    }

    /**
     * Generic file reader utility that converts each line
     * and passes it to a consumer.
     *
     * @param <T>       type of converted item
     * @param input     file path
     * @param converter function to convert line to T
     * @param consumer  action to perform on T
     */
    private static <T> void readFile(
            String input,
            Function<String, T> converter,
            Consumer<T> consumer) {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                T item = converter.apply(line);
                consumer.accept(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a report of all academic members.
     */
    public void ListAcademicMembers() {
        String line = "----------------------------------------";
        System.out.println(line);
        System.out.println("            Academic Members");
        System.out.println(line);
        for (AcademicMember member : academicMembers.values()) {
            System.out.println(member.generateReport() + "\n");
        }
        System.out.println(line);
    }

    /**
     * Prints a report of all students.
     */
    public void ListStudents() {
        System.out.println();
        String line = "----------------------------------------";
        System.out.println(line + "\n                STUDENTS\n" + line);
        for (Student student : students.values()) {
            System.out.println(student.generateReport() + "\n");
        }
        System.out.println(line);
    }

    /**
     * Prints a report of all departments.
     */
    public void ListDepartments() {
        System.out.println();
        String line = "---------------------------------------";
        System.out.println(line + "\n              DEPARTMENTS\n" + line);
        for (Department department : departments.values()) {
            System.out.println(department.generateReport() + "\n");
        }
        System.out.println(line + "-");
    }

    /**
     * Prints a report of all programs.
     */
    public void ListPrograms() {
        System.out.println();
        String line = "--------------------------------------";
        System.out.println(line + "\n                PROGRAMS\n" + line + "-");
        Iterator<Program> it = programs.values().iterator();
        while (it.hasNext()) {
            Program program = it.next();
            System.out.print(program.generateReport() + "\n");
            if (it.hasNext()) {
                System.out.print("\n");
            }
        }
        System.out.println("\n" + line+"--");
    }

    /**
     * Prints a report of all courses.
     */
    public void ListCourses() {
        System.out.println();
        String line = "---------------------------------------";
        System.out.println(line + "\n                COURSES\n" + line);
        for (Course course : courses.values()) {
            System.out.println(course.generateReport() + "\n");
        }
        System.out.println(line + "-");
    }

    /**
     * Prints detailed reports for each course, including
     * enrolled students, instructor, grade distribution, and average.
     */
    public void ListCourseReports() {
        System.out.println();
        String line = "----------------------------------------";
        System.out.println(line + "\n             COURSE REPORTS\n" + line);
        for (Course course : courses.values()) {
            String enrolledStudentString = "Enrolled Students:\n";
            for (Student student : course.getEnrolledStudents()) {
                enrolledStudentString += "- " + student.getName() +
                        " (ID: " + student.getID() + ")\n";
            }
            System.out.printf(
                    Locale.US,
                    "%s%n%nInstructor: %s%n%n%s%nGrade Distribution:%n%sAverage Grade: %.2f%n%n%n",
                    course.generateReport(),
                    course.getInstructor().getName(),
                    enrolledStudentString,
                    course.getGradeDistribution(),
                    course.getAverageGrade()
            );
            System.out.println(line + "\n");
        }
    }

    /**
     * Prints detailed reports for each student, including
     * enrolled courses, completed courses with grades, and GPA.
     */
    public void ListStudentReports() {
        String line = "----------------------------------------";
        System.out.println(line + "\n            STUDENT REPORTS\n" + line);
        for (Student student : students.values()) {
            System.out.println(student.generateReport() + "\n");

            String enrolled = "\nEnrolled Courses:\n";
            for (Course c : student.getEnrolledCourse()) {
                enrolled += "- " + c.getName() +
                        " (" + c.getCode() + ")\n";
            }
            System.out.println(enrolled);

            String completed = "Completed Courses:\n";
            for (Map.Entry<Course, LetterGrade> entry :
                    student.getCompletedGrades().entrySet()) {
                Course c = entry.getKey();
                LetterGrade l = entry.getValue();
                completed += "- " + c.getName() +
                        " (" + c.getCode() + "): " + l + "\n";
            }
            System.out.println(completed);

            double gpa = student.getAverageGrade();
            System.out.printf(Locale.US, "GPA: %.2f%n", gpa);

            System.out.println(line + "\n");
        }
    }
}
