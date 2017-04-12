package taursus.remoteControl;

public interface IOptionsControl {
    public IOptionsControl shutdown();

    public IOptionsControl hibernate();

    public IOptionsControl restart();

    public IOptionsControl printScreen();

    public IOptionsControl taskManager();

    public IOptionsControl ctrlAltDelete();

    public IOptionsControl commandKey();

    public IOptionsControl turnOffMonitor();

    public IOptionsControl volumeUp();

    public IOptionsControl volumeDown();

    public IOptionsControl soundMute();
}
