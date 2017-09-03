package org.spawl.bungeepackets.connection;

import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.netty.ChannelWrapper;

import java.net.InetSocketAddress;

/**
 * Dummy class that represents the connection to the BungeeCord server
 */
public class BungeeConnection implements Connection {

	private final ChannelWrapper channel;

	public BungeeConnection(ChannelHandlerContext chx) {
		channel = new ChannelWrapper(chx);
	}

	@Override
	public void disconnect(String arg0) {
	}

	@Override
	public void disconnect(BaseComponent... arg0) {
	}

	@Override
	public void disconnect(BaseComponent arg0) {
	}

	@Override
	public boolean isConnected() {
		return !channel.isClosed();
	}

	@Override
	public InetSocketAddress getAddress() {
		return null;
	}

	@Override
	public Unsafe unsafe() {
		return null;
	}

}
