package prob3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ex4 { // CyclicBarrier
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        int THREAD_NUM = 8;
        ex4_Thread[] ex4_threads = new ex4_Thread[THREAD_NUM];
        for(int i = 0; i < THREAD_NUM; i++){
            ex4_threads[i] = new ex4_Thread(cyclicBarrier);
        }
        for(int i = 0; i < THREAD_NUM; i++){
            ex4_threads[i].join();
        }
    }
}

class ex4_Thread extends Thread{
    CyclicBarrier cyclicBarrier;

    ex4_Thread(CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
        start();
    }

    @Override
    public void run() {
        try {
            sleep((long)(Math.random() * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(getName() + " CyclicBarrier reach.");
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        System.out.println("                                                        Two Thread's reach, CyclicBarrier Finish!");
    }
}
