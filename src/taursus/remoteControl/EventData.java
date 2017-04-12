package taursus.remoteControl;

public class EventData implements IEventData {
    protected byte type;
    protected short moveMouseX;
    protected short moveMouseY;
    protected int keyCode;
    protected short scroll;
    protected byte OS;

    @Override
    public byte getType() {
        return this.type;
    }

    @Override
    public IEventData setType(byte type) {
        this.type = type;
        return this;
    }

    @Override
    public short getMoveMouseX() {
        return this.moveMouseX;
    }

    @Override
    public IEventData setMoveMouseX(short moveMouseX) {
        this.moveMouseX = moveMouseX;
        return this;
    }

    @Override
    public short getMoveMouseY() {
        return this.moveMouseY;
    }

    @Override
    public IEventData setMoveMouseY(short moveMouseY) {
        this.moveMouseY = moveMouseY;
        return this;
    }

    @Override
    public int getKeyCode() {
        return this.keyCode;
    }

    @Override
    public IEventData setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        return this;
    }

    @Override
    public short getScroll() {
        return this.scroll;
    }

    @Override
    public IEventData setScroll(short scroll) {
        this.scroll = scroll;
        return this;
    }

    @Override
    public byte getOS() {
        return this.OS;
    }

    @Override
    public IEventData setOS(byte OS) {
        this.OS = OS;
        return this;
    }

}
