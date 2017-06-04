package ml.core.network;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ml.core.MLCore;
import ml.core.internal.CoreLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
/**
 * Thanks to MachineMuse for the idea on how to implement this
 * 
 * @author Matchlighter
 */
@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {

	protected static BiMap<Integer, Class<? extends MLPacket>> PacketTypes = HashBiMap.create();

	public void addHandler(Class<? extends MLPacket> pktClass) {
		PacketTypes.put(PacketTypes.size(), pktClass);
	}

	public void addHandlers(List<Class<? extends MLPacket>> pktClss) {
		for (Class<? extends MLPacket> pktCls : pktClss) {
			addHandler(pktCls);
		}
	}

	public final String channel;
	private EnumMap<Side, FMLEmbeddedChannel> channels;

	public PacketHandler(String channel) {
		this.channel = channel;
		this.channels = NetworkRegistry.INSTANCE.newChannel(channel, this);
	}

	protected void onError(Throwable e, MLPacket mlPkt) {
		CoreLogger.log(Level.ERROR, "Error handling packet in channel (" + this.channel + ")", e);
	}

	private MLPacket tryCastPacket(FMLProxyPacket pkt, EntityPlayer pl) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(pkt.payload().array());
		int pkId = dat.readInt();
		if (PacketTypes.get(pkId) != null) {
			try {
				Constructor<? extends MLPacket> contructor = PacketTypes.get(pkId).getConstructor(EntityPlayer.class, ByteArrayDataInput.class);
				MLPacket nPkt = contructor.newInstance(pl, dat);
//				nPkt.channel = pkt.channel();
				return nPkt;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public int findPacketId(Class<? extends MLPacket> pktClass) {
		return PacketTypes.inverse().get(pktClass);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
		
		EntityPlayer entPl = MLCore.proxy.getPlayerEntity(msg);

		MLPacket mlPkt = tryCastPacket(msg, entPl);
		if (mlPkt != null) {
			try {
				mlPkt.handle(entPl, FMLCommonHandler.instance().getEffectiveSide());
			} catch (Exception e) {
				onError(e, mlPkt);
				e.printStackTrace();
			}
		} else {
			CoreLogger.severe("(" + this.getClass().toString() + ") received unknown packet");
		}
	}
	
	public void sendToAll(Packet pkt) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	public void sendToAll(MLPacket pkt) {
		sendToAll(pkt.convertToFMLProxyPacket(this));
	}

	public void sendTo(Packet pkt, EntityPlayerMP player) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	public void sendTo(MLPacket pkt, EntityPlayerMP player) {
		sendTo(pkt.convertToFMLProxyPacket(this), player);
	}

	public void sendToAllAround(Packet pkt, NetworkRegistry.TargetPoint point) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	public void sendToAllAround(MLPacket pkt, NetworkRegistry.TargetPoint point) {
		sendToAllAround(pkt.convertToFMLProxyPacket(this), point);
	}

	public void sendToDimension(Packet pkt, int dimensionId) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		channels.get(Side.SERVER).writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	public void sendToDimension(MLPacket pkt, int dimensionId) {
		sendToDimension(pkt.convertToFMLProxyPacket(this), dimensionId);
	}

	public void sendToServer(Packet pkt) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	public void sendToServer(MLPacket pkt) {
		sendToServer(pkt.convertToFMLProxyPacket(this));
	}
}
