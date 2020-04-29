import java.util.*;

public class JobSchedule {
    private boolean updated = true;
    private List<Job> jobs = new ArrayList<>();
    private int previousMax = -1;

    public JobSchedule() {

    }

    public Job addJob(int time) {
        Job job = new Job(time);
        jobs.add(job);
        job.index = jobs.size() - 1;
        updated = true;
        return job;
    }

    public Job getJob(int index) {
        return jobs.get(index);
    }


    public int minCompletionTime() {
        long miliseconds = 0;
        if(!updated){
            return previousMax;
        }

        miliseconds = System.currentTimeMillis();

        //Enqueue all jobs with an in-degree of 0
        Queue<Job> queue = new LinkedList<>();
        for (Job job : jobs) {
            job.startTime = 0;
            job.tempIndegree = job.indegree;
            if (job.getIndegree() == 0)
                queue.add(job);
        }

        System.out.println("Enqueue " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

        // Count visited jobs
        int count = 0;

        //Stack the topological order
//        System.out.println("\nQueue order: ");
        Queue<Job> topOrder = new LinkedList<>();
        while (!queue.isEmpty()) {
            // Dequeue and add to top order
            Job child = queue.poll();
//            System.out.println(child.index);
            topOrder.add(child);

            //Modify in-degrees
            for (Job parent : child.parents) {
                // If in-degree becomes zero, add it to queue
                if (--parent.tempIndegree == 0)
                    queue.add(parent);
            }
            count++;
        }

        System.out.println("Top Sort " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

//        System.out.println("\nTop order");
        // Process jobs in topological order
        while (!topOrder.isEmpty()) {
            // Get the next job from topological order
            Job child = topOrder.poll();
//            System.out.println("  - " + child.index);

            // Update completion time
            for (Job parent : child.parents) {
//                System.out.println("    - " + parent.index + " : parSt " + parent.startTime + " : chldComp " + child.getCompletionTime());
                if (child.getCompletionTime() > parent.startTime) {
                    parent.startTime = child.getCompletionTime();
                }
            }
        }

        System.out.println("Time Update " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

        updated = false;

        // Check for cycles and update
        if(count != jobs.size()){
            return -1;
        }

        // Else find shortest distance
        int max = 0;
        for (Job job : jobs) {
            max = Math.max(job.getCompletionTime(), max);
        }

        System.out.println("Max " + (System.currentTimeMillis() - miliseconds));
        miliseconds = System.currentTimeMillis();

        previousMax = max;
        return max;
    }

    public class Job {
        public List<Job> parents = new ArrayList<>();
        private int indegree = 0;
        public int tempIndegree = 0;

        public int duration;
        public int startTime;

        public int index;

        private Job(int duration) {
            this.duration = duration;
            this.startTime = 0;
        }

        public void requires(Job requirement) {
            if (requirement != this) {
                requirement.parents.add(this);
                this.indegree++;
                updated = true;
            }
        }

        public int getIndegree() {
            return this.indegree;
        }

        public int getCompletionTime() {
            return startTime + duration;
        }

        // Will return the earliest possible start time for the job.
        public int getStartTime() {
            if (updated) {
                minCompletionTime();
            }

            if (tempIndegree != 0) {
                return -1;
            }

            return startTime;
        }

    }

}
