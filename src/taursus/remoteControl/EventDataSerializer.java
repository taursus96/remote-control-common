package taursus.remoteControl;

import java.nio.ByteBuffer;

public class EventDataSerializer implements IEventDataSerializer {
    protected static class EventDataSerializerLoader {
        private static final EventDataSerializer INSTANCE = new EventDataSerializer();
    }

    protected EventDataSerializer() {
        if (EventDataSerializerLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static EventDataSerializer getInstance() {
        return EventDataSerializerLoader.INSTANCE;
    }

    public IEventData unserialize(IEventData data, byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.rewind();

        data.setType(buffer.get());

        switch (data.getType()) {
        case IEventData.EVENT_MOVE_MOUSE:
            data.setMoveMouseX(buffer.getShort());
            data.setMoveMouseY(buffer.getShort());
            break;
        case IEventData.EVENT_KEY_PRESSED:
            data.setKeyCode(buffer.getInt());
            break;
        case IEventData.EVENT_KEY_RELEASED:
            data.setKeyCode(buffer.getInt());
            break;
        case IEventData.EVENT_SCROLL:
            data.setScroll(buffer.getShort());
            break;
        case IEventData.EVENT_OS:
            data.setOS(buffer.get());
            break;
        }

        return data;
    }

    public byte[] serialize(IEventData data) {
        ByteBuffer buffer;

        switch (data.getType()) {
        case IEventData.EVENT_MOVE_MOUSE: {
            short size = Byte.BYTES + Short.BYTES * 2;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
            buffer.putShort(data.getMoveMouseX());
            buffer.putShort(data.getMoveMouseY());
            break;
        }
        case IEventData.EVENT_KEY_PRESSED: {
            short size = Byte.BYTES + Integer.BYTES;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
            buffer.putInt(data.getKeyCode());
            break;
        }
        case IEventData.EVENT_KEY_RELEASED: {
            short size = Byte.BYTES + Integer.BYTES;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
            buffer.putInt(data.getKeyCode());
            break;
        }
        case IEventData.EVENT_SCROLL: {
            short size = Byte.BYTES + Short.BYTES;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
            buffer.putShort(data.getScroll());
            break;
        }
        case IEventData.EVENT_OS: {
            short size = Byte.BYTES * 2;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
            buffer.put(data.getOS());
            break;
        }
        default: {
            short size = Byte.BYTES;
            buffer = ByteBuffer.allocate(size);
            buffer.put(data.getType());
        }
        }

        return buffer.array();
    }
}
