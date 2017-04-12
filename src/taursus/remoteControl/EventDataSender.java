package taursus.remoteControl;

public abstract class EventDataSender {
    protected ITransporter transporter;
    protected IEventDataSerializer serializer;

    public EventDataSender(ITransporter transporter, IEventDataSerializer serializer) {
        this.transporter = transporter;
        this.serializer = serializer;
    }

    protected EventDataSender sendEventData(IEventData data) {
        byte[] bytes = serializer.serialize(data);
        this.transporter.send(bytes);
        return this;
    }

    protected EventDataSender sendType(byte type) {
        IEventData data = new EventData();
        data.setType(type);
        sendEventData(data);
        return this;
    }
}