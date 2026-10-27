/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.app.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.Timer;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;


/**
 * A Java 2D component showing a movie, a movie controller or an image.
 * <p>
 * The movie is drawn into an offscreen QDGraphics that is painted, a timer tasks the movie or idles the controller.
 * Mouse clicks and keys go to the movie controller.
 */
class QTJavaJ2DCanvas extends JComponent implements QTComponent {

    private static final Logger logger = Logger.getLogger(QTJavaJ2DCanvas.class.getName());

    private Movie movie;
    private MovieController controller;
    private GraphicsImporter image;
    private QDGraphics port;
    private final Timer timer;
    private int width = 1;
    private int height = 1;

    QTJavaJ2DCanvas() {
        timer = new Timer(1000 / 60, e -> tick());
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                if (controller != null) {
                    try {
                        controller.click(new QDPoint(e.getX(), e.getY()), modifiers(e));
                    } catch (QTException x) {
                        logger.log(Level.FINE, x.getMessage(), x);
                    }
                    repaint();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (controller != null) {
                    try {
                        controller.key(e.getKeyCode() == KeyEvent.VK_SPACE ? ' ' : e.getKeyCode(), modifiers(e));
                    } catch (QTException x) {
                        logger.log(Level.FINE, x.getMessage(), x);
                    }
                    repaint();
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0 && (getWidth() != width || getHeight() != height)) {
                    resizeTo(getWidth(), getHeight());
                }
            }
        });
    }

    QTJavaJ2DCanvas(Movie movie) throws QTException {
        this();
        setMovie(movie);
    }

    QTJavaJ2DCanvas(MovieController controller) throws QTException {
        this();
        setMovieController(controller);
    }

    QTJavaJ2DCanvas(GraphicsImporter image) throws QTException {
        this();
        setImage(image);
    }

    /** QTJava modifiers: 1 shift, 2 control, 4 command, 8 option */
    private static int modifiers(InputEvent e) {
        int m = 0;
        if (e.isShiftDown()) {
            m |= 1;
        }
        if (e.isControlDown()) {
            m |= 2;
        }
        if (e.isMetaDown()) {
            m |= 4;
        }
        if (e.isAltDown()) {
            m |= 8;
        }
        return m;
    }

    private void tick() {
        if (!QTSession.isInitialized()) {
            timer.stop();
            return;
        }
        try {
            if (controller != null) {
                controller.idle();
            } else if (movie != null) {
                movie.task(0);
            }
        } catch (QTException | RuntimeException e) {
            logger.log(Level.FINE, e.getMessage(), e);
            timer.stop();
        }
        repaint();
    }

    /** a new port of the size, the movie or controller is laid out into it */
    private void resizeTo(int w, int h) {
        try {
            width = Math.max(1, w);
            height = Math.max(1, h);
            QDGraphics old = port;
            port = new QDGraphics(new QDRect(0, 0, width, height));
            if (controller != null) {
                controller.setPort(port);
                controller.setBounds(new QDRect(0, 0, width, height));
            } else if (movie != null) {
                movie.setGWorld(port, null);
                movie.setBounds(new QDRect(0, 0, width, height));
            } else if (image != null) {
                image.setGWorld(port, null);
                image.setBoundsRect(new QDRect(0, 0, width, height));
                image.draw();
            }
            if (old != null) {
                old.disposeQTObject();
            }
        } catch (QTException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private void start(QDRect bounds) {
        width = Math.max(1, bounds.getWidth());
        height = Math.max(1, bounds.getHeight());
        setPreferredSize(new Dimension(width, height));
        resizeTo(width, height);
        timer.start();
    }

    @Override
    public void setMovie(Movie movie) throws QTException {
        this.controller = null;
        this.image = null;
        this.movie = movie;
        if (movie != null) {
            start(movie.getBox());
        } else {
            timer.stop();
        }
        revalidate();
        repaint();
    }

    @Override
    public void setMovieController(MovieController controller) throws QTException {
        this.image = null;
        this.controller = controller;
        this.movie = controller != null ? controller.getMovie() : null;
        if (controller != null) {
            start(controller.getBounds());
        } else {
            timer.stop();
        }
        revalidate();
        repaint();
    }

    @Override
    public void setImage(GraphicsImporter image) throws QTException {
        this.controller = null;
        this.movie = null;
        this.image = image;
        timer.stop();
        if (image != null) {
            QDRect r = image.getBoundsRect();
            width = Math.max(1, r.getWidth());
            height = Math.max(1, r.getHeight());
            setPreferredSize(new Dimension(width, height));
            resizeTo(width, height);
        }
        revalidate();
        repaint();
    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public MovieController getMovieController() {
        return controller;
    }

    @Override
    public GraphicsImporter getImage() {
        return image;
    }

    @Override
    public Component asComponent() {
        return this;
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (movie != null || controller != null) {
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage img = port != null ? QuickDrawManager.portImage(QTObject.ID(port)) : null;
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
