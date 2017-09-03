package org.spawl.bungeepackets;

import io.netty.channel.Channel;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.Protocol;

import org.spawl.bungeepackets.encoder.CustomDecoder;
import org.spawl.bungeepackets.encoder.CustomEncoder;
import org.spawl.bungeepackets.packet.client.InCloseWindow;
import org.spawl.bungeepackets.packet.client.PlayerDigging;
import org.spawl.bungeepackets.packet.server.OutCloseWindow;
import org.spawl.bungeepackets.packet.server.OutOpenWindow;
import org.spawl.bungeepackets.util.Reflection;
import org.spawl.bungeepackets.util.Util;

import java.lang.reflect.Method;

public class BungeePackets extends Plugin implements Listener {

    public void onEnable() {
        // Info about packets: http://wiki.vg/Protocol
        registerPacket(Protocol.GAME.TO_SERVER, 0x08, InCloseWindow.class);
        registerPacket(Protocol.GAME.TO_SERVER, 0x13, OutOpenWindow.class);
        registerPacket(Protocol.GAME.TO_SERVER, 0x14, PlayerDigging.class);

        registerPacket(Protocol.GAME.TO_CLIENT, 0x12, OutCloseWindow.class);
        registerPacket(Protocol.GAME.TO_CLIENT, 0x13, OutOpenWindow.class);

        this.getProxy().getPluginManager().registerListener(this, this);
    }

    public static UserConnection getUserConnection(AbstractPacketHandler handler) {
        return Util.getConnection(handler);
    }

    public static boolean registerPacket(Protocol.DirectionData data, int id,
                                         Class<? extends org.spawl.bungeepackets.packet.Packet> cl) {
        try {
            Util.registerPacket(data, id, cl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerConnection server = (ServerConnection) event.getServer();

        if (player != null) {
            if (server != null) {
                ChannelWrapper wrapper = server.getCh();
                if (wrapper != null) {
                    try {
                        wrapper.getHandle().pipeline().addAfter(PipelineUtils.PACKET_DECODER,
                                "bp-decoder", new CustomDecoder(player, true));
                        wrapper.getHandle().pipeline().addAfter(PipelineUtils.PACKET_ENCODER,
                                "bp-encoder", new CustomEncoder(player, true));
                    } catch (Exception e) {
                        System.out.println("[BungeePackets] Failed to inject server connection for "
                                + event.getPlayer().getName());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPostLogin(PostLoginEvent event) {
        try {
            ProxiedPlayer player = event.getPlayer();
            Object ch = Reflection.get(player, "ch");
            Method method = ch.getClass().getDeclaredMethod("getHandle", new Class[0]);
            Channel channel = (Channel) method.invoke(ch, new Object[0]);
            channel.pipeline().addAfter(PipelineUtils.PACKET_DECODER, "bp-decoder",
                    new CustomDecoder(player, false));
            channel.pipeline().addAfter(PipelineUtils.PACKET_ENCODER, "bp-encoder",
                    new CustomEncoder(player, false));
        } catch (Exception e) {
            System.out.println("[BungeePackets] Failed to inject client connection for " + event
                    .getPlayer().getName());
        }
    }
}
