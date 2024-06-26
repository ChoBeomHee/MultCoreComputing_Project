package prob2;


import java.util.concurrent.Semaphore;

class ParkingGarage {
    private Semaphore semaphore;
    public ParkingGarage(int places) {
        if (places < 0)
            places = 0;
        this.semaphore = new Semaphore(places);
    }
    public void enter() throws InterruptedException { // enter parking garage
        this.semaphore.acquire();
    }
    public void leave() { // leave parking garage
        this.semaphore.release();
    }
    public int getPlaces() {
        return semaphore.availablePermits();
    }
}


class Car extends Thread {
    private ParkingGarage parkingGarage;
    public Car(String name, ParkingGarage p) {
        super(name);
        this.parkingGarage = p;
        start();
    }

    private void tryingEnter()
    {
        System.out.println(getName()+": trying to enter");
    }


    private void justEntered()
    {
        System.out.println(getName()+": just entered");

    }

    private void aboutToLeave()
    {
        System.out.println(getName()+":                                     about to leave");
    }

    private void Left()
    {
        System.out.println(getName()+":                                     have been left");
    }

    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking
            } catch (InterruptedException e) {}
            tryingEnter();
            try {
                parkingGarage.enter();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            justEntered();
            try {
                sleep((int)(Math.random() * 20000)); // stay within the parking garage
            } catch (InterruptedException e) {}
            aboutToLeave();
            parkingGarage.leave();
            Left();

        }
    }
}


public class ParkingSemaphore {
    public static void main(String[] args){
        ParkingGarage parkingGarage = new ParkingGarage(7);
        for (int i=1; i<= 10; i++) {
            Car c = new Car("Car "+i, parkingGarage);
        }
    }
}

