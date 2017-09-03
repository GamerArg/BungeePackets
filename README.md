# onPacket
A small packet library for BungeeCord. This small library enables the "onPacket" event, on which another plugin can hook and listen for incoming packets! It also can be a good starting point for a more advanced packet library. 

Supports BungeeCord 1.12.1, and newer versions (hopefully).

## How does it work?
Basically, every time a player connects to a server, a new server connection is made between the BungeeCord and the server. The packets then get piped to the player through this. BungeePackets simply adds a little filter in the pipe. This way we can detect any and all packets coming in and out of the Bungee.

## Usage
BungeePackets adds in one simple event to manage packets. PacketEvent. The packet event is used to manage all incoming and outgoing packets. With every incoming and outcoming packet there is a player assigned to it somewhere in the pipe. Using this you can differentiate wheather a server is sending a packet to BungeeCord, or vice-versa.

In the example below, we are listening for an incoming packet coming from the client to the BungeeCord. We can check who is sending/receiving packets by checking if they are an instance of another connection. To check if the sender is a player, check if the sender is an instanceof a **ProxiedPlayer**. To check if the sender is Bungee, check if the sender is an instance of a **BungeeConnection**. To check if the sender is a server, check if the sender is an instanceof a **ServerConnection**.
```
@EventHandler(priority = EventPriority.HIGHEST)
    public void onPacket(PacketEvent event) {
        if (event.getSender() instanceof ProxiedPlayer &&
                event.getReceiver() instanceof BungeeConnection) {
            getLogger().info("Player Packet Received: " + event.getPacket().toString());
        }

        if (event.getReceiver() instanceof BungeeConnection &&
                event.getSender() instanceof ServerConnection) {
            getLogger().info("Server Packet Sent: " + event.getPacket().toString());
        }

        if (event.getSender() instanceof ServerConnection &&
                event.getReceiver() instanceof BungeeConnection) {
            getLogger().info("Server Packet Received: " + event.getPacket().toString());
        }

        if (event.getReceiver() instanceof BungeeConnection &&
                event.getSender() instanceof ProxiedPlayer) {
            getLogger().info("Player Packet Sent: " + event.getPacket().toString());
        }
    }
```
With the code above, we will be able to detect the 4 possible situations. You can also filter for an specific and already declared packet in BungeeCord, for example, the KeepAlive packet:

```
if (event.getSender() instanceof ProxiedPlayer &&
                event.getReceiver() instanceof BungeeConnection) {
    if (event.getPacket() instanceof KeepAlive) {
        KeepAlive packet = (KeepAlive) event.getPacket();
        getLogger().info("Keep Alive Id " + packet.getRandomId());
    }
}
```

## Customization
With this library you can register your own packets! To add your own packet, extend the org.spawl.bungeepackets.Packet class. This is an abstract class so you will get 3 methods to start with. Read, write, and handle. Read is called when the BungeeCord receives the packet, write is called when the Bungee is writing the packet to a pipeline, and handle is called after it is read. Below you can find an example of how you can register your own packet!
```
BungeePackets.registerPacket(Protocol.GAME.TO_SERVER / Protocol.GAME.TO_CLIENT, <ID>, <CLASS EXTENDING PACKET>.class);
```
