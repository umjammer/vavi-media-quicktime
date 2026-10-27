package quicktime;

/**
 * Applications can implement this interface to register a handler object
 * of runtime exceptions. QTRuntimeExceptions occur in methods of which there
 * is no means for an application to try/catch an Exception - for eg if a Presentable
 * object through an exception when invoked from the QTJavaComponent.paint method.
 *
 * By registering a handler the application can deal with the exception
 * rather than having the exception thrown as a matter of course. If the
 * exception cannot be dealt with then the handler should throw the exception.
 * In the case of an unrecoverableFlag being set to true it is imperative that
 * the exception be thrown.
 *
 * If the handler is able to deal with the exception then the exceptionOccurred method
 * should return normally - in which case the exception is considered to have been
 * harmless, innocous or dealt with and the calling code will assume that basically
 * the exception never occured.
 */
public interface QTRuntimeHandler {

    /**
     * This method is called by the handleExceptionOrThrow method of QTRuntimeException,
     * supplying as much information about the exception as is known.
     * @param e the exception that has been generated
     * @param eGenerator the object that was responsible for the excpetion. This may not
     *  be the ultimate originator of the exception - for instance the MoviePlayer.task method
     *  may throw an exception because the Movie that it presents is corrupt. In this case the Movie
     *  class originated the exception but the MoviePlayer will be reported here as the originator.
     *  By looking at the errorCode, generator and method name an application can generally deduce
     *  the ultimate problem.
     * @param methodNameIfKnown this will be the name of the method (if known) that cause the exception.
     *  The same caveats for this name applies as for the eGenerator object.
     * @param unrecoverableFlag if true the exception MUST be thrown unless dealt with or a major
     *  problem will occur for the user. For example there is no memory left to complete an operation and
     *  the application cannot free up any memory resources it is holding.
     */
    void exceptionOccurred(QTRuntimeException paramQTRuntimeException, Object paramObject, String paramString, boolean paramBoolean);
}

