package Task;

public class pc_dynamic {
    // 20만까지 테스트하기 위함
    private static final int NUM_END = 10;
    private static final int NUM_THREAD = 2;

    // 메인함수
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Task.pc_dynamic");
        Dynamic_Thread.counter = 0;
        // for 문에서 소수를 테스트하기 위한 변수
        int i;

        // 쓰레드 생성에 따른 시간 차이를 알기 위해 입력한 시간
        //long startTimeReal = System.currentTimeMillis();


        // 쓰레드 생성
        Dynamic_Thread[] thread = new Dynamic_Thread[NUM_THREAD];
        // 쓰레드에 시작값 할당
        // isPrime 는  처음 할당된 값들 이후부터 계산할 것이다.
        for (int t = 0; t < NUM_THREAD; t++) {
            // 배열은 0부터 시작이라 0에는 1을, 1에는 3을 할당하기 위해 1 추가.
            thread[t] = new Dynamic_Thread();
        }
        // 판단을 시작할 변수를 할당해줬다.
        Dynamic_Thread.isPrime = 1;

        //시작 시간을 측정하기 위한 변수
        long startTime = System.currentTimeMillis();
        // 소수인지 테스트
        // 쓰레드 시작
        for (int t = 0; t < NUM_THREAD; t++) {
            thread[t].start();
        }
        // 시간 측정을 위해 join 을 이용해 쓰레드가 끝날 때까지 대기
        for (int t = 0; t < NUM_THREAD; t++) {
            thread[t].join();
        }

        // 끝나는 시간을 측정하기 위한 변수
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        for (int t = 0; t < NUM_THREAD; t++) {
            System.out.println("Thread #" + t + " Execution Time : " + thread[t].timeDiff + "ms");
        }

        System.out.println("Execution Time : " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime# counter=" + Dynamic_Thread.counter + "\n");

    }

    // 소수 판별 함수. 판별하기 위한 숫자를 x로 받는다.
    private static boolean isPrime(int x){
        int i;
        if(x <= 1) return false;
        for(i = 2; i < x; i++){
            if(x % i == 0) return false;
        }
        return true;
    }

    static class Dynamic_Thread extends Thread {
        // 쓰레드에서 처음 판단할 숫자를 변수 x로 주었다.
        //  isPrime 에서 x를 할당 받아올 것이다.
        int x;
        // 소수 숫자를 세기 위해 counter 변수를 클래스 내에 선언해줬다.
        static int counter;
        // 소수인지 확인할 변수를 변수를 클래스 내에 선언해줬다.
        static int isPrime;
        // 쓰레드 끝난 후 더하기 위해 임시 변수 선언
        int temp = 0;
        // 쓰레드 시작 시간
        long startTime;
        // 쓰레드 종료 시간
        long endTime;
        // 쓰레드 실행 시간
        long timeDiff;

        public Dynamic_Thread() {
            this.x = 1;
        }

        public void run() {
            startTime = System.currentTimeMillis();
            while (isPrime < NUM_END) {
                for (int i = 0; i < 10; i++) {
                    if (this.x < NUM_END) {
                        if (isPrime(this.x)) temp++;
                        System.out.println(x);
                        this.x = update();
                    }
                }
            }

            // 클래스 내 counter 변수에 소수를 더한다.
            counter += temp;

            // 쓰레드 종료 시간
            endTime = System.currentTimeMillis();
            // 쓰레드 실행 시간
            timeDiff = endTime - startTime;
        }

        //여기를 synchronized 로 해줬다. x 를 isPrime 에서 가져오고 isPrime 는 2 더해준다.
        static synchronized int update() {
            isPrime = isPrime + 1;
            return isPrime;
        }
    }
}