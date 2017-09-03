package org.spawl.bungeepackets.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.spawl.bungeepackets.connection.BungeeConnection;
import org.spawl.bungeepackets.event.PacketEvent;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.PacketWrapper;

public class CustomDecoder extends MessageToMessageDecoder<PacketWrapper> {

    private ProxiedPlayer player;
    private boolean server;

    public CustomDecoder(ProxiedPlayer player, boolean server) {
        this.player = player;
        this.server = server;
    }

    @Override
    protected void decode(ChannelHandlerContext chx, PacketWrapper wrapper,
                          List<Object> out) throws Exception {
        //Packet receive event. This can either be from the server or client!
        if (wrapper.packet == null) {
            out.add(wrapper);
            return;
        }

        PacketEvent event = null;
        if (server) {
            if (player instanceof UserConnection) {
                UserConnection user = (UserConnection) player;
                event = new PacketEvent(wrapper.packet, player, user.getServer(),
                        new BungeeConnection(chx));
            }
        } else {
            event = new PacketEvent(wrapper.packet, player, player, new BungeeConnection(chx));
        }

        if (event != null) {
            ProxyServer.getInstance().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                out.add(wrapper);
            }
        }
    }

}
