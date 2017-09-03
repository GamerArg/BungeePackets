package org.spawl.bungeepackets.event;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.protocol.DefinedPacket;

public class PacketEvent extends Event implements Cancellable {

    private boolean cancelled;
    private DefinedPacket packet;
    private Connection sender;
    private Connection receiver;
    private ProxiedPlayer player;

    public PacketEvent(DefinedPacket packet, ProxiedPlayer player, Connection sender,
                       Connection receiver) {
        this.cancelled = false;
        this.player = player;
        this.packet = packet;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public DefinedPacket getPacket() {
        return packet;
    }

    public Connection getSender() {
        return sender;
    }

    public Connection getReceiver() {
        return receiver;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
