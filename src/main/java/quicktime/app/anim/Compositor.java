
package quicktime.app.anim;

import java.util.Enumeration;
import java.util.Hashtable;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.Layerable;
import quicktime.app.image.Compositable;
import quicktime.app.image.ImageSpec;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Protocol;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public class Compositor extends SWCompositor {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static Protocol defaultProtocol = makeP();

    private static Protocol makeP() {
        try {
            return new Protocol(Class.forName("quicktime.app.image.ImageSpec"));
        } catch (ClassNotFoundException localClassNotFoundException) {
            throw new QTRuntimeException(localClassNotFoundException.getMessage());
        }
    }

    /**
     * @deprecated
     */
    public Compositor(QDGraphics paramQDGraphics, QDColor paramQDColor) throws QTException {
        this(paramQDGraphics, paramQDColor, null, 10, 1, defaultProtocol);
    }

    /**
     * @deprecated
     */
    public Compositor(QDGraphics paramQDGraphics1, QDColor paramQDColor, QDGraphics paramQDGraphics2) throws QTException {
        this(paramQDGraphics1, paramQDColor, paramQDGraphics2, 10, 1, defaultProtocol);
    }

    /**
     * @deprecated
     */
    public Compositor(QDGraphics paramQDGraphics, QDColor paramQDColor, int paramInt1, int paramInt2) throws QTException {
        this(paramQDGraphics, paramQDColor, null, paramInt1, paramInt2, defaultProtocol);
    }

    /**
     * @deprecated
     */
    public Compositor(QDGraphics paramQDGraphics1,
            QDColor paramQDColor,
            QDGraphics paramQDGraphics2,
            int paramInt1,
            int paramInt2) throws QTException {
        this(paramQDGraphics1, paramQDColor, paramQDGraphics2, paramInt1, paramInt2, defaultProtocol);
    }

    /**
     * @deprecated
     */
    protected Compositor(QDGraphics paramQDGraphics1,
            QDColor paramQDColor,
            QDGraphics paramQDGraphics2,
            int paramInt1,
            int paramInt2,
            Protocol paramProtocol) throws QTException {
        super(paramQDGraphics1, paramQDColor, paramQDGraphics2, paramInt1, paramInt2, paramProtocol);
    }

    /**
     * @deprecated
     */
    protected Hashtable membersTable = new Hashtable();

    /**
     * @deprecated
     */
    protected Hashtable spritesTable = new Hashtable();

    /**
     * @deprecated
     */
    public Enumeration members() {
        return this.membersTable.keys();
    }

    /**
     * @deprecated
     */
    public Enumeration sprites() {
        return this.spritesTable.keys();
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.membersTable.isEmpty();
    }

    /**
     * @deprecated
     */
    public TwoDSprite getMemberSprite(Object paramObject) {
        if (((paramObject instanceof TwoDSprite)) && (this.spritesTable.containsKey(paramObject))) {
            return (TwoDSprite) paramObject;
        }
        Object localObject = this.membersTable.get(paramObject);
        return localObject != null ? (TwoDSprite) localObject : null;
    }

    /**
     * @deprecated
     */
    public Object getSpriteMember(TwoDSprite paramTwoDSprite) {
        return this.spritesTable.get(paramTwoDSprite);
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject) throws QTException {
        int i = 1;
        if ((paramObject instanceof Layerable))
            i = ((Layerable) paramObject).getLayer();
        boolean bool = true;
        if ((paramObject instanceof TwoDSprite)) {
            bool = ((TwoDSprite) paramObject).getVisible();
        } else if ((paramObject instanceof TwoDSpriteInfo))
            bool = ((TwoDSpriteInfo) paramObject).getVisible();
        return addMember(paramObject, i, bool);
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject, int paramInt) throws QTException {
        boolean bool = true;
        if ((paramObject instanceof TwoDSprite)) {
            bool = ((TwoDSprite) paramObject).getVisible();
        } else if ((paramObject instanceof TwoDSpriteInfo))
            bool = ((TwoDSpriteInfo) paramObject).getVisible();
        return addMember(paramObject, paramInt, bool);
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject, int paramInt, boolean paramBoolean) throws QTException {
        if (!isAppropriate(paramObject))
            return false;
        if (hasMember(paramObject)) {
            return true;
        }
        TwoDSprite localTwoDSprite = null;
        if ((paramObject instanceof TwoDSprite)) {
            localTwoDSprite = (TwoDSprite) paramObject;
            localTwoDSprite.setLayer(paramInt);
            localTwoDSprite.setVisible(paramBoolean);
        } else {
            ImageSpec localImageSpec = (ImageSpec) paramObject;

            GraphicsMode localGraphicsMode = null;
            if ((paramObject instanceof Compositable))
                localGraphicsMode = ((Compositable) paramObject).getGraphicsMode();
            Matrix localMatrix;
            if ((paramObject instanceof Transformable)) {
                localMatrix = ((Transformable) paramObject).getMatrix();
            } else
                localMatrix = new Matrix();
            localTwoDSprite = new TwoDSprite(localImageSpec, localMatrix, paramBoolean, paramInt, localGraphicsMode);
        }
        this.membersTable.put(paramObject, localTwoDSprite);
        this.spritesTable.put(localTwoDSprite, paramObject);
        postProcessAdd(paramObject, localTwoDSprite);

        return true;
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.membersTable.size();
    }

    /**
     * @deprecated
     */
    public int getFrontLayer() throws QTException {
        Enumeration localEnumeration = sprites();
        int i = Integer.MAX_VALUE;
        while (localEnumeration.hasMoreElements()) {
            TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
            int j = localTwoDSprite.getLayer();
            if (j < i)
                i = j;
        }
        return i;
    }

    /**
     * @deprecated
     */
    public int getBackLayer() throws QTException {
        Enumeration localEnumeration = sprites();
        int i = Integer.MIN_VALUE;
        while (localEnumeration.hasMoreElements()) {
            TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
            int j = localTwoDSprite.getLayer();
            if (j > i)
                i = j;
        }
        return i;
    }

    /**
     * @deprecated
     */
    public void removeMember(Object paramObject) throws QTException {
        if (this.membersTable.containsKey(paramObject)) {
            TwoDSprite localTwoDSprite = (TwoDSprite) this.membersTable.remove(paramObject);
            this.spritesTable.remove(localTwoDSprite);
            postProcessRemove(paramObject, localTwoDSprite);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * Compositor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
