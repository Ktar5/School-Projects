import java.util.ArrayList;

public class HeapExperiments {
    static int EXPERIMENT = 24;


    static int CHANGES = 1000;

    public static void main(String[] args) {
        System.out.println("Experiment number " + EXPERIMENT);
        switch (EXPERIMENT) {
            case 1:
                experiments(10000, false, false, false);
                break;
            case 2:
                experiments(100000, false, false, false);
                break;
            case 3:
                experiments(1000000, false, false, false);
                break;
            case 4:
                experiments(10000, false, false, false);
                experiments(10000, true, false, false);
                break;
            case 5:
                experiments(100000, false, false, false);
                experiments(100000, true, false, false);
                break;
            case 6:
                experiments(1000000, false, false, false);
                experiments(1000000, true, false, false);
                break;
            case 7:
                experiments(10000, true, false, false);
                experiments(10000, false, false, false);
                break;
            case 8:
                experiments(100000, true, false, false);
                experiments(100000, false, false, false);
                break;
            case 9:
                experiments(1000000, true, false, false);
                experiments(1000000, false, false, false);
                break;
            case 10:
                experiments(10000, true, false, false);
                break;
            case 11:
                experiments(100000, true, false, false);
                break;
            case 12:
                experiments(1000000, true, false, false);
                break;
            case 13:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(10000, false, false, false);
                }
                break;
            case 14:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(10000, true, false, false);
                }
                break;
            case 15:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(100000, false, false, false);
                }
                break;
            case 16:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(100000, true, false, false);
                }
                break;
            case 17:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(1000000, false, false, false);
                }
                break;
            case 18:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(1000000, true, false, false);
                }
                break;
            case 19:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(10000, true, true, false);
                }
                break;
            case 20:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(100000, true, true, false);
                }
                break;
            case 21:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(1000000, true, true, false);
                }
                break;
            case 22:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(10000, false, false, true);
                }
                break;
            case 23:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(100000, false, false, true);
                }
                break;
            case 24:
                for (int i = 0; i < 10; i++) {
                    System.out.println("Run " + (i + 1) + " of 10");
                    experiments(1000000, false, false, true);
                }
                break;

            default:
                break;
        }


    }

    public static void experiments(int population, boolean iteratedInsertion, boolean worstCase, boolean changeKey) {
        MaxHeap heap;
        long startTime, endTime, duration;
        ArrayList<Student> students;

        System.out.println("Building a heap of " + population + " students:");
        if (!iteratedInsertion) {
            students = createStudents(population, worstCase);
            startTime = System.nanoTime();
            heap = linearBuildHeap(students);
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.println("Linear-time build time  = " + duration);
        } else {
            students = createStudents(population, worstCase);
            startTime = System.nanoTime();
            heap = iteratedInsertionBuildHeap(students);
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.println("Iterated-insertion build time  = " + duration);
            if (worstCase)
                System.out.println("Worst case input used!!!");

        }
        System.out.println("Time per student = " + duration / population + "\n");


        if (changeKey) {
            startTime = System.nanoTime();
            makeErrors(students, heap, population);
            endTime = System.nanoTime();
            duration = endTime - startTime;

            System.out.println("Time to make " + CHANGES + " changes = " + duration);
            System.out.println("Time per change = " + duration / CHANGES + "\n");
        }

    }

    private static void makeErrors(ArrayList<Student> students, MaxHeap heap, int size) {
        for (int i = 0; i < CHANGES; i++) {
            int index = (int) (Math.random() * size);
            Student s = students.get(index);
            double newGPA = Math.random() * 8;
            heap.changeKey(s, newGPA);
        }
    }

    private static MaxHeap linearBuildHeap(ArrayList<Student> students) {
        return new MaxHeap(students);
    }

    private static MaxHeap iteratedInsertionBuildHeap(ArrayList<Student> students) {
        MaxHeap heap = new MaxHeap(students.size());
        for (Student s : students)
            heap.insert(s);
        return heap;
    }

    private static ArrayList<Student> createStudents(int number, boolean worstCase) {
        ArrayList<Student> students = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            int units = (int) (Math.random() * 100);
            double gradePoints;
            if (worstCase)
                gradePoints = 4.0 * i / number;
            else
                gradePoints = Math.random() * 4;     //Random gradePoints
            students.add(new Student("student" + i, units, gradePoints));
        }
        return students;
    }


}
