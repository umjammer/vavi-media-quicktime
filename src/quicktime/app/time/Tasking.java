
package quicktime.app.time;

public abstract class Tasking implements Taskable {
    private TaskThread tt;

    private TaskThread defTasker = Taskable.tasker;

    public void removedFromTasker() {
        this.tt = null;
    }

    public void addedToTasker(TaskThread paramTaskThread) {
        this.tt = paramTaskThread;
    }

    public TaskThread getTasker() {
        return this.tt;
    }

    public void startTasking() {
        this.defTasker.addAndStart(this);
    }

    public void stopTasking() {
        if (getTasker() != null) {
            getTasker().removeMember(this);
        }
    }

    public void setDefaultTasker(TaskThread paramTaskThread) {
        if (paramTaskThread == null) {
            this.defTasker = Taskable.tasker;
        } else {
            this.defTasker = paramTaskThread;
        }
        if ((this.tt != null) && (this.tt != this.defTasker)) {
            this.tt.removeMember(this);
            this.defTasker.addAndStart(this);
        }
    }

    public TaskThread getDefaultTasker() {
        return this.defTasker;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Tasking.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
