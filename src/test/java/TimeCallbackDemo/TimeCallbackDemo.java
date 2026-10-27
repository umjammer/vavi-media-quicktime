package TimeCallbackDemo;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;
import quicktime.app.time.Timer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.clocks.RateCallBack;
import quicktime.std.clocks.TimeBase;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.media.Media;

//Note : When using callback it should be noted that the callbacks need to be properly cleanedup when thier use is over.
// You should take proper care to just cancel the callback if you wish to reschedule it . To do this you should call just cancel() .
// You should take proper care to call cancelAndCleanup() when you are quitting the application itself, this will ensure proper clean up
// of any memory and thread hanging around.

/**
 * This sample code demonstrates how to use the various TimeCallBack features of QuickTime for Java
 * including Extremes, rate changes, and time callbacks
 */

public class TimeCallbackDemo extends Frame implements Errors {

    /* main entry point of the application */
    static void main(String[] args) {
        try {
            System.out.println("Time Base callback Demo - time callback *ONLY*");
            System.out.println("will work the first time through the movie as eventually");
            System.out.println("it is rescheduled past the end of the movie and there is");
            System.out.println("no logic to correct that in this code");

            QTSession.open();

            TimeCallbackDemo movieWindow = new TimeCallbackDemo("QT in Java");
            movieWindow.pack();
            movieWindow.setVisible(true);
            movieWindow.toFront();
        } catch (Exception ex) {
            ex.printStackTrace();
            QTSession.close();
        }

    }

    private QTComponent qtc;
    private TimeBaseRateCallBack theMoviesRateCallback;
    private TimeBaseExtremesCallBack theMoviesExtremeCallback;
    private TimeBaseTimeJumpCallBack theMoviesTimeJumpCallback;
    private TimeBaseTimeCallBack theMoviesTimeCallback;

    /**
     * Default constructor
     *
     * @param title the title of the window to be created
     */
    public TimeCallbackDemo(String title) throws QTException {
        super(title);

        createMovie();
        add((Component) qtc);
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                theMoviesRateCallback.cancelAndCleanup();
                theMoviesExtremeCallback.cancelAndCleanup();
                theMoviesTimeJumpCallback.cancelAndCleanup();
                theMoviesTimeCallback.cancelAndCleanup();

                QTSession.close();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Opens a movie from a file and installs the various movie callbacks
     */
    public void createMovie() throws QTException {
        Media m = null;

        QTFile qtf = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
        Movie mov = Movie.fromFile(OpenMovieFile.asRead(qtf));
        MovieController mc = new MovieController(mov);
        qtc = QTFactory.makeQTComponent(mc);

        TimeBase theMoviesTimeBase = mov.getTimeBase();

        theMoviesRateCallback = new TimeBaseRateCallBack(theMoviesTimeBase, 1.0F, StdQTConstants.triggerRateChange); // this callback is triggered
        theMoviesRateCallback.callMeWhen();                                                                             // when the rate of the movie changes

        theMoviesExtremeCallback = new TimeBaseExtremesCallBack(theMoviesTimeBase, StdQTConstants.triggerAtStop);    // this callback is triggered when the movie stops
        theMoviesExtremeCallback.callMeWhen();

        theMoviesTimeJumpCallback = new TimeBaseTimeJumpCallBack(theMoviesTimeBase);                    // this callback is triggered when there is a jump in the timebase
        theMoviesTimeJumpCallback.callMeWhen();

        // this schedules the time callback once every 2 seconds
        theMoviesTimeCallback = new TimeBaseTimeCallBack(theMoviesTimeBase, 1, 2, StdQTConstants.triggerTimeEither); // this callback is triggered at a specific time interval
        theMoviesTimeCallback.callMeWhen();

        //Using the Timer class you can get rescheduled properly and get callbacks at the set intervals. It uses the same callback
        //mechanism of internally of the TimeCallback.
        //Its recomended to use this Timer class to do callbacks , which would take care of the time base time changes and
        //recscheduling of the tickle method .
        Timer timer = new Timer(1, 2, new Tickler(), mov);
        timer.setActive(true);
    }


    /**
     * This class extends the RateCallBack class and provides an execute routine that is invoked by the callback
     * when the rate of the movie changes
     */

    public static class TimeBaseRateCallBack extends RateCallBack {

        public TimeBaseRateCallBack(TimeBase tb, float rate, int flag) throws QTException {
            super(tb, rate, flag);
        }

        @Override
        public void execute() {
            try {
                System.out.println("---  RateCallBack@ratechange---");
                cancel();
                callMeWhen();
            } catch (StdQTException e) {
            }
        }
    }

    /**
     * This class implements a method that is called when the movie stops
     */
    static class TimeBaseExtremesCallBack extends quicktime.std.clocks.ExtremesCallBack {

        /**
         * @param mov The movie to monitor.
         */
        public TimeBaseExtremesCallBack(TimeBase tb, int flag) throws QTException {
            super(tb, flag);
        }

        @Override
        public void execute() {
            try {
                System.out.println("---  ExtremesCallBack@stop---");
                cancel();
                callMeWhen();
            } catch (StdQTException e) {
            }
        }
    }

    /**
     * This class extends the TimeJumpCallBack class to provide a method that is called when the
     * timebase of the movie changes (IE, if the user clicks in the movie controller)
     */
    static class TimeBaseTimeJumpCallBack extends quicktime.std.clocks.TimeJumpCallBack {

        public TimeBaseTimeJumpCallBack(TimeBase tb) throws QTException {
            super(tb);
        }

        @Override
        public void execute() {
            try {
                System.out.println("---  TimeJumpCallBack---");
                cancel();
                callMeWhen();
            } catch (StdQTException e) {
            }
        }
    }

    /**
     * This class extends the TimeCallBack class to provide a method that is called when a
     * specific interval of time elapses.
     */
    static class TimeBaseTimeCallBack extends quicktime.std.clocks.TimeCallBack {

        public TimeBaseTimeCallBack(TimeBase tb, int scale, int value, int flags) throws QTException {
            super(tb, scale, value, flags);
            period = value;
            theTimeBase = tb;
        }

        int period;
        TimeBase theTimeBase;

        @Override
        public void execute() {
            try {
                System.out.println("---  TimeCallBack@triggerTimeEither--- called at:" + timeWhenCalledMsecs + "msecs");
                cancel();
                value += period;
                callMeWhen();
            } catch (StdQTException e) {
            }
        }
    }

    public static class Tickler implements Ticklish {

        @Override
        public void timeChanged(int newTime) throws QTException {
            System.out.println("* * * * Timer Class * * * timeChanged at:" + newTime);
        }

        @Override
        public boolean tickle(float er, int time) throws QTException {
            System.out.println("* * * * Timer Class  * * * tickle at:" + time);
            return true;
        }
    }

} 

 