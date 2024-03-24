package prob3;

import java.util.concurrent.ArrayBlockingQueue;



class My_Thread extends Thread{
    ArrayBlockingQueue<Integer> arrayBlockingQueue;

    My_Thread(ArrayBlockingQueue<Integer> arrayBlockingQueue){
        this.arrayBlockingQueue = arrayBlockingQueue;
        start();
    }

    @Override
    public void run() {

        try {
            sleep((long)(Math.random()) * 1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            arrayBlockingQueue.put(0);
            System.out.println("    안녕하세요 " + this.getName() + " 큐에 들어왔습니다. ");

            sleep((int)(Math.random() * 10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("                                            안녕히계세요 "+ this.getName() +" 큐에서 나갑니다. ");
            arrayBlockingQueue.take();

            sleep((int)(Math.random() * 10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ex1 {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(3);
        int NUM_THREAD = 6;
        My_Thread[] my_thread = new My_Thread[NUM_THREAD];
        for(int i = 0; i < NUM_THREAD; i++){
            my_thread[i] = new My_Thread(arrayBlockingQueue);
        }
        for(int i = 0; i < NUM_THREAD; i++){
            my_thread[i].join();
        }
        System.out.println("                모든 쓰레드가 들어왔다 나갔습니다.     ");
    }
}
