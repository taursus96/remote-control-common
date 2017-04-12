package taursus.remoteControl;

public interface ITransporter {
    public static final byte EVENT_KEEP_ALIVE = -1;

    public ITransporter init();

    public void close();

    public abstract void run();

    public ITransporter registerOnConnectedListener(IOnConnected onConnected);

    public ITransporter removeOnConnectedListener(IOnConnected onConnected);

    public ITransporter registerOnDisconnectedListener(IOnDisconnected onDisconnected);

    public ITransporter removeOnDisconnectedListener(IOnDisconnected onDisconnected);

    public ITransporter registerOnPackageReceivedListener(IOnPackageReceived onPackageReceived);

    public ITransporter removeOnPackageReceivedListener(IOnPackageReceived onPackageReceived);

    public ITransporter registerOnConnectionFailedListener(IOnConnectionFailed onConnectionFailed);

    public ITransporter removeOnConnectionFailedListener(IOnConnectionFailed onConnectionFailed);

    public ITransporter send(byte[] data);
}
