package taursus.remoteControl;

public interface IEventDataSerializer {
    public IEventData unserialize(IEventData data, byte[] bytes);

    public byte[] serialize(IEventData data);
}
