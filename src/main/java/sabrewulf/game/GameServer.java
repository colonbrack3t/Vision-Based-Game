package sabrewulf.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sabrewulf.App;
import sabrewulf.events.AvailableColorsChangeEvent;
import sabrewulf.events.CharacterNamesEvent;
import sabrewulf.events.ClientJoinEvent;
import sabrewulf.events.ClientLeaveEvent;
import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.EventListener;
import sabrewulf.events.GameUpdateEvent;
import sabrewulf.events.GetAvailableCharactersEvent;
import sabrewulf.events.GetCharacterNamesEvent;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.LevelOverEvent;
import sabrewulf.events.MoveEvent;
import sabrewulf.events.SetClientColorEvent;
import sabrewulf.events.SetLevelEvent;
import sabrewulf.events.SetNameEvent;
import sabrewulf.events.StartGameEvent;
import sabrewulf.events.UpdateCharacter;
import sabrewulf.events.StartTimeEvent;
import sabrewulf.events.UpdateLevelObjectsEvent;
import sabrewulf.game.Character;
import sabrewulf.game.Color;
import sabrewulf.game.Vector;
import sabrewulf.networking.server.Server;
import sabrewulf.ui.UI;

public class GameServer implements Runnable, EventListener {

    public static Server server;
    private ServerLevel level;
    private ServerLevel nextLevel;

    private Map<String, Character> characters; // for continuity between levels
    private Map<String, Color> clientIpsToColors;
    private Set<Color> availableColors;
    private Map<Color, String> characterNames;
    private EventBus eventBus;
    public static long startTime;

    public GameServer() {
        eventBus = new EventBus();
        server = Server.create(eventBus);
        this.level = ServerLevel.lobbyLevel();
        nextLevel = null;
        eventBus.subscribe(IncomingEvent.class, this);
        eventBus.subscribe(ClientJoinEvent.class, this);
        eventBus.subscribe(ClientLeaveEvent.class, this);

        clientIpsToColors = new HashMap<>();
        availableColors = new HashSet<>();
        availableColors.add(Color.BLUE);
        availableColors.add(Color.GREEN);
        availableColors.add(Color.YELLOW);
        availableColors.add(Color.RED);

        characters = new HashMap<>();
        characterNames = new HashMap<>();
    }

    public String initAndGetIp() {
        return initialize();
    }

    @Override
    public void run() {
        while (UI.isHost || App.serverMode) {
            try {
                update();
                Thread.sleep(1000 / 100);
            } catch (Exception e) {
            }
        }
        destroy();
        server.close();
    }

    private void destroy() {
        eventBus.unsubscribe(IncomingEvent.class, this);
        eventBus.unsubscribe(ClientJoinEvent.class, this);
        eventBus.unsubscribe(ClientLeaveEvent.class, this);
        level.destroy();
        if (nextLevel != null)
            nextLevel.destroy();
    }

    private void update() throws IOException {
        var updates = level.generateGameUpdates();
        server.sendToAll(new GameUpdateEvent(updates));
        level.update();
    }

    public void setLevel(ServerLevel level, boolean setStartTime) {
        this.level = level;
        if (setStartTime)
            level.setStartTime();
        for (Character character : characters.values()) {
            character.setPosition(level.getStartPosition());
            character.resetVision();
            character.resetVelocity();
            character.resetCheckpointHistory();
            level.add(character);
        }
    }

    private String initialize() {
        return server.start();
    }

    public Set<Color> getAvailableColors() {
        return availableColors;
    }

    private void addClient() {
        server.sendToAll(new AvailableColorsChangeEvent(availableColors));
    }

    private void setClientColor(String ip, Color color) {
        clientIpsToColors.put(ip, color);
        Character character = new Character(color);
        characters.put(ip, character);
        level.add(character);
        availableColors.remove(color);
        server.sendToAll(new AvailableColorsChangeEvent(availableColors));
        server.sendToAll(new UpdateLevelObjectsEvent(level.getGameObjects()));
    }

    private void removeClient(String ip) {
        Color clientColor = clientIpsToColors.get(ip);
        availableColors.add(clientColor);
        characterNames.remove(clientColor);
        Character clientCharacter = characters.get(ip);
        try {
            level.remove(clientCharacter);
        } catch (Exception e) {
            System.out.println("Game not started yet");
        }
        characters.remove(ip);
        clientIpsToColors.remove(ip);
        server.sendToAll(new CharacterNamesEvent(characterNames));
        server.sendToAll(new AvailableColorsChangeEvent(availableColors));
        server.sendToAll(new UpdateLevelObjectsEvent(level.getGameObjects()));
    }

    private void setCharacterName(String ip, String name) {
        Color col = clientIpsToColors.get(ip);
        characterNames.put(col, name);
        server.sendToAll(new CharacterNamesEvent(characterNames));
    }

    @Override
    public void notify(Event<?> event) {
        if (event instanceof IncomingEvent) {
            handleIncomingEvent((IncomingEvent) event);
        } else if (event instanceof ClientJoinEvent) {
            String clientIp = ((ClientJoinEvent) event).getClientIP();
            addClient();
        } else if (event instanceof ClientLeaveEvent) {
            String clientIp = ((ClientLeaveEvent) event).getIp();
            removeClient(clientIp);
        }
    }

    private void handleIncomingEvent(IncomingEvent incomingEvent) {
        Event unwrapped = incomingEvent.get();
        if (unwrapped instanceof SetClientColorEvent) {
            setClientColor(incomingEvent.getClientIP(), ((SetClientColorEvent) unwrapped).getColor());
        } else if (unwrapped instanceof GetAvailableCharactersEvent) {
            server.sendToAll(new AvailableColorsChangeEvent(availableColors));
        } else if (unwrapped instanceof SetLevelEvent) {
            nextLevel = ServerLevel.genLevel(((SetLevelEvent) unwrapped).getLevel());
            System.out.println("set next level to: " + ((SetLevelEvent) unwrapped).getLevel());
        } else if (unwrapped instanceof StartGameEvent) {
            setLevel(nextLevel, true);
            nextLevel = null;
            server.sendToAll(new UpdateLevelObjectsEvent(level.getGameObjects()));
            server.sendToAll(new StartTimeEvent());
        } else if (unwrapped instanceof SetNameEvent) {
            setCharacterName(incomingEvent.getClientIP(), ((SetNameEvent) unwrapped).getName());
        } else if (unwrapped instanceof GetCharacterNamesEvent) {
            server.sendToAll(new CharacterNamesEvent(characterNames));
        } else if (unwrapped instanceof UpdateCharacter) {
            Color col = clientIpsToColors.get(incomingEvent.getClientIP());
            Vector vel = ((UpdateCharacter) unwrapped).getVelocity();
            Vector pos = ((UpdateCharacter) unwrapped).getPosition();
            level.updateCharacter(col, vel, pos);
        } else if (unwrapped instanceof LevelOverEvent) {
            server.sendToAll(new LevelOverEvent());
            setLevel(ServerLevel.lobbyLevel(), false);
            server.sendToAll(new UpdateLevelObjectsEvent(level.getGameObjects()));
        }
    }

    public static Thread createThread() {
        GameServer server = new GameServer();
        server.initAndGetIp();
        return new Thread(server);
    }
}
