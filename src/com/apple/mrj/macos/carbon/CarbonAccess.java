
package com.apple.mrj.macos.carbon;

import quicktime.QTSession;


public class CarbonAccess {
    public static Object invokeCarbonEventClient(final CarbonEventClient call) {
        class Runner implements Runnable {
            public Object fResult;

            public void run() {
                try {
                    if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() == 65539) {
                        CarbonLock.acquire();
                    }
                    this.fResult = call.invoke();
                } finally {
                    if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() == 65539) {
                        CarbonLock.release();
                    }
                }
            }
        };

        Runner runner = new Runner();
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() == 65539) {
            Timer.startOneShotAndBlock(runner);
        } else if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {
            Timer.startOneShotAndBlock(runner);
        }

        return runner.fResult;
    }
}
