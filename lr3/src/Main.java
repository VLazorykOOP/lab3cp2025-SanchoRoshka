// ======= AudioEngine (Singleton) =======
class AudioEngine {
    private static AudioEngine instance;

    private AudioEngine() {
        System.out.println("AudioEngine initialized.");
    }

    public static AudioEngine getInstance() {
        if (instance == null) {
            instance = new AudioEngine();
        }
        return instance;
    }

    public void playSound(String sound) {
        System.out.println("[Playing sound]: " + sound);
    }
}

// ======= AudioEffect (Decorator) =======
interface AudioEffect {
    String process();
}

class BasicSound implements AudioEffect {
    public String process() {
        return "Basic sound";
    }
}

class EchoEffect implements AudioEffect {
    private AudioEffect source;

    public EchoEffect(AudioEffect source) {
        this.source = source;
    }

    public String process() {
        return source.process() + " + Echo";
    }
}

class BassBoostEffect implements AudioEffect {
    private AudioEffect source;

    public BassBoostEffect(AudioEffect source) {
        this.source = source;
    }

    public String process() {
        return source.process() + " + Bass Boost";
    }
}

// ======= Player State (State Pattern) =======
interface PlayerState {
    void pressPlay(Player player);
    void pressPause(Player player);
    void pressStop(Player player);
}

class PlayingState implements PlayerState {
    public void pressPlay(Player player) {
        System.out.println("Already playing.");
    }

    public void pressPause(Player player) {
        System.out.println("Paused.");
        player.setState(new PausedState());
    }

    public void pressStop(Player player) {
        System.out.println("Stopped.");
        player.setState(new StoppedState());
    }
}

class PausedState implements PlayerState {
    public void pressPlay(Player player) {
        System.out.println("Resuming playback.");
        player.setState(new PlayingState());
    }

    public void pressPause(Player player) {
        System.out.println("Already paused.");
    }

    public void pressStop(Player player) {
        System.out.println("Stopped from pause.");
        player.setState(new StoppedState());
    }
}

class StoppedState implements PlayerState {
    public void pressPlay(Player player) {
        System.out.println("Starting playback.");
        player.setState(new PlayingState());
    }

    public void pressPause(Player player) {
        System.out.println("Cannot pause. Player is stopped.");
    }

    public void pressStop(Player player) {
        System.out.println("Already stopped.");
    }
}

// ======= Player (Context for State) =======
class Player {
    private PlayerState state;
    private AudioEffect effect;

    public Player() {
        this.state = new StoppedState();
        this.effect = new BasicSound();
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void pressPlay() {
        state.pressPlay(this);
        if (state instanceof PlayingState) {
            String sound = effect.process();
            AudioEngine.getInstance().playSound(sound);
        }
    }

    public void pressPause() {
        state.pressPause(this);
    }

    public void pressStop() {
        state.pressStop(this);
    }

    public void addEffect(AudioEffect newEffect) {
        this.effect = newEffect;
        System.out.println("Effect added.");
    }
}

// ======= Main =======
public class Main {
    public static void main(String[] args) {
        Player player = new Player();

        // Перше натискання Play
        player.pressPlay();

        // Додаємо ефекти (Decorator)
        AudioEffect basic = new BasicSound();
        AudioEffect withEcho = new EchoEffect(basic);
        AudioEffect withEchoAndBass = new BassBoostEffect(withEcho);
        player.addEffect(withEchoAndBass);

        // Продовжуємо
        player.pressPause();
        player.pressPlay();

        // Зупиняємо
        player.pressStop();

        // Спроба поставити на паузу у зупиненому стані
        player.pressPause();
    }
}
