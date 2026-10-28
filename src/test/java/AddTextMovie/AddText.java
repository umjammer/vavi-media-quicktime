/*
 * QuickTime for Java SDK Sample Code

   Usage subject to restrictions in SDK License Agreement
 * Copyright: © 2000-2002 Apple Computer, Inc.

 */

package AddTextMovie;

import java.awt.Frame;

import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDColor;
import quicktime.qd.QDConstants;
import quicktime.qd.QDFont;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Movie;
import quicktime.std.movies.TimeInfo;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.TextMedia;
import quicktime.std.movies.media.TextMediaHandler;
import quicktime.util.QTPointer;


public class AddText extends Frame {

    static void main(String[] args) {
        new AddText();
    }

    public AddText() {
        try {
            QTSession.open();

            QTFile qtf = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
            DataRef urlMovie = new DataRef("file://" + qtf.getPath());
            Movie m = Movie.fromDataRef(urlMovie, StdQTConstants.newMovieActive);

            float textTrackHeight = 32;

            QDRect movieBounds = m.getNaturalBoundsRect();
            float movieWidth = movieBounds.getWidthF();
            float movieHeight = movieBounds.getHeightF();

            Track textTrack = m.addTrack(movieWidth, textTrackHeight, 0 /* no volume */);

            Matrix textTrackMatrix = textTrack.getMatrix();
            textTrackMatrix.translate(0, movieHeight - textTrackHeight);
            textTrack.setMatrix(textTrackMatrix);

            textTrack.setEnabled(true);

            int movieTimeScale = m.getTimeScale();
            TextMedia textMedia = new TextMedia(textTrack, movieTimeScale);

            QDRect textBounds = new QDRect(movieWidth, movieHeight);

            textMedia.beginEdits();

            TimeInfo sampleTime = new TimeInfo(0, m.getDuration());
   
            String text = "This is a big fat hairy test\rAnd so is this";
            TextMediaHandler textMediaHandler = textMedia.getTextHandler();
            QTPointer textPointer = new QTPointer(text.length() + 1, true);
            textPointer.copyFromArray(0, text.getBytes(), 0, text.length());
            textMediaHandler.addTextSample(
                    textPointer,
                    QDFont.getFNum("Times"),
                    10,
                    0,
                    QDColor.black,
                    QDColor.white,
                    QDConstants.teCenter,
                    textBounds,
                    StdQTConstants.dfClipToTextBox | StdQTConstants.dfKeyedText,
                    0, 0, 0,
                    null,
                    sampleTime.duration);

            textMedia.endEdits();

            textTrack.insertMedia(sampleTime.time, 0, sampleTime.duration, 1);
            OpenMovieFile outStream = OpenMovieFile.asWrite(qtf);

            m.updateResource(outStream, StdQTConstants.movieInDataForkResID, qtf.getName());
        } catch (Exception e) {
            e.printStackTrace();
            QTSession.close();
            System.exit(0);
        }

        QTSession.close();
        System.out.println("Done.");
        System.exit(0);
    }
}