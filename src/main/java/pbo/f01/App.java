package pbo.f01;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import pbo.f01.model.Dorm;
import pbo.f01.model.Student;

/**
 * 12S22033 - Mickael Sitompul
 * 12S22027 - Ferry Panjaitan
 * mvn clean compile assembly:single
 * java -cp .\target\f01-1.0-SNAPSHOT-jar-with-dependencies.jar pbo.f01.App
 */

public class App {
    public static void main(String[] _args) {
        Constrac constractor = new Constrac("dormy_pu");
        Scanner scanner = new Scanner(System.in);
        boolean keepGoing = true;
        String line = null;

        while (scanner.hasNext() && keepGoing) {
            line = scanner.nextLine();

            if (line.equals("---")) {
                break;
            }

            String[] data = line.split("#");
            String command = data[0];
            data = Arrays.copyOfRange(data, 1, data.length);

            if (command.equals("student-add")) {
                if (constractor.findStudent(data[0]) == null) {
                    constractor.save(new Student(data[0], data[1], data[2], data[3]));
                }
            } else if (command.equals("dorm-add")) {
                if (constractor.findDorm(data[0]) == null) {
                    constractor.save(new Dorm(data[0], Integer.parseInt(data[1]), data[2]));
                }
            } else if (command.equals("assign")) {
                Student student = constractor.findStudent(data[0]);
                if (student != null) {
                    Dorm dorm = constractor.findDorm(data[1]);
                    if (dorm != null && dorm.addResident(student)) {
                        constractor.update(dorm);
                    }
                }
            } else if (command.equals("display-all")) {
                List<Dorm> dorms = constractor.getAllDorms();
                for (Dorm dorm : dorms) {
                    System.out.println(dorm);
                    for (Student student : dorm.getResidents()) {
                        System.out.println(student);
                    }
                }
            }
        }

        scanner.close();
        constractor.shutdown();
    }
}
