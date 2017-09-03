package org.spawl.bungeepackets.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;

import org.spawl.bungeepackets.connection.BungeeConnection;
import org.spawl.bungeepackets.event.PacketEvent;

public class CustomEncoder extends MessageToMessageEncoder<DefinedPacket> {

    private ProxiedPlayer player;
    private boolean server;

    public CustomEncoder(ProxiedPlayer player, boolean server) {
        this.player = player;
        this.server = server;
    }

    @Override
    protected void encode(ChannelHandlerContext chx, DefinedPacket msg, List<Object> out)
            throws Exception {
        PacketEvent event = null;

        if (server) {
            event = new PacketEvent(msg, player, new BungeeConnection(chx), player);
        } else {
            if (player instanceof UserConnection) {
                UserConnection user = (UserConnection) player;
                event = new PacketEvent(msg, player, new BungeeConnection(chx), user.getServer());
            }
        }

        if (event != null) {
            ProxyServer.getInstance().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                out.add(msg);
            }
        }
    }

}
