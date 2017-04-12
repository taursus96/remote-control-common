package taursus.remoteControl;

public interface IMouseControl {
	public IMouseControl moveCursor(short x, short y);
	public IMouseControl leftButtonPress();
	public IMouseControl leftButtonRelease();
	public IMouseControl rightButtonPress();
	public IMouseControl rightButtonRelease();
	public IMouseControl middleButtonPress();
	public IMouseControl middleButtonRelease();
	public IMouseControl scroll(short amount);
}
