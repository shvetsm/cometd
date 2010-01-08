package org.cometd.bayeux;

import java.util.Set;


/* ------------------------------------------------------------ */
/** A Bayeux Session
 * <p>A bayeux session represents a connection between a bayeux client and 
 * a bayeux server.  Previously this interface was called "Client", but this
 * resulted in confusion between the various representation of the server-side 
 * elements of a "Client".   Thus this entity has been renamed "Session", but
 * for backwards compatibility with the wire protocol, it is identified by 
 * a clientID string.
 */
public interface Session
{
    /* ------------------------------------------------------------ */
    /** The ClientId of the session.
     * <p>This would more correctly be called a "sessionId", but for
     * backwards compatibility with the bayeux protocol, it is a clientId
     * that identifies a session.
     * @return A string identifying the current session.
     */
    String getId();

    /* ------------------------------------------------------------ */
    /**
     * @param listener
     */
    void addListener(SessionListener listener);

    /* ------------------------------------------------------------ */
    /**
     * @param listener
     */
    void removeListener(SessionListener listener);

    /* ------------------------------------------------------------ */
    /**
     * @return True if the session is connected
     */
    boolean isConnected();

    /* ------------------------------------------------------------ */
    /** Disconnect the session
     * 
     */
    void disconnect();
    

    /* ------------------------------------------------------------ */
    /** Set a session attribute.
     * <p>Session attributes are convenience data that allows arbitrary
     * application data to be associated with a session.
     * @param name The attribute name
     * @param value The attribute value
     */
    void setAttribute(String name,Object value);
    
    /* ------------------------------------------------------------ */
    /** Get a named attribute
     * @param name The name of the attribute
     * @return The attribute value or null if not set.
     */
    Object getAttribute(String name);
    
    /* ------------------------------------------------------------ */
    /** Get Attribute names.
     * @return Set of known session attribute names
     */
    Set<String> getAttributeNames();
    
    /* ------------------------------------------------------------ */
    /**
     * Remove a session attribute
     * @param name The name of the attribute
     * @return the previous value of the attribute
     */
    Object removeAttribute(String name);
    
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /**
     */
    interface SessionListener extends Bayeux.BayeuxListener
    {};


    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /**
     */
    public interface MessageListener extends SessionListener
    {
        void onMessage(Session session, Message message);
    }
    
    
}