package own.commen.code.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//例子3，lockInterruptibly()响应中断的使用方法：
public class TestReentrantLock {
    private Lock lock = new ReentrantLock();   
    public static void main(String[] args)  {
    	TestReentrantLock test = new TestReentrantLock();
        MyThread thread0 = new MyThread(test);
        MyThread thread1 = new MyThread(test);
        thread0.start();
        thread1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();
    }  
     
    public void insert(Thread thread) throws InterruptedException{
        lock.lockInterruptibly();   //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {  
            System.out.println(thread.getName()+"得到了锁");
            long startTime = System.currentTimeMillis();
            for(int i=0;i<1;i++) {
                if(System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                    break;
                System.out.println("插入数据");
            }
        }finally {
            System.out.println(Thread.currentThread().getName()+" 执行finally");
            lock.unlock();
            System.out.println(thread.getName()+"释放了锁");
        }  
    }
}
 
class MyThread extends Thread {
    private TestReentrantLock test = null;
    public MyThread(TestReentrantLock test) {
        this.test = test;
    }
    @Override
    public void run() {
        try {
            test.insert(Thread.currentThread());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"被中断");
        }
    }
}


