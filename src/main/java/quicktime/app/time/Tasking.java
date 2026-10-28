package quicktime.app.time;

/**
 * This class implements most of the methods of the Taskable interface,
 * providing an application a default implementation of this.
 * The default Tasker for this object can be set by an application and
 * doing so will move this Taskable object to that new TaskThread object and
 * consequent calls to startTasking() will use the application set TaskThread.
 */
public abstract class Tasking implements Taskable {

    private TaskThread tt;

    private TaskThread defTasker = Taskable.tasker;

    /**
     * This is called by a TaskThread when an object is removed from a TaskThread.
     * It is removedFrom the TaskThread that it was added to and a Taskable
     * can only be in one TaskThread object.
     */
    @Override
    public void removedFromTasker() {
        this.tt = null;
    }

    /**
     * This is called by a TaskThread when an object is added to a TaskThread.
     * The parameter is the TaskThread that the object is being added to.
     * @param t the TaskThread
     */
    @Override
    public void addedToTasker(TaskThread t) {
        this.tt = t;
    }

    /**
     * This should return the task thread that the Taskable object is currently being
     * tasked by or null if not being tasked.
     * @return a TaskThread object or null.
     */
    @Override
    public TaskThread getTasker() {
        return this.tt;
    }

    /**
     * This will start tasking the Tasking object by adding this object to
     * the currently set default TaskThread and ensuring that this TaskThread
     * object is started.
     */
    @Override
    public void startTasking() {
        this.defTasker.addAndStart(this);
    }

    /**
     * This will stop tasking the Tasking object by removing it from its existing
     * TaskThread.
     */
    @Override
    public void stopTasking() {
        if (getTasker() != null) {
            getTasker().removeMember(this);
        }
    }

    /**
     * This will set the default TaskThread that will be used by this object when
     * the startTasking method is called. If the object is currently attached to a TaskThread
     * this will remove it from that Thread and add it to the incoming TaskThread. It won't
     * start the incoming TaskThread
     * @param tasker the TaskThread to use for tasking
     */
    public void setDefaultTasker(TaskThread tasker) {
        if (tasker == null) {
            this.defTasker = Taskable.tasker;
        } else {
            this.defTasker = tasker;
        }
        if ((this.tt != null) && (this.tt != this.defTasker)) {
            this.tt.removeMember(this);
            this.defTasker.addAndStart(this);
        }
    }

    /**
     * Returns the default TaskThread that is used when this object is tasked.
     * @return a TaskThread object
     */
    public TaskThread getDefaultTasker() {
        return this.defTasker;
    }
}

