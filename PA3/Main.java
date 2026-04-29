import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args){

        String personstxtfilepath     = args[0];
        String departmentstxtfilepath = args[1];
        String programtxtfilepath     = args[2];
        String coursetxtfilepath      = args[3];
        String assignmenttxtfilepath  = args[4];
        String gradestxtfilepath      = args[5];
        String outputtxtfilepath      = args[6];

        /*
        String personstxtfilepath     = "ETestCases/persons.txt";
        String departmentstxtfilepath = "ETestCases/departments.txt";
        String programtxtfilepath     = "ETestCases/programs.txt";
        String coursetxtfilepath      = "ETestCases/courses.txt";
        String assignmenttxtfilepath  = "ETestCases/assignments.txt";
        String gradestxtfilepath      = "ETestCases/grades.txt";
        String outputtxtfilepath      = "ETestCases/outputtt.txt";

        StudentManagementSystem s = new StudentManagementSystem(
                personstxtfilepath,
                departmentstxtfilepath,
                programtxtfilepath,
                coursetxtfilepath,
                assignmenttxtfilepath,
                gradestxtfilepath
        );
        */


        try (
                FileOutputStream fos = new FileOutputStream(outputtxtfilepath, false);
                PrintStream ps       = new PrintStream(fos)
        ) {
            System.setOut(ps);
            System.setErr(ps);

            StudentManagementSystem s = new StudentManagementSystem(
                personstxtfilepath,
                departmentstxtfilepath,
                programtxtfilepath,
                coursetxtfilepath,
                assignmenttxtfilepath,
                gradestxtfilepath
            );

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
