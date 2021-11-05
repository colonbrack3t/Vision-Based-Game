package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import sabrewulf.database.Database;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.events.SetLevelEvent;
import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.AnimatedButton;
import sabrewulf.ui.Button;
import sabrewulf.ui.ButtonAnimation;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.UI;

public class SelectLevelScene extends Scene {

    private Button nextPageButton, prevPageButton, menuButton;
    private String selectLevelTexture, yourIPTexture, enterTexture;

    private int page = 0;
    private int maxPage; // will be determined by size of database
    private int remainder;
    private Button[] buttons = new Button[7]; // 7 level names will be displayed per page

    public SelectLevelScene(UI ui) {
        super(ui);

        menuButton = new AnimatedButton(new Vector(25, 730), new Vector(195, 50),
                new ButtonAnimation("Menu", "Menu hover", "Menu press"));

        nextPageButton = new AnimatedButton(new Vector(1224, 25), new Vector(195, 50),
                new ButtonAnimation("Next Page", "Next Page hover", "Next Page press"));

        prevPageButton = new AnimatedButton(new Vector(21, 25), new Vector(195, 50),
                new ButtonAnimation("Prev Page", "Prev Page hover", "Prev Page press"));

        buttons[0] = new AnimatedButton(new Vector(320, 565), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[1] = new AnimatedButton(new Vector(320, 490), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[2] = new AnimatedButton(new Vector(320, 415), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[3] = new AnimatedButton(new Vector(320, 340), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[4] = new AnimatedButton(new Vector(320, 265), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[5] = new AnimatedButton(new Vector(320, 190), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        buttons[6] = new AnimatedButton(new Vector(320, 115), new Vector(800, 50),
                new ButtonAnimation("Level Option", "Level Option hover", "Level Option"));

        selectLevelTexture = "Select Level"; // will change when have new texture
        yourIPTexture = "Your IP";
        enterTexture = "Enter To Start";

        ui.names = Database.getNames();

        maxPage = ui.names.size() / 7; // finds number of pages required
        remainder = ui.names.size() % 7; // this is the number of level names on the final page
    }

    /**
     * prints a box with the level name in it for each level in the database
     * displays 7 levels per page
     */
    public void draw() {

        if (UI.isHost) {
            Sprite.draw(new Vector(0, 600), Texture.create(yourIPTexture));
            ui.printText(ui.ip, 88.0f, 166.5f, 4.0f, 0);
        }

        ui.drawSoundButtons();

        menuButton.draw();

        Sprite.draw(new Vector(545, 670), Texture.create(selectLevelTexture));
        Sprite.draw(new Vector(407, 25), Texture.create(enterTexture));

        if (page > 0) {
            prevPageButton.draw();
        }

        if (page < maxPage) {
            nextPageButton.draw();
            for (int i = 0; i < 7; i++) {
                buttons[i].draw();
                if (ui.names.get(i + page * 7).length() >= 25) {
                    ui.printText(ui.names.get(i + page * 7).substring(0, 24) + "...", 88.0f, 151.25f - i * 18.75f, 4.0f,
                            0);
                } else {
                    ui.printText(ui.names.get(i + page * 7), 88.0f, 151.25f - i * 18.75f, 4.0f, 0);
                }
            }
        } else {
            for (int i = 0; i < remainder; i++) { // ensures correct number of levels are printed on final page
                buttons[i].draw();
                ui.printText(ui.names.get(i + page * 7), 88.0f, 151.25f - i * 18.75f, 4.0f, 0);
            }
        }

        if (menuButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            ui.setTitleKeybindings();
            if (UI.isHost) {
                UI.isHost = false;
            }
            changeScene(SceneType.MENU);
        }

        if (nextPageButton.isClicked() && page < maxPage) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            page = page + 1;
        } else if (prevPageButton.isClicked() && page > 0) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            page = page - 1;
        }

        for (int i = 0; i < 7; i++) {
            if (buttons[i].isClicked()) {
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                ui.playClickSound();
                ui.setLobbyKeybindings();
                ui.eventBus().trigger(new OutgoingEvent(new SetLevelEvent(ui.names.get(i + page * 7)))); // ensures
                                                                                                         // correct
                                                                                                         // level is
                                                                                                         // loaded
                // GameServer.setLevel(Level.genLevel(ui.names.get(i + page*7)));
                changeScene(SceneType.IN_GAME);
            }
        }
    }
}
