import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MaxHeap {
    private ArrayList<Student> students;

    public MaxHeap(int capacity) {
        students = new ArrayList<Student>(capacity);
    }

    public MaxHeap(Collection<Student> collection) {
        students = new ArrayList<Student>(collection);
        for (int i = 0; i < students.size(); i++) {
            students.get(i).heapIndex = i;
        }
        for (int i = size() / 2; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean indiciesAreCorrect(){
        for (int i = 0; i < students.size(); i++) {
            if(students.get(i).heapIndex != i){
                return false;
            }
        }
        return true;
    }

    public Student getMax() {
        if (size() < 1) {
            throw new IndexOutOfBoundsException("No maximum value:  the heap is empty.");
        }
        return students.get(0);
    }

    public Student extractMax() {
        Student value = getMax();
        Student lastStudent = students.get(size() - 1);
        lastStudent.heapIndex = 0;
        students.set(0, lastStudent);
        students.remove(size() - 1);
        maxHeapify(0);
        return value;
    }

    public void insert(Student elt) {
        students.add(elt);
        elt.heapIndex = students.size() - 1;
        bubble(students.size() - 1);
    }

    public void bubble(int indexToBubble) {
        Student child = students.get(indexToBubble);
        int parentIndex = getParentIndex(indexToBubble);
        Student parent = students.get(parentIndex);
        if (parent.gpa() < child.gpa()) {
            swap(indexToBubble, parentIndex);
            bubble(parentIndex);
        }
    }

    //Note: I'm sad that I am given a student as opposed to an index :'(
    public void changeKey(Student s, double newGPA) {
        int index = s.heapIndex;
        if (index == -1) {
            return;
        }
        if (newGPA > s.gpa()) {
            s.setGPA(newGPA);
            bubble(index);
        } else if (newGPA < s.gpa()) {
            s.setGPA(newGPA);
            maxHeapify(index);
        }
    }

    public boolean isHeap(int indexOfRoot) {
        Student max = students.get(indexOfRoot);
        int leftIndex = getLeftIndex(indexOfRoot);
        int rightIndex = getRightIndex(indexOfRoot);
        int size = students.size();

        if(leftIndex >= size){
            return true;
        } else if(rightIndex >= size){
            return max.gpa() > students.get(leftIndex).gpa();
        }else if (max.gpa() > students.get(leftIndex).gpa() && max.gpa() > students.get(rightIndex).gpa()) {
            return isHeap(getLeftIndex(leftIndex)) && isHeap(rightIndex);
        }
        return false;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftIndex(int index) {
        return 2 * index + 1;
    }

    private int getRightIndex(int index) {
        return 2 * index + 2;
    }

    private int size() {
        return students.size();
    }

    private void swap(int from, int to) {
        Student studentFrom = students.get(from);
        Student studentTo = students.get(to);
        studentFrom.heapIndex = to;
        studentTo.heapIndex = from;
        students.set(from, studentTo);
        students.set(to, studentFrom);
    }

    private void maxHeapify(int index) {
        int left = getLeftIndex(index);
        int right = getRightIndex(index);
        int largest = index;
        if (left < size() && students.get(left).gpa() > students.get(largest).gpa()) {
            largest = left;
        }
        if (right < size() && students.get(right).gpa() > students.get(largest).gpa()) {
            largest = right;
        }
        if (largest != index) {
            swap(index, largest);
            maxHeapify(largest);
        }
    }
}
