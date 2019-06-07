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
 * Deprecated.
 * The Compositor uses the SpriteWorld compositing services of the SWCompositor to composit
 * an image out of its member objects. The member object of a Compositor is required to at least
 * implement the ImageSpec interface. In which case the Compositor will create the TwoDSprite
 * that presents that image data in its display space. If a TwoDSprite itself is added to the
 * Compositor it is added directly as a member (as TwoDSprite implements the ImageSpec interface.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a Compositor with a background color. It
     * has a default scale of 10 fps at a rate of 1.
     * @param spriteGWorld Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *  determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     */
    public Compositor(QDGraphics paramQDGraphics, QDColor paramQDColor) throws QTException {
        this(paramQDGraphics, paramQDColor, null, 10, 1, defaultProtocol);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a Compositor with a background picutre as drawn in the supplied
     * QDGraphics background parameter. It has a default scale of 10 fps at a rate of 1.
     * @param spriteGWorld Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *  determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param background Specifies the background QDGraphics to be used to composit background sprites into.
     *  This will give better performance if the sprites in front of the background are changing.
     */
    public Compositor(QDGraphics paramQDGraphics1, QDColor paramQDColor, QDGraphics paramQDGraphics2) throws QTException {
        this(paramQDGraphics1, paramQDColor, paramQDGraphics2, 10, 1, defaultProtocol);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a Compositor with a background color.
     * @param spriteGWorld Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *  determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     */
    public Compositor(QDGraphics paramQDGraphics, QDColor paramQDColor, int paramInt1, int paramInt2) throws QTException {
        this(paramQDGraphics, paramQDColor, null, paramInt1, paramInt2, defaultProtocol);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a Compositor with a background picutre as drawn in the supplied
     * QDGraphics background parameter.
     * @param spriteGWorld Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *  determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param background Specifies the background QDGraphics to be used to composit background sprites into.
     *  This will give better performance if the sprites in front of the background are changing.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     */
    public Compositor(QDGraphics paramQDGraphics1, QDColor paramQDColor, QDGraphics paramQDGraphics2, int paramInt1, int paramInt2) throws QTException {
        this(paramQDGraphics1, paramQDColor, paramQDGraphics2, paramInt1, paramInt2, defaultProtocol);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Used by subclasses to construct a Compositor. The subclass must specify a background or a backgroundColor
     * but not both.
     * @param spriteGWorld this the buffer which the internal SpriteWorld uses to composit its image into.
     * @param background Specifies the background QDGraphics to be used to composit background sprites into.
     *  This will give better performance if the sprites in front of the background are changing.
     * @param backgroundColor this is background color that can be supplied to the SpriteWorld
     * @param scale the initial scale of the Space's timer
     * @param period the initial period of the Space's timer
     * @param defaultProtocol the minimal Protocol that members of the SWCompositor must meet
     */
    protected Compositor(QDGraphics paramQDGraphics1, QDColor paramQDColor, QDGraphics paramQDGraphics2, int paramInt1, int paramInt2, Protocol paramProtocol) throws QTException {
        super(paramQDGraphics1, paramQDColor, paramQDGraphics2, paramInt1, paramInt2, paramProtocol);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Dual hashtables are kept for both members and sprites as
     * this improves the runtime performance of Compositors (particularly with large numbers of sprites).
     * This is the container that is used to store the members of a Compositor.
     * The member as added is stored as the key of the hash table. The member's TwoDSprite
     * presenter is stored as the value corresponding to that key. If the member is
     * itself a TwoDSprite it is stored as both key and value.
     */
    protected Hashtable membersTable = new Hashtable();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Dual hashtables are kept for both members and sprites as
     * this improves the runtime performance of Compositors (particularly with large numbers of sprites).
     * This is the container that is used to store the TwoDSprites of a Compositor.
     * The member as added is stored as the value of the hash table. The member's TwoDSprite
     * presenter is stored as the key of that value. If the member is
     * itself a TwoDSprite it is stored as both key and value.
     */
    protected Hashtable spritesTable = new Hashtable();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return an enumeration to enumerate over all of the members that have been added to the
     * Compositor.
     * @return an Enumeration
     */
    public Enumeration members() {
        return this.membersTable.keys();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns an Enumeration that allows your application to iterate over the TwoDSprite
     * presenters that are responsible for presenting the image data within the Compositor.
     * @return an Enumeration;
     */
    public Enumeration sprites() {
        return this.spritesTable.keys();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if there are no members of the Compositor.
     * @return a boolean
     */
    public boolean isEmpty() {
        return this.membersTable.isEmpty();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the TwoDSprite presenter that is presenting the image data
     * of the given member. It returns null if the object is not a member.
     * @param member the member object
     * @return the member's TwoDSprite presenter
     */
    public TwoDSprite getMemberSprite(Object paramObject) {
        if (((paramObject instanceof TwoDSprite)) && (this.spritesTable.containsKey(paramObject))) {
            return (TwoDSprite) paramObject;
        }
        Object localObject = this.membersTable.get(paramObject);
        return localObject != null ? (TwoDSprite) localObject : null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the member that is being presented by the supplied TwoDSprite presenter.
     * It returns null if the TwoDSprite is not present in the Compositor.
     * @param sprite the TwoDSprite presenter
     * @return the member that is being presented by the supplied TwoDSprite presenter
     */
    public Object getSpriteMember(TwoDSprite paramTwoDSprite) {
        return this.spritesTable.get(paramTwoDSprite);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     * If the member implements the Layerable interface it will get the layer and assign the layer appropriately.
     * If the member is an instanceof the TwoDSprite class, then it will use the visibility state of that class.
     * Otherwise, the member will be added with default settings of visiblity set to true and a layer of 1.
     * @param member the object to add.
     * @return true if the object is appropriate for this collection
     * @return {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     * If the member is an instanceof the TwoDSprite class, then it will use the visibility state of that class.
     * The member will be added with default settings of visiblity set to true.
     * @param member the object to add.
     * @param layer the layer within the Compositor that the member will be presented at.
     * @return true if the object is appropriate for this collection
     * @return {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     * @param member the object to add.
     * @param layer the layer within the Compositor that the member will be presented at.
     * @param initiallyVisible if true the member is intially visible.
     * @return true if the object is appropriate for this collection
     * @return {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the number of members of the collection.
     * @return an int
     */
    public int size() {
        return this.membersTable.size();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the frontmost layer of all of the TwoDSprite presenters of the Compositor.
     * @return the frontmost layer
     * @return {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the backmost layer of all of the TwoDSprite presenters of the Compositor.
     * @return the backmost layer
     * @return {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove a sprite from the Compositor and remove it visually. If the applications
     * have defined control actions to this member these actions should be disabled
     * before the member is removed from the Compositor.
     * @param member the member to remove
     * @param {@link quicktime.QTException}
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
