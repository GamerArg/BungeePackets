package org.spawl.bungeepackets.packet.client;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.spawl.bungeepackets.packet.Packet;
import org.spawl.bungeepackets.packet.StructurePacket;

public class PlayerDigging extends Packet {

    private int status;
    private long position;
    private byte face;

    public PlayerDigging() {}
    public PlayerDigging(int status, long position, byte face) {
        this.status = status;
        this.position = position;
        this.face = face;
    }

    @Override
    public void read(ByteBuf buffer, ProtocolConstants.Direction direction, int protocolVersion) {
        this.status = buffer.readInt();
        this.position = buffer.readLong();
        this.face = buffer.readByte();
    }

    @Override
    public void write(ByteBuf buffer, ProtocolConstants.Direction direction, int protocolVersion) {
        buffer.writeInt(this.status);
        buffer.writeLong(this.position);
        buffer.writeByte(this.face);
    }

    @Override
    public void handle(ProxiedPlayer player) throws Exception {
    }

    @Override
    public String toString() {
        return "PlayerDigging";
    }

}
