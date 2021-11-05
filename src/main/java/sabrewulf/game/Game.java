package sabrewulf.game;

import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.EventListener;
import sabrewulf.events.SetCameraRightBreakpointEvent;
import sabrewulf.events.UpdateCharacter;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.networking.client.Client;
import sabrewulf.render.GameScreen;
import sabrewulf.ui.UI;

public class Game implements EventListener {

    private GameScreen renderer;
    private EventBus eventBus;
    private ClientLevel levelInfo;

    private String ip;

    public boolean loop;

    public Game(GameScreen renderer, EventBus eventBus, String ip) {
        this.renderer = renderer;
        this.eventBus = eventBus;
        this.ip = ip;
        this.levelInfo = new ClientLevel(eventBus);

        this.eventBus.subscribe(SetCameraRightBreakpointEvent.class, this);
    }

    public void destroy() {
        this.eventBus.unsubscribe(SetCameraRightBreakpointEvent.class, this);
        this.levelInfo.destroy();
    }

    public void run(UI ui) {

        try {
            loop = true;

            // ui.setGameKeybindings();

            long taskTime = 0;
            long sleepTime = 1000 / 60;

            while (renderer.running() && loop) {
                taskTime = System.currentTimeMillis();

                if (levelInfo.update())
                    eventBus.trigger(new OutgoingEvent(
                            new UpdateCharacter(levelInfo.getPlayerVelocity(), levelInfo.getPlayerPosition())));
                renderer.render(levelInfo.getRenderList());

                taskTime = System.currentTimeMillis() - taskTime;
                if (sleepTime - taskTime > 0) {
                    Thread.sleep(sleepTime - taskTime);
                }
            }

        } catch (Exception e) {
            System.err.println("System interrupted during sleep - how rude!");
            e.printStackTrace();
        }
        ui.setMenuKeybindings();
    }

    public Client initializeClient() throws Exception {
        Client client = Client.createAndConnect(ip, eventBus);
        return client;
    }

    @Override
    public void notify(Event<?> event) {
        if (event instanceof SetCameraRightBreakpointEvent) {
            int right = ((SetCameraRightBreakpointEvent) event).get();
            renderer.setCameraRightBreakpoint(right);
        }
    }
}
