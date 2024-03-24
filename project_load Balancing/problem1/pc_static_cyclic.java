package Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class pc_static_cyclic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 32;
    private static int task = 10;
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        ArrayList<Integer> arrayLists = new ArrayList<>();
        My_Thread[] my_thread = new My_Thread[NUM_THREADS];
        long startTime = System.currentTimeMillis();

        System.out.println("Task.pc_static_cyclic");

        for(int i = 0; i < NUM_THREADS; i++){
            my_thread[i] = new My_Thread(i);
        }
        int index = 0;
        for(int i = 1; i <= NUM_END; i++) {
            arrayLists.add(i);
            if (i % task == 0) { // task 갯수 만큼 채워짐
                if (index == NUM_THREADS) {
                    index = 0;
                }
                my_thread[index].setRange(arrayLists);
                index++;
                arrayLists = new ArrayList<>();
            }
        }
        for(int i = 0; i < NUM_THREADS; i++){
            my_thread[i].start();
        }
        for(int i = 0; i < NUM_THREADS; i++){
            my_thread[i].join();
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("total Program Execution Time : " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime # count " + counter);

    }

    private static boolean isPrime(int x){
        int i;
        if(x <= 1) return false;
        for(i = 2; i < x; i++){
            if(x % i == 0) return false;
        }
        return true;
    }

    public static class My_Thread extends Thread{
        ArrayList<ArrayList<Integer>> range = new ArrayList<ArrayList<Integer>>();
        int Thread_num;
        int cnt = 0;

        My_Thread(int Thread_num){
            this.Thread_num = Thread_num;
        }
        void setRange(ArrayList<Integer> subArray){
            this.range.add(subArray);
        }

        public void run(){
            long startTime = System.currentTimeMillis();
            for(int i = 0; i < this.range.size(); i++){
                for(int j = 0; j < this.range.get(i).size(); j++){
                    if(isPrime(range.get(i).get(j))) cnt++;
                }
            }
            counter += cnt;
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println("Thread #" + Thread_num +" Execution Time : " + timeDiff + "ms");
        }
    }
}