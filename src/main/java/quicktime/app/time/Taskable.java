
package quicktime.app.time;

import quicktime.QTException;


abstract interface Taskable {
    public static final TaskThread tasker = new TaskThread("Taskable.tasker");

    public abstract void task() throws QTException;

    public abstract TaskThread getTasker();

    public abstract void addedToTasker(TaskThread paramTaskThread);

    public abstract void removedFromTasker();

    public abstract void startTasking();

    public abstract void stopTasking();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Taskable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
