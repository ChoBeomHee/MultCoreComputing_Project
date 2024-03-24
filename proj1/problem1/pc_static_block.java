package Task;

public class pc_static_block {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 14;
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        int block_size = NUM_END / NUM_THREADS;
        My_Thread[] my_thread = new My_Thread[NUM_THREADS];

        System.out.println("Task.pc_static_block");

        int i;
        int idx = 1;
        long startTime = System.currentTimeMillis();
        for(i = 0; i < NUM_THREADS; i++){
            if(i == NUM_THREADS - 1){
                my_thread[i] = new My_Thread(i, idx, NUM_END);
            }
            else {
                my_thread[i] = new My_Thread(i, idx, idx + block_size - 1);
                idx += block_size;
            }
        }
        for(i = 0; i < NUM_THREADS; i++){
            my_thread[i].start();
        }
        for(i = 0; i < NUM_THREADS; i++){
            my_thread[i].join();
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("total Program Excution Time : " + timeDiff + "ms");
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
        int Thread_num;
        int start_num;
        int end_num;
        int cnt = 0;

        My_Thread(int Thread_num, int start_num, int end_num){
            this.Thread_num = Thread_num;
            this.start_num = start_num;
            this.end_num = end_num;
        }

        public void run(){
            long startTime = System.currentTimeMillis();
            for(int i = start_num; i < end_num; i++){
                if(isPrime(i)) cnt++;
            }
            counter += cnt;
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;

            System.out.println("Thread #" + Thread_num +" Execution Time : " + timeDiff + "ms");
        }
    }
}