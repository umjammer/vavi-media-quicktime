
package quicktime;

import com.apple.mrj.MRJApplicationUtils;


final class MacDoQuit implements com.apple.mrj.MRJQuitHandler {
    MacDoQuit() {
        MRJApplicationUtils.registerQuitHandler(this);
    }

    public void handleQuit() {
        QTSession.close();
        System.exit(0);
    }
}
