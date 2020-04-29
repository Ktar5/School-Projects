import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class MaxHeapTest {
    private MaxHeap heap;

    @Before
    public void setUp() throws Exception {
        heap = new MaxHeap(10);
        heap.insert(new Student("Susan", 60, 3.5));
        heap.insert(new Student("Ben", 70, 3.4));
        heap.insert(new Student("Reed", 120, 4.0));
        heap.insert(new Student("Johnny", 50, 1.2));
        System.out.println("Before is ran");
    }

    @Test
    public void isHeap() {
        assertTrue(heap.isHeap(0));
    }

    @Test
    public void indiciesAreCorrect(){
        assertTrue(heap.indiciesAreCorrect());
    }

    @Test
    public void testStudent() {
        assertEquals(heap.getMax().getName(), "Reed");
        assertEquals(heap.getMax().units(), 120);
        heap.getMax().setUnits(150);
        assertEquals(heap.getMax().units(), 150);
        assertTrue(heap.isHeap(0));
        assertTrue(heap.indiciesAreCorrect());
    }

    @Test
    public void testCreation() {
        heap = new MaxHeap(heap.getStudents());
        assertEquals(4.0, heap.getMax().gpa(), .000001);
        assertTrue(heap.isHeap(0));
        assertTrue(heap.indiciesAreCorrect());
    }

    @Test
    public void testExtractMax() {
        assertEquals(4.0, heap.getMax().gpa(), .000001);

        assertEquals(4.0, heap.extractMax().gpa(), .000001);
        assertEquals(3.5, heap.extractMax().gpa(), .000001);

        assertTrue(heap.isHeap(0));
        assertTrue(heap.indiciesAreCorrect());
    }

    @Test
    public void testNewStudent() {
        Student student = new Student("Nerd");
        assertNotNull(student);
        assertTrue(heap.indiciesAreCorrect());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testZeroSize() {
        MaxHeap heapZero = new MaxHeap(0);
        heapZero.getMax();
        assertTrue(heap.isHeap(0));

    }

    @Test
    public void testChangeKey() {
        Student max = heap.getMax();

        assertEquals(4.0, max.gpa(), .000001);
        heap.changeKey(max, 1.0);
        assertEquals(3.5, heap.getMax().gpa(), .000001);
        assertEquals(1.0, max.gpa(), .0000001);

        heap.changeKey(max, 4.0);
        assertEquals(heap.getMax(), max);


        Student weirdJohnny = new Student("Johnny", 50, 5.0);
        heap.changeKey(weirdJohnny, 5.0);
        assertNotSame(heap.getMax(), weirdJohnny);
        assertTrue(heap.isHeap(0));
        assertTrue(heap.indiciesAreCorrect());
    }

}
