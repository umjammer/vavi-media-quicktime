package quicktime.std.image;

import quicktime.QTException;
import quicktime.std.comp.Component;

/**
 * This class represents the QuickTime vectors. QuickTime vectors are
 * mathematical descriptions of images that are smaller in size than their
 * equivalent bitmaps and can be scaled without loss of image quality.
 *
 * Data for a QuickTime vector is stored in a QT atom container. The contents
 * are a stream of atoms. Each atom contains one of the following:
 * a specification of a path
 * a style specification for the path, such as the stroke width of the pen
 * used to draw the path
 * an atom that identifies the end of the data stream
 *
 * It should be noted that calls that need to have the data endian flipped is done internally
 * by QTJava for you, else its explicitly mentioned if the Endian flipping is the responsiblity of
 * the application.
 */
public final class Curve extends Component {

    /**
     * Opens the specified QTVector component subtype kVectorCodecType.
     * QuickTime::OpenADefaultComponent
     */
    public Curve() throws QTException {
        super(1768776803, 1885434984);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /Curve.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
