
package quicktime.app.time;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;


public class TaskThread implements Runnable {
    private static final int PRIORITY_NOT_SET = -1;

    public static final void killAllThreads() {
        Enumeration e = threads.elements();
        while (e.hasMoreElements()) {
            TaskThread localTaskThread = (TaskThread) e.nextElement();
            localTaskThread.stop();
            localTaskThread.removeAll();
        }
        while (!threads.isEmpty()) {
            Thread.yield();
        }
    }

    public TaskThread(String paramString) {
        this(paramString, 100);
    }

    public TaskThread(String paramString, int paramInt) {
        this.sleepTime = paramInt;
        this.tName = paramString;
    }

    private Vector tasks = new Vector(12, 0);

    private Thread theThread = null;

    private int sleepTime;

    private String tName;

    private int threadPriority = 5;

    public int getSleepTime() {
        return this.sleepTime;
    }

    public void setSleepTime(int paramInt) {
        this.sleepTime = paramInt;
    }

    public boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject)) {
            return false;
        }
        if (this.tasks.contains(paramObject))
            return true;
        Taskable localTaskable = (Taskable) paramObject;
        if (localTaskable.getTasker() != null) {
            localTaskable.getTasker().removeMember(paramObject);
        }
        this.tasks.addElement(paramObject);

        localTaskable.addedToTasker(this);
        return true;
    }

    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Taskable;
    }

    public Enumeration members() {
        return this.tasks.elements();
    }

    public void addAndStart(Taskable paramTaskable) {
        addMember(paramTaskable);
        start();
    }

    public void removeMember(Object paramObject) {
        if (paramObject != null) {
            this.tasks.removeElement(paramObject);
        }

        ((Taskable) paramObject).removedFromTasker();
    }

    public void removeAll() {
        stop();
        synchronized (this) {
            for (Enumeration e = this.tasks.elements(); e.hasMoreElements();) {
                Taskable localTaskable = (Taskable) e.nextElement();
                localTaskable.removedFromTasker();
            }
            this.tasks.removeAllElements();
        }
    }

    public int size() {
        return this.tasks.size();
    }

    public boolean hasMember(Object paramObject) {
        return this.tasks.contains(paramObject);
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    public void start() {
        if (this.theThread == null) {
            if (QTSession.getJavaVersion() == 65537) {
                this.theThread = new Thread(this, this.tName);

            } else {

                class QTJPrivelegedAction {
                    Object doPrivelegedAction() {
                        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
                            public Object run() {
                                return new Thread(TaskThread.this, tName);
                            }
                        });
                    }
                }

                this.theThread = (Thread) new QTJPrivelegedAction().doPrivelegedAction();
            }

            if (this.threadPriority != -1)
                setPriority(this.threadPriority);
            this.threadIsRunning = true;
            this.theThread.start();
            threads.addElement(this);
        }
    }

    public void stop() {
        if (this.theThread != null) {
            synchronized (this.theThread) {
                this.threadIsRunning = false;

                this.theThread.resume();
                this.theThread.interrupt();
            }
            this.theThread = null;
        }
    }

    private boolean threadIsRunning = false;

    private static Vector threads = new Vector();

    public void suspend() {
        if (this.theThread != null)
            this.theThread.suspend();
    }

    public void resume() {
        if (this.theThread != null)
            this.theThread.resume();
    }

    public boolean isAlive() {
        return (this.theThread != null) && (this.theThread.isAlive());
    }

    public int getPriority() {
        return this.theThread != null ? this.theThread.getPriority() : this.threadPriority;
    }

    public void setPriority(int paramInt) {
        if (this.theThread != null)
            this.theThread.setPriority(paramInt);
        this.threadPriority = paramInt;
    }

    public void run() {
end:
        while (true) {
            if (this.threadIsRunning && this.tasks.size() > 0) {
                synchronized (this.theThread) {
                    if (!this.threadIsRunning)
                        break end;
                    for (int i = 0; i < this.tasks.size(); i++) {
                        try {
                            Taskable task = (Taskable) this.tasks.elementAt(i);

                            boolean isRunning = true;
                            try {
                                if (!this.threadIsRunning)
                                    break;
                                task.task();
                                isRunning = false;
                            } catch (QTException e) {
                                QTRuntimeException.handleOrThrow(new QTRuntimeException(e), task, "task");
                                isRunning = true;
                            } finally {
                                if (isRunning) {
                                    threads.removeElement(this);
                                    this.theThread = null;
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            break;
                        }
                    }
                }
                if (this.threadIsRunning)
                    try {
                        if (this.sleepTime < 1)
                            Thread.yield();
                        else {
                            Thread.sleep(this.sleepTime);
                        }
                    } catch (InterruptedException e) {
                        if (!this.threadIsRunning)
                            break end;
                    }
            }
        }
        threads.removeElement(this);
        this.theThread = null;
    }

    protected void finalize() throws Throwable {
        removeAll();
        super.finalize();
    }

    public String toString() {
        return getClass().getName() + "[name=" + this.tName + ",size=" + size() + ",priority=" + getPriority() + ",sleepTime="
               + this.sleepTime + "]";
    }
}
