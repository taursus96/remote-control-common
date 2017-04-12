package taursus.remoteControl;

public interface IEventData {
    public static final byte EVENT_KEEP_ALIVE = 0, EVENT_MOVE_MOUSE = 1, EVENT_PRESSED_LEFT_MOUSE_BUTTON = 2,
            EVENT_RELEASED_LEFT_MOUSE_BUTTON = 3, EVENT_PRESSED_RIGHT_MOUSE_BUTTON = 4,
            EVENT_RELEASED_RIGHT_MOUSE_BUTTON = 5, EVENT_PRESSED_MIDDLE_MOUSE_BUTTON = 6,
            EVENT_RELEASED_MIDDLE_MOUSE_BUTTON = 7, EVENT_KEY_PRESSED = 8, EVENT_KEY_RELEASED = 9, EVENT_SCROLL = 10,
            EVENT_OS = 11, EVENT_OPTION_SHUTDOWN = 12, EVENT_OPTION_HIBERNATE = 13, EVENT_OPTION_RESTART = 14,
            EVENT_OPTION_PRINT_SCREEN = 15, EVENT_OPTION_TASK_MANAGER = 16, EVENT_OPTION_CTRL_ALT_DELETE = 17,
            EVENT_OPTION_COMMAND_KEY = 18, EVENT_OPTION_TURN_OFF_MONITOR = 19, EVENT_OPTION_VOLUME_UP = 20,
            EVENT_OPTION_VOLUME_DOWN = 21, EVENT_OPTION_SOUND_MUTE = 22;

    public static final byte OS_WINDOWS = 1, OS_LINUX = 2, OS_MAC = 3;

    public byte getType();

    public IEventData setType(byte type);

    public short getMoveMouseX();

    public IEventData setMoveMouseX(short moveMouseX);

    public short getMoveMouseY();

    public IEventData setMoveMouseY(short moveMouseY);

    public int getKeyCode();

    public IEventData setKeyCode(int keyCode);

    public short getScroll();

    public IEventData setScroll(short scroll);

    public byte getOS();

    public IEventData setOS(byte OS);
}
