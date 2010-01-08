package org.acme;

import java.io.IOException;

import org.cometd.bayeux.Bayeux;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.bayeux.client.BayeuxClient;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.SessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;

public class BayeuxUsage
{
    BayeuxServer _bayeux;
    BayeuxClient _client;

    public void clientUsage() throws IOException
    {
        
        
        
        // configure the transport options
        _client.getTransportOptions("websocket").put("port",81);
        _client.getTransportOptions("jsonp").put("callback","jsonp_deliver");
        _client.getTransportOptions("*").put("backoffMs",1000);
        _client.setAllowedTransports("websocket","xdlongpoll","jsonp","longpoll");
        

        // Add listeners for meta messages for all sessions
        _client.getChannel("/meta/*").addListener(new Channel.MetaListener()
        {
            public void onMetaMessage(Bayeux bayeux, Channel channel, Message message, boolean successful, String error)
            {
            }
        });
        
        
        // Initialize a session
        ClientSession session = _client.newSession("www1.acme.com/cometd","www2.acme.com/cometd");
        session.handshake(true);
        
        
        // Initialize another session
        ClientSession session2 = _client.newSession("ws.google.com/cometd");
        session2.addExtension(new GoogleWsAuthenticationExtension());
        session2.handshake(true);
        
       

        // Get a Channel scoped by the Session
        SessionChannel channel = session.getChannel("/foo/bar");

        // subscribe to all messages on a particular channel
        // THIS DOES SEND A SUBSCRIPTION!
        channel.subscribe(new Channel.MessageListener()
        {
            public void onMessage(Bayeux bayeux, Channel channel, Message message)
            {
            }
        });


        // publish a message
        channel.publish("hello world");


    }


    public void serverUsage()
    {

        // Add a listener to notice new session
        _bayeux.addListener(new BayeuxServer.SessionListener()
        {
            public void sessionAdded(ServerSession channel)
            {

            }
            public void sessionRemoved(ServerSession channel, boolean timedout)
            {
            }
        });

        // Add a listener to notice new channels
        _bayeux.addListener(new BayeuxServer.ChannelListener()
        {
            public void channelRemoved(ServerChannel channel)
            {
            }

            public void channelAdded(ServerChannel channel)
            {
            }
        });

        // Listen to all subscriptions on the server
        _bayeux.addListener(new BayeuxServer.SubscriptionListener()
        {
            public void unsubscribed(ServerSession session, ServerChannel channel)
            {
            }

            public void subscribed(ServerSession session, ServerChannel channel)
            {
            }
        });

        // Listen to all subscriptions on a particular channel
        _bayeux.getChannel("/foo/bar").addListener(new ServerChannel.SubscriptionListener()
        {
            public void unsubscribed(ServerSession client, Channel channel)
            {
            }

            public void subscribed(ServerSession client, Channel channel)
            {
            }
        });

        // Listen to all messages on a particular channel
        _bayeux.getChannel("/foo/bar").addListener(new Channel.MessageListener()
        {
            public void onMessage(Bayeux bayeux, Channel channel, Message message)
            {
            }
        });

        // Listen and potentially CHANGE messages on a particular channel
        _bayeux.getChannel("/foo/bar").addListener(new ServerChannel.PublishListener()
        {
            public boolean onMessage(ServerMessage.Mutable message)
            {
                return true;
            }
        });



        // batch the delivery of special message to an arbitrary client:
        final ServerSession session = _bayeux.getServerSession("123456789");
        final ServerMessage.Mutable msg=_bayeux.newServerMessage();
        msg.setChannelId("/foo/bar");
        msg.setData("something special");
        session.batch(new Runnable()
        {
            public void run()
            {
                session.deliver(session,msg);
                if (session.isLocalSession())
                    session.getLocalSession().getChannel("/foo/bar").publish("Hello");
            }
        });




        // Create a new Local Session
        final LocalSession local = _bayeux.newLocalSession("testui");
        final SessionChannel channel=local.getChannel("/foo/bar");

        // Subscribe to a channel for a local session
        channel.subscribe(new Channel.MessageListener()
        {
            public void onMessage(Bayeux bayeux, Channel channel, Message message)
            {
            }
        });

        // batch the publishing of messages from a local client.
        local.batch(new Runnable()
        {
            public void run()
            {
                channel.publish("hello");
                channel.publish("world");
            }
        });
    }

    
    
    static class GoogleWsAuthenticationExtension implements ClientSession.Extension
    {
        public boolean rcv(ClientSession session, Mutable message)
        {
            return false;
        }

        public boolean rcvMeta(ClientSession session, Mutable message)
        {
            return false;
        }

        public boolean send(ClientSession session, Mutable message)
        {
            return false;
        }

        public boolean sendMeta(ClientSession session, Mutable message)
        {
            return false;
        }
    }
}