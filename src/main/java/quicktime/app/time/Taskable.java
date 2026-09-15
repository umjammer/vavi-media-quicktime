
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


