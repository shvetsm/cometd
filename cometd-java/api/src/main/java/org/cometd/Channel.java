// ========================================================================
// Copyright 2007-2008 Dojo Foundation
// ------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at 
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//========================================================================

package org.cometd;

import java.util.Collection;

/* ------------------------------------------------------------ */
/** A Bayeux Channel.
 * <p>
 * A Channel represents a routing path for messages to Clients.
 * Clients may subscribe to a channel and will be delivered all messages
 * published to the channel.
 * 
 */
public interface Channel
{
    /* ------------------------------------------------------------ */
    /**
     * @return true if the Channel has been removed, false if it was not possible to remove the channel
     */
    public abstract boolean remove();

    /* ------------------------------------------------------------ */
    public abstract String getId();

    /* ------------------------------------------------------------ */
    /** Publish a message.
     * @param fromClient From client or null
     * @param data The message data
     * @param msgId The message ID or null
     */
    public void publish(Client fromClient, Object data, String msgId);
    
    /* ------------------------------------------------------------ */
    /** Is the channel persistent.
     * Non persistent channels are removed when the last subscription is
     * removed 
     * @return true if the Channel will persist without any subscription.
     */
    public boolean isPersistent();
    
    /* ------------------------------------------------------------ */
    /**
     * @param persistent true if the Channel will persist without any subscription.
     */
    public void setPersistent(boolean persistent);
    
    /* ------------------------------------------------------------ */
    /** Subscribe to a channel.
     * Equivalent to bayeux.subscribe(channel.getId(),subscriber,false);
     * @param toChannel
     * @param subscriber
     */
    public void subscribe(Client subscriber);

    /* ------------------------------------------------------------ */
    /** Unsubscribe to a channel
     * @param toChannel
     * @param subscriber
     */
    public void unsubscribe(Client subscriber);

    /* ------------------------------------------------------------ */
    public Collection<Client> getSubscribers();

    /* ------------------------------------------------------------ */
    public int getSubscriberCount();
 
    /* ------------------------------------------------------------ */
    public void addDataFilter(DataFilter filter);

    /* ------------------------------------------------------------ */
    public DataFilter removeDataFilter(DataFilter filter);

    /* ------------------------------------------------------------ */
    public Collection<DataFilter> getDataFilters();

    /* ------------------------------------------------------------ */
    public void addListener(ChannelListener listener);
}
