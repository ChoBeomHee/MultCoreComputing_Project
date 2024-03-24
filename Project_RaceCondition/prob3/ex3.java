package prob3;

import java.util.concurrent.atomic.AtomicInteger;

class ex3_Thread extends Thread {
    AtomicInteger atomicInteger;
    ex3_Thread(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
        start();
    }

    @Override
    public void run() {
        while (atomicInteger.get() < 30) {
            try {
                System.out.println(getName() + " : " + atomicInteger.getAndAdd(1));
                System.out.println(getName() + " : " + atomicInteger.addAndGet(1));

                sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class ex3 {
    public static void main(String[] args) throws InterruptedException {
        int ATOMIC_NUM = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(ATOMIC_NUM);

        int NUM_TREAD = 4;
        ex3_Thread[] ex3_threads = new ex3_Thread[NUM_TREAD];

        for(int i = 0; i < NUM_TREAD; i++)
            ex3_threads[i] = new ex3_Thread(atomicInteger);

        for(int i = 0; i < NUM_TREAD; i++)
            ex3_threads[i].join();
        System.out.println("final Value " + atomicInteger.get());
    }
}