package algo_questions;

import java.util.Arrays;

/**
 * Class containing solutions for algorithm.
 * @author elias.
 */
public class Solutions {

    /**
     * Constructor.
     */
    public Solutions(){}

    /**
     * Method computing the maximal amount of tasks out of n tasks
     * that can be completed with m time slots.
     * A task can only be completed in a time slot if the length of the
     * time slot is grater than the no. of hours needed to complete the task.
     * @param tasks array of integers of length n.
     *             tasks[i] is the time in hours required to complete task i.
     * @param timeSlots array of integersof length m.
     *                  timeSlots[i] is the length in hours of the slot i.
     * @return maximal amount of tasks that can be completed (integer).
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        int numOfTasks = 0;
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        int i = 0;
        int j = 0;
        while (i != timeSlots.length && j != tasks.length){
            if (timeSlots[i] >= tasks[j]){
                numOfTasks++;
                i++;
                j++;
                continue;
            }
            i++;
        }
        return numOfTasks;
    }

    /**
     * Method computing the min amount of jumps a frog needs to jumb across n waterlily leaves,
     * from leaf 1 to leaf n. The leaves vary in size and how stable they are,
     * so some leaves allow larger jumps than others.
     * @param leapNum array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i.
     * @return minimal no. of leaps to last leaf (integer).
     */
    public static int minLeap(int[] leapNum) {
        int previous = 0;
        int current = 0;
        int minJumps = 0;
        for (int i = 0 ; i < leapNum.length; i++) {
            if(i > previous) {
                minJumps = minJumps + 1;
                previous = current;
            }
            current = Math.max(current, i + leapNum[i]);
        }
        return minJumps;
    }

    /**
     * Method computing the solution to the following problem:
     * A boy is filling the water trough for his father's cows in their village.
     * The trough holds n liters of water. With every trip to the village well,
     * he can return using either the 2 bucket yoke, or simply with a single bucket.
     * A bucket holds 1 liter. In how many different ways can he fill the water trough?
     * @param n litres of water in trough, 0 <= n <= 45.
     * @return number of ways (integer).
     */
    public static int bucketWalk(int n) {
        int[] sums = new int[n + 1];
        if(n == 0 || n == 1){
            return 1;
        }
        if(n == 2){
            return 2;
        }
        sums[0] = 1;
        sums[1] = 1;
        sums[2] = 2;
        for (int j = 3; j < n + 1; j++){
            sums[j] = sums[j-1] + sums[j-2];
        }
        return sums[n];
    }

    /**
     * Method computing the solution to the following problem:
     * Given an integer n, return the number of structurally unique BST's
     * (binary search trees) which has exactly n nodes of unique values from 1 to n.
     * You can assume n is at least 1 and at most 19.
     * @param n number of nodes of unique values.
     * @return valid output of algorithm.
     */
    public static int numTrees(int n){
        int[] numbersOfTrees = new int[n+1];
        numbersOfTrees[0] = 1;
        for (int i = 0; i < n + 1; i++){
            for (int j = 0; j < i; j++){
                numbersOfTrees[i] += numbersOfTrees[j] * numbersOfTrees[i - j - 1];
            }
        }
        return numbersOfTrees[n];
    }
}
