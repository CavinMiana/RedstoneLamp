package raknet.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import raknet.MinecraftPacket;
import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Server;

public class JoinPacket extends Packet {
	private byte[] magic = new byte[16];
	private byte[] cookie = new byte[4];
	private short mtuSize;
	private short clientPort;
	private InetAddress clientAddress;
	private long serverID;
	private Server network;
	
	public JoinPacket(DatagramPacket p, long serverID, InetAddress clientAddress, short clientPort, Server network) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		if(!(b.get() == MinecraftPacket.ID_OPEN_CONNECTION_REQUEST_2))
			return;
		b.get(magic);
		b.get(cookie);
		b.get();
		this.mtuSize = b.getShort();
		this.clientAddress = p.getAddress();
		this.clientPort = (short) p.getPort();
		this.network = network;
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer response = ByteBuffer.allocate(30);
		response.put((byte) MinecraftPacket.ID_OPEN_CONNECTION_REPLY_2);
		response.put(magic);
		response.putLong(serverID);
		response.putShort(clientPort);
		response.putShort(mtuSize);
		response.put((byte) 0x00);
		
		return response;
	}

	@Override
	public void process(PacketHandler h) {
		//TODO: Add player
		String name = "Steve"; //Dummy player name
		this.network.getLogger().info("Player " + name + " has joined " + clientAddress);
		h.sendPacket(getPacket());
	}	
}
