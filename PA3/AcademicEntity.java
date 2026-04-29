import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AcademicEntity implements IAcademicEntity {
    /** Unique code of the entity */
    private String code;
    /** Name of the entity */
    private String name;
    /** Short description of the entity */
    private String description;

    /**
     * Creates a new academic entity.
     *
     * @param code        unique code
     * @param name        name of the entity
     * @param description brief description
     */
    public AcademicEntity(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String getCode() {
        return this.code;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return this.description;
    }
}
/**
 * Represents a course, with grading and reporting features.
 */
class Course extends AcademicEntity implements  IReportable {
    /** Department this course belongs to */
    private Department department;
    /** Total credit value */
    private int totalCredits;
    /** Semester when the course is offered */
    private String semester;
    /** Program code the course is part of */
    private Program program;
    /** Assigned instructor (defaults to unassigned) */
    private AcademicMember instructor = new AcademicMember("-1","Not assigned","",null);
    /** List of enrolled students */
    private ArrayList<Student> enrolledStudents = new ArrayList<>();
    /** Mapping of students to their letter grades */
    private Map<Student, LetterGrade> grades = new HashMap<>();

    /**
     * Constructs a new course.
     *
     * @param code         course code
     * @param name         course name
     * @param description  course description
     * @param department   department offering the course
     * @param totalCredits number of credits
     * @param semester     semester identifier
     * @param programCode  code of the program
     * @throws NonExistentProgram if the program code doesn't exist
     */
    public Course(String code, String name, String description, Department department, int totalCredits, String semester, String programCode) {
        super(code, name, description);
        this.department = department;
        this.totalCredits = totalCredits;
        this.semester = semester;
        this.setProgram(programCode);
    }


    /** {@inheritDoc} */
    @Override
    public String generateReport() {
        return "Course Code: "+this.getCode()+"\n"+
                "Name: "+this.getName()+"\n"+
                "Department: "+this.department.getName()+"\n"+
                "Credits: "+this.totalCredits+"\n"+
                "Semester: "+this.semester;
    }

    /**
     * Returns the assigned instructor.
     *
     * @return instructor member
     */
    public AcademicMember getInstructor() {
        return this.instructor;
    }

    /**
     * Assigns an instructor by their ID.
     *
     * @param instructorId ID of the academic member
     * @throws NonExistentAcademicMember if member not found
     */
    public void setInstructor(String instructorId) throws NonExistentAcademicMember {
        if (!StudentManagementSystem.academicMembers.containsKey(instructorId)) {
            throw new NonExistentAcademicMember("Academic Member Not Found with ID "+instructorId);
        }
        this.instructor = StudentManagementSystem.academicMembers.get(instructorId);
    }

    /**
     * Returns the list of enrolled students.
     *
     * @return list of students
     */
    public ArrayList<Student> getEnrolledStudents() {
        return this.enrolledStudents;
    }

    /**
     * Enrolls a student by their ID.
     *
     * @param studentId ID of the student
     * @throws NonExistentStudent if student not found
     */
    public void EnrollStudent(String studentId) throws NonExistentStudent {
        if (!StudentManagementSystem.students.containsKey(studentId)) {
            throw new NonExistentStudent("Student Not Found with ID "+studentId);
        }
        this.enrolledStudents.add(StudentManagementSystem.students.get(studentId));
    }

    /**
     * Adds a letter grade for a student.
     *
     * @param student student object
     * @param grade   letter grade
     */
    public void AddGrade(Student student, LetterGrade grade) {
        this.grades.put(student, grade);
    }

    /**
     * Provides the grade distribution as text.
     *
     * @return distribution of letter grades
     */
    public String getGradeDistribution() {
        Map<LetterGrade, Integer> counts = new HashMap<>();
        for (LetterGrade grade : grades.values()) {
            int oldCount = counts.containsKey(grade) ? counts.get(grade) : 0;
            counts.put(grade, oldCount + 1);
        }

        StringBuilder sb = new StringBuilder();
        for (LetterGrade lg : LetterGrade.values()) {
            Integer cnt = counts.get(lg);
            if (cnt != null && cnt > 0) {
                sb.append(lg.name())
                        .append(": ")
                        .append(cnt)
                        .append("\n");
            }
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString()+"\n\n";
    }

    /**
     * Calculates the average grade score.
     *
     * @return average numeric score
     */
    public double getAverageGrade() {
        double sum = 0.0;
        int count = 0;
        for (LetterGrade grade : grades.values()) {
            sum += grade.getLetterScore();
            count++;
        }
        if (count == 0) {
            return 0.00;
        }
        return sum / count;
    }

    /**
     * Returns the total credits of the course.
     *
     * @return credit count
     */
    public int getTotalCredits() {
        return this.totalCredits;
    }

    /**
     * Sets the program by its code.
     *
     * @param programCode program identifier
     * @throws NonExistentProgram if not found
     */
    public void setProgram(String programCode) throws NonExistentProgram {
        if (!StudentManagementSystem.programs.containsKey(programCode)) {
            throw new NonExistentProgram("Program "+programCode+" Not Found");
        }
        this.program = StudentManagementSystem.programs.get(programCode);
    }
}

/**
 * Represents a department, with a head and reporting ability.
 */
class Department extends AcademicEntity implements IReportable {
    /** Head of the department */
    private AcademicMember head;

    /**
     * Constructs a new department.
     *
     * @param code        department code
     * @param name        department name
     * @param description department description
     * @param headId      ID of the head member
     */
    public Department(String code, String name, String description, String headId) {
        super(code, name, description);
        try {
            this.SetHead(headId);
        } catch (NonExistentAcademicMember e) {
            System.out.println(e.getMessage());
            this.head = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String generateReport() {
        String headName = (this.head != null) ? this.head.getName() : "Not assigned";
        return "Department Code: "+this.getCode()+"\n"+
                "Name: "+this.getName()+"\n"+
                "Head: "+headName;
    }

    /**
     * Returns the department head.
     *
     * @return head member
     */
    public AcademicMember getHead() {
        return this.head;
    }

    /**
     * Sets the head by their ID.
     *
     * @param headId academic member ID
     * @throws NonExistentAcademicMember if not found
     */
    public void SetHead(String headId) throws NonExistentAcademicMember {
        if (!StudentManagementSystem.academicMembers.containsKey(headId)) {
            throw new NonExistentAcademicMember("Academic Member Not Found with ID "+headId);
        }
        this.head = StudentManagementSystem.academicMembers.get(headId);
    }
}

/**
 * Represents a program of study, including its courses.
 */
class Program extends AcademicEntity implements IReportable {
    /** Department offering the program */
    private Department department;
    /** Degree level (e.g., Bachelor, Master, Phd) */
    private DegreeLevel degreeLevel;
    /** Total required credits */
    private int totalCredits;
    /** List of courses in the program */
    private ArrayList<Course> courses = new ArrayList<>();

    /**
     * Constructs a new program.
     *
     * @param code           program code
     * @param name           program name
     * @param description    program description
     * @param departmentName name of the department
     * @param degreeLevel    level of the degree
     * @param totalCredits   required credits
     */
    public Program(String code, String name, String description, String departmentName, DegreeLevel degreeLevel, int totalCredits) {
        super(code, name, description);
        this.department = department;
        this.degreeLevel = degreeLevel;
        this.totalCredits = totalCredits;
        try {
            setDepartment(departmentName);
        } catch (NonExistentDepartment e) {
            System.out.println(e.getMessage());
            this.department = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String generateReport() {
        String courseline = "{";
        for (int i = 0; i < courses.size(); i++) {
            courseline += courses.get(i).getCode();
            if (i < courses.size() - 1) {
                courseline += ",";
            }
        }
        courseline += "}";

        if (courseline.length() == 2) {
            courseline = "- ";
        }
        String departmentName = (this.department != null) ? this.department.getName() : "Department Not assigned";
        return "Program Code: "+this.getCode()+"\n"+
                "Name: "+this.getName()+"\n"+
                "Department: "+departmentName+"\n"+
                "Degree Level: "+this.getDegreeLevel()+"\n"+
                "Required Credits: "+this.getTotalCredits()+"\n"+
                "Courses: "+courseline;
    }

    /**
     * Returns the related department.
     *
     * @return department object
     */
    public Department getDepartment() {
        return this.department;
    }

    /**
     * Sets the department by name.
     *
     * @param departmentName name of the department
     * @throws NonExistentDepartment if not found
     */
    public void setDepartment(String departmentName) throws NonExistentDepartment {
        if (!StudentManagementSystem.departments.containsKey(departmentName)) {
            throw new NonExistentDepartment("Department "+departmentName+" Not Found");
        }
        this.department = StudentManagementSystem.departments.get(departmentName);
    }

    /**
     * Returns the degree level.
     *
     * @return degree level
     */
    public DegreeLevel getDegreeLevel() {
        return this.degreeLevel;
    }

    /**
     * Returns the total required credits.
     *
     * @return credits count
     */
    public int getTotalCredits() {
        return this.totalCredits;
    }

    /**
     * Adds a course to the program.
     *
     * @param course course object
     */
    public void AddCourse(Course course) {
        this.courses.add(course);
    }
}

/**
 * Degree levels available for programs.
 */
enum DegreeLevel {
    Bachelor,
    Master,
    Phd
}
