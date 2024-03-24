package prob3;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static prob3.ex2.cnt;

class ex2_Thread extends Thread {
    private final ReadWriteLock readWriteLock;

    ex2_Thread(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        start();
    }
    void modify() {
        try {
            sleep((int)(Math.random() * 1000));
            System.out.println(getName() + " : Try Get WriteLock ");
            readWriteLock.writeLock().lock();
            System.out.println(getName() + " : Get the WriteLock, Now count : " + cnt);
            cnt++;
            sleep((int)(Math.random() * 1000));
            System.out.println(getName() + " : Lose the WriteLock");
            readWriteLock.writeLock().unlock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void getData() {
        try {
            sleep((int)(Math.random() * 1000));
            System.out.println("                                                            " + getName() + " : Try Get ReadLock ");

            readWriteLock.readLock().lock();
            System.out.println("                                                            " + getName() + "  Get the ReadLock, Now count :" + cnt);
            sleep((int) (Math.random() * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        readWriteLock.readLock().unlock();
    }

    @Override
    public void run() {
        while (cnt < 10) {
                getData();
            if(cnt < 10)
                modify();

        }
    }
}

public class ex2 {
    public static int cnt = 0;
    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        int NUM_THREAD = 4;
        ex2_Thread[] ex2_thread = new ex2_Thread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            ex2_thread[i] = new ex2_Thread(readWriteLock);
        }
        for (int i = 0; i < NUM_THREAD; i++) {
            ex2_thread[i].join();
        }

        System.out.println("                    final Count : " + cnt);
    }
}
