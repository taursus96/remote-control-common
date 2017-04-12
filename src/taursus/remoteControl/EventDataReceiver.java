package taursus.remoteControl;

public abstract class EventDataReceiver implements IOnPackageReceived {
	protected ITransporter transporter;
	protected IEventDataSerializer eventDataSerializer;
	
	public EventDataReceiver(ITransporter transporter, IEventDataSerializer eventDataSerializer) {
		this.transporter = transporter;
		this.eventDataSerializer = eventDataSerializer;
		
		this.transporter.registerOnPackageReceivedListener(this);
	}

	public void onPackageReceived(byte[] data) {
		IEventData event = new EventData();
		eventDataSerializer.unserialize(event, data);
		processEvent(event);
	}
	
	public abstract void processEvent(IEventData event);
}
