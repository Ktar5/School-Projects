import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Tests {
    JobSchedule schedule;

    @Before
    public void before() {
        schedule = new JobSchedule();
    }

    @Test
    public void givenTest() {
        schedule.addJob(8); //adds job 0 with time 8
        JobSchedule.Job j1 = schedule.addJob(3); //adds job 1 with time 3
        schedule.addJob(5); //adds job 2 with time 5

        assertEquals(8, schedule.minCompletionTime()); //should return 8, since job 0 takes time 8 to complete.

        /* Note it is not the min completion time of any job, but the earliest the entire set can complete. */
        schedule.getJob(0).requires(schedule.getJob(2)); //job 2 must precede job 0

        assertEquals(13, schedule.minCompletionTime()); //should return 13 (job 0 cannot start until time 5)
        schedule.getJob(0).requires(j1); //job 1 must precede job 0

        assertEquals(13, schedule.minCompletionTime()); //should return 13
        assertEquals(5, schedule.getJob(0).getStartTime()); //should return 5

        assertEquals(0, j1.getStartTime()); //should return 0

        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0

        j1.requires(schedule.getJob(2)); //job 2 must precede job 1

        assertEquals(16, schedule.minCompletionTime()); //should return 16
        assertEquals(8, schedule.getJob(0).getStartTime()); //should return 8
        assertEquals(5, schedule.getJob(1).getStartTime()); //should return 5
        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0

        schedule.getJob(1).requires(schedule.getJob(0)); //job 0 must precede job 1 (creates loop)

        assertEquals(-1, schedule.minCompletionTime()); //should return -1

        assertEquals(-1, schedule.getJob(0).getStartTime()); //should return -1

        assertEquals(-1, schedule.getJob(1).getStartTime()); //should return -1
        assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0 (no loops in prerequisites)
    }

    @Test
    public void basicTestWithoutCycles() {
        JobSchedule.Job job0 = schedule.addJob(1);
        JobSchedule.Job job1 = schedule.addJob(10);
        JobSchedule.Job job2 = schedule.addJob(100);
        JobSchedule.Job job3 = schedule.addJob(1000);
        job3.requires(job2);
        assertEquals(1, job3.getIndegree());
        assertEquals(0, job2.getIndegree());
        assertEquals(100, job3.getStartTime());
        assertEquals(1100, job3.getCompletionTime());

        job2.requires(job1);
        assertEquals(10, job2.getStartTime());
        assertEquals(110, job2.getCompletionTime());

        assertEquals(110, job3.getStartTime());
        assertEquals(1110, job3.getCompletionTime());

        job1.requires(job0);
        assertEquals(1, job1.getStartTime());
        assertEquals(11, job1.getCompletionTime());
        assertEquals(11, job2.getStartTime());
        assertEquals(111, job2.getCompletionTime());

        assertEquals(111, job3.getStartTime());
        assertEquals(1111, job3.getCompletionTime());


        assertEquals(0, job0.getStartTime());
        assertEquals(1, job0.getCompletionTime());

        assertEquals(1111, schedule.minCompletionTime());
    }

    @Test
    public void basicTestWithCycles() {
        JobSchedule.Job job0 = schedule.addJob(1);
        JobSchedule.Job job1 = schedule.addJob(10);
        JobSchedule.Job job2 = schedule.addJob(100);
        JobSchedule.Job job3 = schedule.addJob(1000);
        job3.requires(job2);
        job2.requires(job1);
        job1.requires(job0);
        job1.requires(job3);
        assertEquals(-1, schedule.minCompletionTime());
    }

    @Test
    public void basicTestWithWeirdData() {
        JobSchedule.Job job0 = schedule.addJob(1);
        JobSchedule.Job job1 = schedule.addJob(10);
        JobSchedule.Job job2 = schedule.addJob(100);
        JobSchedule.Job job3 = schedule.addJob(1000);

        job0.requires(job0);
        job0.requires(job1);
        job0.requires(job2);
        job0.requires(job3);

        job1.requires(job0);
        job1.requires(job1);
        job1.requires(job2);
        job1.requires(job3);

        job2.requires(job0);
        job2.requires(job1);
        job2.requires(job2);
        job2.requires(job3);

        job3.requires(job0);
        job3.requires(job1);
        job3.requires(job2);
        job3.requires(job3);

        assertEquals(-1, schedule.minCompletionTime());
    }

    @Test
    public void generatedTest() {
        Random r = new Random();

        int amount = 1000000;
        long miliseconds = System.currentTimeMillis();

        for (int i = 0; i < amount; i++) {
            schedule.addJob(i);
        }

        System.out.println("Create Jobs " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

        for (int i = 0; i < amount; i++) {
            schedule.getJob(r.nextInt(amount)).requires(schedule.getJob(r.nextInt(amount)));
        }

        System.out.println("Requirement Set " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

        schedule.minCompletionTime();
        schedule.minCompletionTime();
        schedule.minCompletionTime();

        System.out.println("Completion Times " + (System.currentTimeMillis() - miliseconds));
    }


}