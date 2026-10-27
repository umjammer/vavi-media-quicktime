
package quicktime.app.time;

import quicktime.QTException;


interface Taskable {
    TaskThread tasker = new TaskThread("Taskable.tasker");

    void task() throws QTException;

    TaskThread getTasker();

    void addedToTasker(TaskThread paramTaskThread);

    void removedFromTasker();

    void startTasking();

    void stopTasking();
}


