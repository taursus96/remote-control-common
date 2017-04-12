package taursus.remoteControl;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Transporter implements ITransporter, Runnable {
    protected OutputStream output = null;
    protected InputStream input = null;

    protected boolean isListening = false;
    protected boolean isConnected = false;

    protected long keepAliveTo = 0;
    protected short keepAliveTime = 5000;

    protected long sendKeepAliveAt = 0;
    protected short sendKeepAliveTime = 1000;

    protected Thread thread = null;

    protected List<IOnConnectionFailed> onConnectionFailedListeners = Collections.synchronizedList(new ArrayList<IOnConnectionFailed>());
    protected List<IOnConnected> onConnectedListeners = Collections.synchronizedList(new ArrayList<IOnConnected>());
    protected List<IOnDisconnected> onDisconnectedListeners = Collections.synchronizedList(new ArrayList<IOnDisconnected>());
    protected List<IOnPackageReceived> onPackageReceivedListeners = Collections.synchronizedList(new ArrayList<IOnPackageReceived>());

    protected List<byte[]> packagesToSend = Collections.synchronizedList(new ArrayList<byte[]>());

    protected ILogger logger;

    public Transporter(ILogger logger) {
        this.logger = logger;
    }

    public void close() {
        disconnect();
        this.isListening = false;
    }

    public abstract void run();

    public Transporter registerOnConnectedListener(IOnConnected onConnected) {
        this.onConnectedListeners.remove(onConnected);
        this.onConnectedListeners.add(onConnected);
        return this;
    }

    public Transporter removeOnConnectedListener(IOnConnected onConnected) {
        this.onConnectedListeners.remove(onConnected);
        return this;
    }

    public Transporter registerOnDisconnectedListener(IOnDisconnected onDisconnected) {
        this.onDisconnectedListeners.remove(onDisconnected);
        this.onDisconnectedListeners.add(onDisconnected);
        return this;
    }

    public Transporter removeOnDisconnectedListener(IOnDisconnected onDisconnected) {
        this.onDisconnectedListeners.remove(onDisconnected);
        return this;
    }

    public Transporter registerOnPackageReceivedListener(IOnPackageReceived onPackageReceived) {
        this.onPackageReceivedListeners.remove(onPackageReceived);
        this.onPackageReceivedListeners.add(onPackageReceived);
        return this;
    }

    public Transporter removeOnPackageReceivedListener(IOnPackageReceived onPackageReceived) {
        this.onPackageReceivedListeners.remove(onPackageReceived);
        return this;
    }

    public Transporter registerOnConnectionFailedListener(IOnConnectionFailed onConnectionFailed) {
        this.onConnectionFailedListeners.remove(onConnectionFailed);
        this.onConnectionFailedListeners.add(onConnectionFailed);
        return this;
    }

    public Transporter removeOnConnectionFailedListener(IOnConnectionFailed onConnectionFailed) {
        this.onConnectionFailedListeners.remove(onConnectionFailed);
        return this;
    }

    public Transporter init() {
        this.thread = new Thread(this, "transporter");
        this.thread.start();
        return this;
    }

    public synchronized Transporter send(byte[] data) {
        this.packagesToSend.add(data);
        return this;
    }

    protected void onConnectionFailed() {
        this.logger.log("Transporter", "connection failed");

        for (IOnConnectionFailed x : this.onConnectionFailedListeners) {
            x.onConnectionFailed();
        }
    }

    protected void onConnected() {
        this.logger.log("Transporter", "connected");

        this.isListening = true;
        this.isConnected = true;
        this.keepAliveTo = System.currentTimeMillis() + this.keepAliveTime;

        for (IOnConnected x : this.onConnectedListeners) {
            x.onConnected();
        }

        listen();
    }

    protected void onDisconnected() {
        this.logger.log("Transporter", "disconnected");

        this.isListening = false;
        this.isConnected = false;

        for (IOnDisconnected x : this.onDisconnectedListeners) {
            x.onDisconnected();
        }
    }

    protected void onPackageSent(byte[] data) {

    }

    protected void onPackageReceived(byte[] data) {
        this.keepAliveTo = System.currentTimeMillis() + this.keepAliveTime;
        for (IOnPackageReceived x : this.onPackageReceivedListeners) {
            x.onPackageReceived(data);
        }
    }

    protected void disconnect() {
        if (this.isConnected) {
            try {
                this.output.close();
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.output = null;
            this.input = null;

            onDisconnected();
        }
    }

    protected void listen() {
        while (this.isListening) {
            if (!checkIfShouldKeepAlive()) {
                disconnect();
                break;
            }

            sendKeepAliveIfNeeded();
            processIncomingData();
            sendBufferedPackages();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendBufferedPackages() {
        synchronized (this.packagesToSend) {
            if (this.packagesToSend.size() > 0) {
                byte[] data = this.packagesToSend.get(0);
                this.packagesToSend.remove(0);
                sendPackage(data);
            }
        }
    }

    protected void sendPackage(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + Short.BYTES);
        buffer.putShort((short) data.length);
        buffer.put(data);

        try {
            this.output.write(buffer.array());
            this.output.flush();
            onPackageSent(data);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    protected void processIncomingData() {
        try {
            int availableBytes = this.input.available();
            if (availableBytes > Short.BYTES) {
                this.input.mark(100);
                short packageSize = getPackageSize();

                if (packageSize <= this.input.available()) {
                    byte[] packageData = getPackageData(packageSize);

                    if (packageData != null) {
                        onPackageReceived(packageData);
                    }
                } else { // Whole package isn't available yet
                    this.input.reset();
                }
            }
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    protected byte[] getPackageData(short packageSize) {
        byte[] bytes = new byte[packageSize];

        try {
            this.input.read(bytes, 0, packageSize);
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
            return null;
        }

        return bytes;
    }

    protected short getPackageSize() {
        byte[] bytes = new byte[Short.BYTES];

        try {
            this.input.read(bytes, 0, Short.BYTES);
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
            return 0;
        }

        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.put(bytes);
        buffer.rewind();

        return buffer.getShort();
    }

    protected boolean checkIfShouldKeepAlive() {
        return this.keepAliveTo > System.currentTimeMillis();
    }

    protected void sendKeepAliveIfNeeded() {
        long currentTime = System.currentTimeMillis();

        if (this.sendKeepAliveAt <= currentTime) {
            this.sendKeepAliveAt = currentTime + this.sendKeepAliveTime;

            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort(ITransporter.EVENT_KEEP_ALIVE);

            send(buffer.array());
        }
    }
}
