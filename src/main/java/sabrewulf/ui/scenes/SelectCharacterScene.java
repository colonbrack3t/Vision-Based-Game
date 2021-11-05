package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import sabrewulf.events.GetAvailableCharactersEvent;
import sabrewulf.events.GetCharacterNamesEvent;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.events.SetClientColorEvent;
import sabrewulf.events.SetNameEvent;
import sabrewulf.game.Color;
import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.CharacterPreview;
import sabrewulf.ui.UI;
import sabrewulf.ui.AnimatedButton;
import sabrewulf.ui.Button;
import sabrewulf.ui.ButtonAnimation;
import sabrewulf.ui.SceneType;

public class SelectCharacterScene extends Scene {
    CharacterPreview blue, green, red, yellow;

    private String selectCharacterTexture, yourIPTexture, selected;
    public Color selectedColor;
    private long reqTime;
    private Button menuButton;

    public SelectCharacterScene(UI ui) {
        super(ui);
        reqTime = System.currentTimeMillis();
        selectCharacterTexture = "Select Character";
        yourIPTexture = "Your IP";
        selected = "Selected";

        Vector previewDimensions = new Vector(277, 565);

        menuButton = new AnimatedButton(new Vector(25, 730), new Vector(195, 50),
                new ButtonAnimation("Menu", "Menu hover", "Menu press"));

        blue = new CharacterPreview("character-red", new AnimatedButton(new Vector(82, 40), previewDimensions,
                new ButtonAnimation("Select Blue", "Select Blue hover", "Select Blue")));

        green = new CharacterPreview("character-green", new AnimatedButton(new Vector(407, 40), previewDimensions,
                new ButtonAnimation("Select Green", "Select Green hover", "Select Green")));

        red = new CharacterPreview("character-blue", new AnimatedButton(new Vector(732, 40), previewDimensions,
                new ButtonAnimation("Select Red", "Select Red hover", "Select Red")));

        yellow = new CharacterPreview("character-yellow", new AnimatedButton(new Vector(1057, 40), previewDimensions,
                new ButtonAnimation("Select Yellow", "Select Yellow hover", "Select Yellow")));
    }

    @Override
    public void draw() {
        ui.drawSoundButtons();

        menuButton.draw();

        Sprite.draw(new Vector(545, 650), Texture.create(selectCharacterTexture));

        if (UI.isHost) {
            Sprite.draw(new Vector(0, 600), Texture.create(yourIPTexture));
            ui.printText(ui.ip, 88.0f, 166.5f, 4.0f, 0);
        }

        if (menuButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.client.close();
            if (UI.isHost) {
                // close server
                UI.isHost = false;
            }
            changeScene(SceneType.MENU);
        }

        if (ui.availableColors == null) {
            if (reqTime < System.currentTimeMillis() - 1000 * 3) {
                reqTime = System.currentTimeMillis();
                ui.eventBus().trigger(new OutgoingEvent(new GetAvailableCharactersEvent()));
            }
            return;
        }

        if (ui.characterNames == null) {
            if (reqTime < System.currentTimeMillis() - 1000 * 3) {
                reqTime = System.currentTimeMillis();
                ui.eventBus().trigger(new OutgoingEvent(new GetCharacterNamesEvent()));
            }
            return;
        }

        if (ui.availableColors.contains(Color.BLUE)) {
            blue.draw(false);
        } else {
            blue.draw(true);
            Sprite.draw(new Vector(82, 40), Texture.create(selected));
            // print name of player who selected it
            String blueName = ui.characterNames.getOrDefault(Color.BLUE, "");
            if (blueName.length() > 10) {
                ui.printText(blueName.substring(0, 9) + "...", 20.5f, 9.0f, 4.0f, 1);
            } else {
                ui.printText(blueName, 20.5f, 9.0f, 4.0f, 1);
            }
        }

        if (ui.availableColors.contains(Color.GREEN)) {
            green.draw(false);
        } else {
            green.draw(true);
            Sprite.draw(new Vector(407, 40), Texture.create(selected));
            String greenName = ui.characterNames.getOrDefault(Color.GREEN, "");
            // print name of player who selected it
            if (greenName.length() > 10) {
                ui.printText(greenName.substring(0, 9) + "...", 101.75f, 9.0f, 4.0f, 2);
            } else {
                ui.printText(greenName, 101.75f, 9.0f, 4.0f, 2);
            }
        }

        if (ui.availableColors.contains(Color.RED)) {
            red.draw(false);
        } else {
            red.draw(true);
            Sprite.draw(new Vector(732, 40), Texture.create(selected));
            String redName = ui.characterNames.getOrDefault(Color.RED, "");
            // print name of player who selected it
            if (redName.length() > 10) {
                ui.printText(redName.substring(0, 9) + "...", 183.0f, 9.0f, 4.0f, 3);
            } else {
                ui.printText(redName, 183.0f, 9.0f, 4.0f, 3);
            }
        }

        if (ui.availableColors.contains(Color.YELLOW)) {
            yellow.draw(false);
        } else {
            yellow.draw(true);
            Sprite.draw(new Vector(1057, 40), Texture.create(selected));
            String yellowName = ui.characterNames.getOrDefault(Color.YELLOW, "");
            // print name of player who selected it
            if (yellowName.length() > 10) {
                ui.printText(yellowName.substring(0, 9) + "...", 264.25f, 9.0f, 4.0f, 4);
            } else {
                ui.printText(yellowName, 264.25f, 9.0f, 4.0f, 4);
            }
        }

        if (blue.isClicked()) {
            System.out.println("blue clicked");
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.characterNames.put(Color.BLUE, ui.name);
            selectedColor = Color.BLUE;
            setColor(selectedColor);
            updateNames();
            // ui.setGameKeybindings();
            // changeScene(SceneType.IN_GAME);

            if (UI.isHost) {
                changeScene(SceneType.SELECT_LEVEL);
            } else {
                ui.games++;
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }

        if (green.isClicked()) {
            System.out.println("green clicked");
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.characterNames.put(Color.GREEN, ui.name);
            selectedColor = Color.GREEN;
            setColor(selectedColor);
            updateNames();
            // ui.setGameKeybindings();
            // changeScene(SceneType.IN_GAME);

            if (UI.isHost) {
                changeScene(SceneType.SELECT_LEVEL);
            } else {
                ui.games++;
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }

        if (red.isClicked()) {
            System.out.println("red clicked");
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.characterNames.put(Color.RED, ui.name);
            selectedColor = Color.RED;
            setColor(selectedColor);
            updateNames();
            // ui.setGameKeybindings();
            // changeScene(SceneType.IN_GAME);

            if (UI.isHost) {
                changeScene(SceneType.SELECT_LEVEL);
            } else {
                ui.games++;
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }

        if (yellow.isClicked()) {
            System.out.println("yellow clicked");
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.characterNames.put(Color.YELLOW, ui.name);
            selectedColor = Color.YELLOW;
            setColor(selectedColor);
            updateNames();
            // ui.setGameKeybindings();
            // changeScene(SceneType.IN_GAME);

            if (UI.isHost) {
                changeScene(SceneType.SELECT_LEVEL);
            } else {
                ui.games++;
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }

    }

    private void setColor(Color color) {
        // set player in ui
        ui.eventBus().trigger(new SetClientColorEvent(color));
        // set ref in server
        ui.eventBus().trigger(new OutgoingEvent(new SetClientColorEvent(color)));
    }

    private void updateNames() {
        // doesn't need color as game server can infer from ip address
        ui.eventBus().trigger(new OutgoingEvent(new SetNameEvent(ui.name)));
    }

}
