
package quicktime.app.view;

public interface DrawingNotifier {
    void addDrawingListener(DrawingListener drawingListener);

    void removeDrawingListener(DrawingListener drawingListener);
}
