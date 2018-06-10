package org.lwl.netty.codec.kryo.serialize;

import com.esotericsoftware.kryo.Kryo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.lwl.netty.codec.marshalling.MarshallingAdapterFactory;
import org.lwl.netty.codec.marshalling.MarshallingDecoderAdapter;
import org.lwl.netty.config.ProtocolConfig;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thinking_fioa
 * @createTime 2018/5/12
 * @description
 */


public class KryoDecoder {
    private static final MarshallingDecoderAdapter DECODER_ADAPTER = MarshallingAdapterFactory.buildDecoderAdapter();

    private static final KryoDecoder INSTANCE = new KryoDecoder();

    public static KryoDecoder getInstance() {
        return INSTANCE;
    }

    public List<Object> readList(Kryo kryo, ByteBuf inByteBuf) throws Exception {
        int size = inByteBuf.readInt();
        if(-1 == size) {
            return null;
        }
        if( 0 == size) {
            return new ArrayList<>();
        }

        List<Object> list = new ArrayList<Object>(size);
        for(int i =0;i<size; i++) {
            list.add(readObject(kryo, inByteBuf));
        }

        return list;
    }

    public Map<String, Object> readMap(Kryo kryo, ByteBuf inByteBuf) throws Exception {
        int size = inByteBuf.readInt();
        if(-1 == size) {
            return null;
        }
        if(0 == size) {
            return new HashMap<String, Object>();
        }

        Map<String, Object> valueMap = new HashMap<String, Object>(size);
        for(int i = 0; i<size; i++) {
            String key = readString(inByteBuf);
            Object value = readObject(ctx, inByteBuf);
            valueMap.put(key, value);
        }

        return valueMap;
    }

    public String readString(ByteBuf inByteBuf) throws UnsupportedEncodingException {
        int byteSize = inByteBuf.readInt();

        if(-1 == byteSize) {
            return null;
        }
        if(0 == byteSize) {
            return "";
        }

        byte[] bytes = new byte[byteSize];
        readBytes(inByteBuf, bytes);

        return new String(bytes, ProtocolConfig.getCharsetFormat());
    }

    public Object readObject(Kryo kryo, ByteBuf inByteBuf) throws Exception {
        return DECODER_ADAPTER.decode(ctx, inByteBuf);
    }

    public void readBytes(ByteBuf inByteBuf, byte[] dst) {
        inByteBuf.readBytes(dst);
    }

    public int readInt(ByteBuf inByteBuf) {
        return inByteBuf.readInt();
    }

    public long readLong(ByteBuf inByteBuf) {
        return inByteBuf.readLong();
    }

    public byte readByte(ByteBuf inByteBuf) {
        return inByteBuf.readByte();
    }

    public double readDouble(ByteBuf inByteBuf) {
        return inByteBuf.readDouble();
    }

    public short readShort(ByteBuf inByteBuf) {
        return inByteBuf.readShort();
    }
}
