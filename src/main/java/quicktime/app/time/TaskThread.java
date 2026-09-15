package quicktime.app.time;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * The TaskThread class maintains a table of Taskable objects that will periodically
 * have their task method called. Each time an instance of this class is created
 * a new thread is spawned when the start method of the TaskThread object is called.
 * The thread is stopped when the stop method is called. Taskable objects can be added
 * or removed from TaskThreads at the applications discretion.
 *
 * When the thread is running it calls its Taskable member's task method. When it has
 * completed a single iteration of its members it will sleep for the specified
 * sleepTime and then go through the process again until the thread is stopped.
 *
 * A TaskThread object must be explicitly started - just adding a Taskable object
 * to the collection is not sufficient to start the thread. However, once all
 * of the Taskable objects are removed from the collection the thread will stop.
 * To ensure that adding a Taskable object to a TaskThread will also have the
 * thread started the addAndStart method can be used.
 *
 * If a member's task() method throws an Exception it is uncerimoniously removed from
 * the TaskThread collection - no notification is given of such an occurance.
 */
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

    /**
     * Creates a TaskThread object. When the object's start method is called
     * the supplied name will be the name of the thread that is created by
     * the task thread object. The default sleep time for a TaskThread's thread
     * is 100 msecs.
     * @param tName the name of the thread
     */
    public TaskThread(String paramString) {
        this(paramString, 100);
    }

    /**
     * Creates a TaskThread object. When the object's start method is called
     * the supplied name will be the name of the thread that is created by
     * the task thread object.
     * @param sleepTime sets the sleep time that will be used when the thread starts
     */
    public TaskThread(String paramString, int paramInt) {
        this.sleepTime = paramInt;
        this.tName = paramString;
    }

    private Vector tasks = new Vector(12, 0);

    private Thread theThread = null;

    private int sleepTime;

    private String tName;

    private int threadPriority = 5;

    /**
     * The time that the thread will sleep.
     */
    public int getSleepTime() {
        return this.sleepTime;
    }

    /**
     * The sleepTime for the thread. If the sleepTime is set to zero, the thread will only
     * yield (but not sleep), otherwise it sleeps for the given time.
     */
    public void setSleepTime(int paramInt) {
        this.sleepTime = paramInt;
    }

    /**
     * This method will add a taskable client to the list. It will not alter the state of the thread.
     */
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

    /**
     * Returns true if the object is an instance of the Taskable interface.
     * @return a boolean
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Taskable;
    }

    /**
     * Returns an Enumeration for the current Taskable members of the collection.
     * @return an Enumeration
     */
    public Enumeration members() {
        return this.tasks.elements();
    }

    /**
     * This method will add a taskable client to the list.
     * If the thread was stopped (because there were no objects to task) this will start the thread up.
     * @param t the Taskable object to add to the collection.
     */
    public void addAndStart(Taskable paramTaskable) {
        addMember(paramTaskable);
        start();
    }

    /**
     * Removes the Taskable object from the collection. If this would leave
     * the collection empty then the internal thread is stopped.
     */
    public void removeMember(Object paramObject) {
        if (paramObject != null) {
            this.tasks.removeElement(paramObject);
        }
        ((Taskable) paramObject).removedFromTasker();
    }

    /**
     * Remove all task objects from the task list and stops the task thread.
     */
    public void removeAll() {
        stop();
        synchronized (this) {
            for (Enumeration e = this.tasks.elements(); e.hasMoreElements(); ) {
                Taskable localTaskable = (Taskable) e.nextElement();
                localTaskable.removedFromTasker();
            }
            this.tasks.removeAllElements();
        }
    }

    /**
     * Return the number of task current objects.
     * @return an int
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Return true if the Taskable object is currently a member of the task list.
     * @param object the object to test if a member
     * @return true if the object is a member
     */
    public boolean hasMember(Object paramObject) {
        return this.tasks.contains(paramObject);
    }

    /**
     * Returns true if there are no Taskable objects in the collection
     * @return a boolean
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * This will create a thread and start it running, calling task on each of the items
     * you have added to the TaskThread object. If a thread is already active this call has no effect
     */
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

    /**
     * This will destroy the thread and end the periodic calling of task objects.
     * You can restart this object later by calling start.
     * The priority and sleepTime will be persistent across
     * start and stop calls, as will the task objects themselves.
     */
    public void stop() {
        if (this.theThread != null) {
            synchronized (this.theThread) {
                this.threadIsRunning = false;
                resume();
                this.theThread.interrupt();
            }
            this.theThread = null;
        }
    }

    private boolean threadIsRunning = false;

    private volatile boolean suspended = false;

    private static Vector threads = new Vector();

    /**
     * Suspends the execution of the task thread.
     */
    public synchronized void suspend() {
        this.suspended = true;
    }

    /**
     * Resumes the execution of the task thread.
     */
    public synchronized void resume() {
        if (this.suspended) {
            this.suspended = false;
            notifyAll();
        }
    }

    /**
     * Returns the alive state of the thread.
     */
    public boolean isAlive() {
        return (this.theThread != null) && (this.theThread.isAlive());
    }

    /**
     * Returns the current priority of the task thread.
     * @return the task thread priority or PRIORITY_NOT_SET if the TaskThread object has
     *  not had a priority explicitly set.
     */
    public int getPriority() {
        return this.theThread != null ? this.theThread.getPriority() : this.threadPriority;
    }

    /**
     * Sets the priority of the thread.
     * @param newPriority the new priority of the thread or any consequent threads created.
     */
    public void setPriority(int paramInt) {
        if (this.theThread != null)
            this.theThread.setPriority(paramInt);
        this.threadPriority = paramInt;
    }

    /**
     * Calls task on each of the task object in the internal task list. If an object's
     * task call fails it is removed from the collection.
     */
    public void run() {
        end: while (true) {
            synchronized (this) {
                while (this.suspended && this.threadIsRunning) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        if (!this.threadIsRunning)
                            break end;
                    }
                }
            }
            if (!this.threadIsRunning)
                break end;
            if (this.tasks.size() > 0) {
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

    /**
     * java.lang.Throwable
     */
    protected void finalize() throws Throwable {
        removeAll();
        super.finalize();
    }

    /**
     * String representation of the class.
     */
    public String toString() {
        return getClass().getName() + "[name=" + this.tName + ",size=" + size() + ",priority=" + getPriority() + ",sleepTime=" + this.sleepTime + "]";
    }
}
