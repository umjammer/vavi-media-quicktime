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
 * The Compositor uses the SpriteWorld compositing services of the SWCompositor to composit
 * an image out of its member objects. The member object of a Compositor is required to at least
 * implement the ImageSpec interface. In which case the Compositor will create the TwoDSprite
 * that presents that image data in its display space. If a TwoDSprite itself is added to the
 * Compositor it is added directly as a member (as TwoDSprite implements the ImageSpec interface.
 *
 * @deprecated
 */
public class Compositor extends SWCompositor {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static final Protocol defaultProtocol = makeP();

    private static Protocol makeP() {
        try {
            return new Protocol(Class.forName("quicktime.app.image.ImageSpec"));
        } catch (ClassNotFoundException localClassNotFoundException) {
            throw new QTRuntimeException(localClassNotFoundException.getMessage());
        }
    }

    /**
     * This constructor creates a Compositor with a background color. It
     * has a default scale of 10 fps at a rate of 1.
     *
     * @param spriteGWorld    Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *                        determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @since QTJava 6.1
     * @deprecated
     */
    public Compositor(QDGraphics spriteGWorld, QDColor backgroundColor) throws QTException {
        this(spriteGWorld, backgroundColor, null, 10, 1, defaultProtocol);
    }

    /**
     * This constructor creates a Compositor with a background picture as drawn in the supplied
     * QDGraphics background parameter. It has a default scale of 10 fps at a rate of 1.
     *
     * @param spriteGWorld    Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *                        determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param background      Specifies the background QDGraphics to be used to composite background sprites into.
     *                        This will give better performance if the sprites in front of the background are changing.
     * @since QTJava 6.1
     * @deprecated
     */
    public Compositor(QDGraphics spriteGWorld, QDColor backgroundColor, QDGraphics background) throws QTException {
        this(spriteGWorld, backgroundColor, background, 10, 1, defaultProtocol);
    }

    /**
     * This constructor creates a Compositor with a background color.
     *
     * @param spriteGWorld    Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *                        determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param scale           the amount with which a second is divided into at a rate of one
     * @param period          the number of scale ticks that elapse between invocations of the action.
     * @since QTJava 6.1
     * @deprecated
     */
    public Compositor(QDGraphics spriteGWorld, QDColor backgroundColor, int scale, int period) throws QTException {
        this(spriteGWorld, backgroundColor, null, scale, period, defaultProtocol);
    }

    /**
     * This constructor creates a Compositor with a background picutre as drawn in the supplied
     * QDGraphics background parameter.
     *
     * @param spriteGWorld    Specifies the sprites offscreen QDGraphics. The size of this QDGraphics
     *                        determines the corresponding display size when the SpriteWorld is added to its display surface.
     * @param backgroundColor Specifies the background color.
     * @param background      Specifies the background QDGraphics to be used to composite background sprites into.
     *                        This will give better performance if the sprites in front of the background are changing.
     * @param scale           the amount with which a second is divided into at a rate of one
     * @param period          the number of scale ticks that elapse between invocations of the action.
     * @since QTJava 6.1
     * @deprecated
     */
    public Compositor(QDGraphics spriteGWorld, QDColor backgroundColor, QDGraphics background, int scale, int period) throws QTException {
        this(spriteGWorld, backgroundColor, background, scale, period, defaultProtocol);
    }

    /**
     * Used by subclasses to construct a Compositor. The subclass must specify a background or a backgroundColor
     * but not both.
     *
     * @param spriteGWorld    this the buffer which the internal SpriteWorld uses to composite its image into.
     * @param background      Specifies the background QDGraphics to be used to composite background sprites into.
     *                        This will give better performance if the sprites in front of the background are changing.
     * @param backgroundColor this is background color that can be supplied to the SpriteWorld
     * @param scale           the initial scale of the Space's timer
     * @param period          the initial period of the Space's timer
     * @param defaultProtocol the minimal Protocol that members of the SWCompositor must meet
     * @since QTJava 6.1
     * @deprecated
     */
    protected Compositor(QDGraphics spriteGWorld, QDColor background, QDGraphics backgroundColor, int scale, int period, Protocol defaultProtocol) throws QTException {
        super(spriteGWorld, background, backgroundColor, scale, period, defaultProtocol);
    }

    /**
     * Dual hashtables are kept for both members and sprites as
     * this improves the runtime performance of Compositors (particularly with large numbers of sprites).
     * This is the container that is used to store the members of a Compositor.
     * The member as added is stored as the key of the hash table. The member's TwoDSprite
     * presenter is stored as the value corresponding to that key. If the member is
     * itself a TwoDSprite it is stored as both key and value.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected final Hashtable<Object, Object> membersTable = new Hashtable<>();

    /**
     * Dual hashtables are kept for both members and sprites as
     * this improves the runtime performance of Compositors (particularly with large numbers of sprites).
     * This is the container that is used to store the TwoDSprites of a Compositor.
     * The member as added is stored as the value of the hash table. The member's TwoDSprite
     * presenter is stored as the key of that value. If the member is
     * itself a TwoDSprite it is stored as both key and value.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected final Hashtable<Object, Object> spritesTable = new Hashtable<>();

    /**
     * Return an enumeration to enumerate over all of the members that have been added to the
     * Compositor.
     *
     * @return an Enumeration
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Enumeration<?> members() {
        return this.membersTable.keys();
    }

    /**
     * This returns an Enumeration that allows your application to iterate over the TwoDSprite
     * presenters that are responsible for presenting the image data within the Compositor.
     *
     * @return an Enumeration;
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Enumeration sprites() {
        return this.spritesTable.keys();
    }

    /**
     * Returns true if there are no members of the Compositor.
     *
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isEmpty() {
        return this.membersTable.isEmpty();
    }

    /**
     * This method returns the TwoDSprite presenter that is presenting the image data
     * of the given member. It returns null if the object is not a member.
     *
     * @param member the member object
     * @return the member's TwoDSprite presenter
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public TwoDSprite getMemberSprite(Object member) {
        if (((member instanceof TwoDSprite)) && (this.spritesTable.containsKey(member))) {
            return (TwoDSprite) member;
        }
        Object localObject = this.membersTable.get(member);
        return localObject != null ? (TwoDSprite) localObject : null;
    }

    /**
     * This method returns the member that is being presented by the supplied TwoDSprite presenter.
     * It returns null if the TwoDSprite is not present in the Compositor.
     *
     * @param sprite the TwoDSprite presenter
     * @return the member that is being presented by the supplied TwoDSprite presenter
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Object getSpriteMember(TwoDSprite sprite) {
        return this.spritesTable.get(sprite);
    }

    /**
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     * If the member implements the Layerable interface it will get the layer and assign the layer appropriately.
     * If the member is an instanceof the TwoDSprite class, then it will use the visibility state of that class.
     * Otherwise, the member will be added with default settings of visiblity set to true and a layer of 1.
     *
     * @param member the object to add.
     * @return true if the object is appropriate for this collection
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean addMember(Object member) throws QTException {
        int i = 1;
        if ((member instanceof Layerable))
            i = ((Layerable) member).getLayer();
        boolean bool = true;
        if ((member instanceof TwoDSprite)) {
            bool = ((TwoDSprite) member).getVisible();
        } else if ((member instanceof TwoDSpriteInfo))
            bool = ((TwoDSpriteInfo) member).getVisible();
        return addMember(member, i, bool);
    }

    /**
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     * If the member is an instanceof the TwoDSprite class, then it will use the visibility state of that class.
     * The member will be added with default settings of visiblity set to true.
     *
     * @param member the object to add.
     * @param layer  the layer within the Compositor that the member will be presented at.
     * @return true if the object is appropriate for this collection
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean addMember(Object member, int layer) throws QTException {
        boolean bool = true;
        if ((member instanceof TwoDSprite)) {
            bool = ((TwoDSprite) member).getVisible();
        } else if ((member instanceof TwoDSpriteInfo))
            bool = ((TwoDSpriteInfo) member).getVisible();
        return addMember(member, layer, bool);
    }

    /**
     * Adds a member to the Compositor. The member must be some kind of ImageSpec object.
     *
     * @param member           the object to add.
     * @param layer            the layer within the Compositor that the member will be presented at.
     * @param initiallyVisible if true the member is intially visible.
     * @return true if the object is appropriate for this collection
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean addMember(Object member, int layer, boolean initiallyVisible) throws QTException {
        if (!isAppropriate(member))
            return false;
        if (hasMember(member)) {
            return true;
        }
        TwoDSprite localTwoDSprite = null;
        if ((member instanceof TwoDSprite)) {
            localTwoDSprite = (TwoDSprite) member;
            localTwoDSprite.setLayer(layer);
            localTwoDSprite.setVisible(initiallyVisible);
        } else {
            ImageSpec localImageSpec = (ImageSpec) member;
            GraphicsMode localGraphicsMode = null;
            if ((member instanceof Compositable))
                localGraphicsMode = ((Compositable) member).getGraphicsMode();
            Matrix localMatrix;
            if ((member instanceof Transformable)) {
                localMatrix = ((Transformable) member).getMatrix();
            } else
                localMatrix = new Matrix();
            localTwoDSprite = new TwoDSprite(localImageSpec, localMatrix, initiallyVisible, layer, localGraphicsMode);
        }
        this.membersTable.put(member, localTwoDSprite);
        this.spritesTable.put(localTwoDSprite, member);
        postProcessAdd(member, localTwoDSprite);
        return true;
    }

    /**
     * Returns the number of members of the collection.
     *
     * @return an int
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int size() {
        return this.membersTable.size();
    }

    /**
     * Returns the frontmost layer of all of the TwoDSprite presenters of the Compositor.
     *
     * @return the frontmost layer
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
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
     * Returns the backmost layer of all of the TwoDSprite presenters of the Compositor.
     *
     * @return the backmost layer
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
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
     * Remove a sprite from the Compositor and remove it visually. If the applications
     * have defined control actions to this member these actions should be disabled
     * before the member is removed from the Compositor.
     *
     * @param member the member to remove
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void removeMember(Object member) throws QTException {
        if (this.membersTable.containsKey(member)) {
            TwoDSprite localTwoDSprite = (TwoDSprite) this.membersTable.remove(member);
            this.spritesTable.remove(localTwoDSprite);
            postProcessRemove(member, localTwoDSprite);
        }
    }
}
