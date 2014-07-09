package ml.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;

import ml.core.internal.CoreLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Thanks to MachineMuse for the idea on how to implement this
 * @author Matchlighter
 */
public class PacketHandler extends MessageToMessageCodec<FMLProxyPacket, MLPacket> {
		
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	protected static BiMap<Integer, Class<? extends MLPacket>> PacketTypes = HashBiMap.create();
	
	public PacketHandler(String channel) {
		this.channels = NetworkRegistry.INSTANCE.newChannel(channel, this);
	}
	
	public static void addHandler(Class<? extends MLPacket> pktClass){
		PacketTypes.put(PacketTypes.size(), pktClass);
	}
	
	public static void addHandlers(List<Class<? extends MLPacket>> pktClss) {
		for (Class<? extends MLPacket> pktCls : pktClss) {
			addHandler(pktCls);
		}
	}
	
	protected void onError(Throwable e, MLPacket mlPkt) {
		CoreLogger.log(Level.SEVERE, "Error handling packet in channel ("+mlPkt.channel+")", e);
	}

	public static int findPacketId(Class<? extends MLPacket> pktClass){
		return PacketTypes.inverse().get(pktClass);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, MLPacket msg, List<Object> out) throws Exception {
		out.add(msg.convertToFMLPacket());
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
		ByteBuf buf = msg.payload();
		int pkId = buf.readInt();
		if (PacketTypes.get(pkId) != null){
			try {
				Constructor<? extends MLPacket> contructor = PacketTypes.get(pkId).getConstructor(ByteBuf.class);
				MLPacket nPkt = contructor.newInstance(buf);
				nPkt.channel = msg.channel();
				
				Side side = msg.getTarget();
				if (side == Side.CLIENT) {
					nPkt.handleClientSide(getClientPlayer());
				} else {
					INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
					nPkt.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public void sendToAll(MLPacket message) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendTo(MLPacket message, EntityPlayerMP player) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToAllAround(MLPacket message, NetworkRegistry.TargetPoint point) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToDimension(MLPacket message, int dimensionId) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}
	
	public void sendToServer(MLPacket message) {
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeAndFlush(message);
	}
}
