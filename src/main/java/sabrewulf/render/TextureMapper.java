package sabrewulf.render;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for holding a lookup table which maps the human
 * readable texture names to instances of {@link Texture}. The added benefit
 * of this class is that it loads all the textures it contains on application
 * start.
 *
 * The lookup table is implemented as a {@link HashMap}.
 */

public class TextureMapper {
    private static Map<String, Texture> textures;

    static {
        textures = new HashMap<>();

        textures.put("platform-grass", new Texture("./assets/Grass/slice03_03.png"));
        textures.put("platform-snow", new Texture("./assets/Snow/slice03_03.png"));
        textures.put("platform-castle", new Texture("./assets/Castle/slice03_03.png"));
        textures.put("platform-purple", new Texture("./assets/Purple/slice03_03.png"));
        textures.put("platform-black", new Texture("./assets/Black/slice03_03.png"));

        textures.put("platform-current", new Texture("./assets/Castle/slice03_03.png"));

        textures.put("character-red", new Texture("./assets/character_femaleAdventurer_idle.png"));
        textures.put("character-blue", new Texture("./assets/character_femalePerson_idle.png"));
        textures.put("character-green", new Texture("./assets/character_maleAdventurer_idle.png"));
        textures.put("character-yellow", new Texture("./assets/character_malePerson_idle.png"));

        textures.put("spike-red", new Texture("./assets/Spikes/spikes_red.png"));
        textures.put("spike-green", new Texture("./assets/Spikes/spikes_green.png"));
        textures.put("spike-blue", new Texture("./assets/Spikes/spikes_blue.png"));
        textures.put("spike-yellow", new Texture("./assets/Spikes/spikes_yellow.png"));
        textures.put("spike-neutral", new Texture("./assets/Spikes/spikes_neutral.png"));

        textures.put("saw-red", new Texture("./assets/Saws/saw_red.png"));
        textures.put("saw-green", new Texture("./assets/Saws/saw_green.png"));
        textures.put("saw-blue", new Texture("./assets/Saws/saw_blue.png"));
        textures.put("saw-yellow", new Texture("./assets/Saws/saw_yellow.png"));
        textures.put("saw-neutral", new Texture("./assets/Saws/saw_neutral.png"));

        textures.put("checkpoint", new Texture("./assets/Checkpoints/checkpoint.png"));
        textures.put("checkpoint-down", new Texture("./assets/Checkpoints/checkpoint_down.png"));
        textures.put("exit", new Texture("./assets/Exits/exit.png"));

        textures.put("Continue", new Texture("./assets/buttons/Main Menu.png"));
        textures.put("Back", new Texture("./assets/buttons/Back.png"));
        textures.put("NewGame", new Texture("./assets/buttons/NewGame.png"));
        textures.put("Settings", new Texture("./assets/buttons/Settings.png"));
        textures.put("Quit", new Texture("./assets/buttons/Exit.png"));
        textures.put("Music", new Texture("./assets/buttons/MusicOff.png"));
        textures.put("SFX", new Texture("./assets/buttons/SoundOff.png"));
        textures.put("Music2", new Texture("./assets/buttons/MusicOn.png"));
        textures.put("SFX2", new Texture("./assets/buttons/SoundOn.png"));
        textures.put("Up", new Texture("./assets/buttons/+.png"));
        textures.put("Down", new Texture("./assets/buttons/-.png"));
        textures.put("Maximum", new Texture("./assets/buttons/Full+.png"));
        textures.put("Minimum", new Texture("./assets/buttons/Full-.png"));
        textures.put("Controls", new Texture("./assets/buttons/Controls.png"));
        textures.put("Confirm", new Texture("./assets/buttons/are_you_sure.png"));
        // can't find this one
        textures.put("Yes", new Texture("./assets/buttons/Yes.png"));
        textures.put("No", new Texture("./assets/buttons/No.png"));
        textures.put("Menu", new Texture("./assets/buttons/Menu.png"));
        textures.put("No", new Texture("./assets/buttons/No.png"));
        textures.put("Menu", new Texture("./assets/buttons/Menu.png"));
        textures.put("Continue hover", new Texture("./assets/buttons/Hover/Main Menu.png"));
        textures.put("Back hover", new Texture("./assets/buttons/Hover/Back.png"));
        textures.put("NewGame hover", new Texture("./assets/buttons/Hover/NewGame.png"));
        textures.put("Settings hover", new Texture("./assets/buttons/Hover/Settings.png"));
        textures.put("Quit hover", new Texture("./assets/buttons/Hover/Exit.png"));
        textures.put("Music hover", new Texture("./assets/buttons/Hover/MusicOff.png"));
        textures.put("SFX hover", new Texture("./assets/buttons/Hover/SoundOff.png"));
        textures.put("Music2 hover", new Texture("./assets/buttons/Hover/MusicOn.png"));
        textures.put("SFX2 hover", new Texture("./assets/buttons/Hover/SoundOn.png"));
        textures.put("Up hover", new Texture("./assets/buttons/Hover/+.png"));
        textures.put("Down hover", new Texture("./assets/buttons/Hover/-.png"));
        textures.put("Controls hover", new Texture("./assets/buttons/Hover/Controls.png"));
        textures.put("Yes hover", new Texture("./assets/buttons/Hover/Yes.png"));
        textures.put("No hover", new Texture("./assets/buttons/Hover/No.png"));
        textures.put("Menu hover", new Texture("./assets/buttons/Hover/Menu.png"));
        textures.put("Continue press", new Texture("./assets/buttons/Pressed/Main Menu.png"));
        textures.put("Back press", new Texture("./assets/buttons/Pressed/Back.png"));
        textures.put("NewGame press", new Texture("./assets/buttons/Pressed/NewGame.png"));
        textures.put("Settings press", new Texture("./assets/buttons/Pressed/Settings.png"));
        textures.put("Quit press", new Texture("./assets/buttons/Pressed/Exit.png"));
        textures.put("Up press", new Texture("./assets/buttons/Pressed/+.png"));
        textures.put("Down press", new Texture("./assets/buttons/Pressed/-.png"));
        textures.put("Controls press", new Texture("./assets/buttons/Pressed/Controls.png"));
        textures.put("Yes press", new Texture("./assets/buttons/Pressed/Yes.png"));
        textures.put("No press", new Texture("./assets/buttons/Pressed/No.png"));
        textures.put("Menu press", new Texture("./assets/buttons/Pressed/Menu.png"));
        textures.put("Volume", new Texture("./assets/buttons/Volume.png"));
        textures.put("Controls2", new Texture("./assets/buttons/Controls2.png"));
        textures.put("Jump", new Texture("./assets/buttons/Jump.png"));
        textures.put("Left", new Texture("./assets/buttons/Forwards.png"));
        textures.put("Right", new Texture("./assets/buttons/Backwards.png"));
        textures.put("W", new Texture("./assets/buttons/W.png"));
        textures.put("A", new Texture("./assets/buttons/A.png"));
        textures.put("D", new Texture("./assets/buttons/D.png"));
        textures.put("Exit Game", new Texture("./assets/buttons/Exit Game.png"));
        textures.put("Esc", new Texture("./assets/buttons/ESC.png"));
        textures.put("1", new Texture("./assets/buttons/1.png"));
        textures.put("2", new Texture("./assets/buttons/2.png"));
        textures.put("3", new Texture("./assets/buttons/3.png"));
        textures.put("Access Code", new Texture("./assets/buttons/Access Code.png"));
        textures.put("Select Blue", new Texture("./assets/buttons/BlueSelectChar.png"));
        textures.put("Select Green", new Texture("./assets/buttons/GreenSelectChar.png"));
        textures.put("Select Red", new Texture("./assets/buttons/RedSelectChar.png"));
        textures.put("Select Yellow", new Texture("./assets/buttons/YellowSelectChar.png"));
        textures.put("Select Blue hover", new Texture("./assets/buttons/Hover/BlueSelectChar.png"));
        textures.put("Select Green hover", new Texture("./assets/buttons/Hover/GreenSelectChar.png"));
        textures.put("Select Red hover", new Texture("./assets/buttons/Hover/RedSelectChar.png"));
        textures.put("Select Yellow hover", new Texture("./assets/buttons/Hover/YellowSelectChar.png"));
        textures.put("Selected", new Texture("./assets/buttons/SelectedChar.png"));
        textures.put("Congrats", new Texture("./assets/buttons/Congrats.png"));
        textures.put("Create Game", new Texture("./assets/buttons/Create Game.png"));
        textures.put("Create Game hover", new Texture("./assets/buttons/Hover/Create Game.png"));
        textures.put("Create Game press", new Texture("./assets/buttons/Pressed/Create Game.png"));
        textures.put("Enter Access Code", new Texture("./assets/buttons/Enter Host IP.png"));
        textures.put("Enter", new Texture("./assets/buttons/Enter.png"));
        textures.put("Enter hover", new Texture("./assets/buttons/Hover/Enter.png"));
        textures.put("Enter press", new Texture("./assets/buttons/Pressed/Enter.png"));
        textures.put("Game Over", new Texture("./assets/buttons/Game Over.png"));
        textures.put("Go", new Texture("./assets/buttons/Go.png"));
        textures.put("Good Luck", new Texture("./assets/buttons/Good Luck.png"));
        textures.put("IN", new Texture("./assets/buttons/IN.png"));
        textures.put("Join Game", new Texture("./assets/buttons/Join Game.png"));
        textures.put("Join Game hover", new Texture("./assets/buttons/Hover/Join Game.png"));
        textures.put("Join Game press", new Texture("./assets/buttons/Pressed/Join Game.png"));
        textures.put("Leaderboard", new Texture("./assets/buttons/Leaderboard.png"));
        textures.put("Level Completed", new Texture("./assets/buttons/Level Completed.png"));
        textures.put("Next Level", new Texture("./assets/buttons/Next Level.png"));
        textures.put("Next Level hover", new Texture("./assets/buttons/Hover/Next Level.png"));
        textures.put("Next Level press", new Texture("./assets/buttons/Pressed/Next Level.png"));
        textures.put("Select Character", new Texture("./assets/buttons/Select Character.png"));
        textures.put("Start Game", new Texture("./assets/buttons/Start Game.png"));
        textures.put("Start Game hover", new Texture("./assets/buttons/Hover/Start Game.png"));
        textures.put("Start Game press", new Texture("./assets/buttons/Pressed/Start Game.png"));
        textures.put("Starts", new Texture("./assets/buttons/Starts.png"));
        textures.put("Text Input", new Texture("./assets/buttons/Text Input.png"));
        textures.put("Text Input hover", new Texture("./assets/buttons/Hover/Text Input.png"));
        textures.put("Text Input selected", new Texture("./assets/buttons/Selected Text Input.png"));
        textures.put("Time Taken", new Texture("./assets/buttons/Time Taken.png"));
        textures.put("Restart", new Texture("./assets/buttons/Restart.png"));
        textures.put("Restart hover", new Texture("./assets/buttons/Hover/Restart.png"));
        textures.put("Restart press", new Texture("./assets/buttons/Pressed/Restart.png"));
        textures.put("Waiting for players", new Texture("./assets/buttons/Waiting for players.png"));
        textures.put("Waiting", new Texture("./assets/buttons/Waiting.png"));
        textures.put("You Died", new Texture("./assets/buttons/You Died.png"));
        textures.put("YOUR TIME", new Texture("./assets/buttons/YOUR TIME.png"));
        textures.put("Invalid IP", new Texture("./assets/buttons/Invalid IP Address.png"));
        textures.put("Lobby", new Texture("./assets/buttons/lobby.png"));
        textures.put("Your Team", new Texture("./assets/buttons/Your Team.png"));
        textures.put("Your IP", new Texture("./assets/buttons/Your IP Address Is.png"));
        textures.put("Menu Background", new Texture("./assets/menuBackground.png"));
        textures.put("Logo", new Texture("./assets/buttons/LOGO.png"));
        textures.put("Press Enter", new Texture("./assets/buttons/Press Enter to Continue.png"));
        textures.put("Set Name", new Texture("./assets/buttons/Enter Name.png"));
        textures.put("M", new Texture("./assets/buttons/M.png"));
        textures.put("Mute Music", new Texture("./assets/buttons/Mute Music.png"));
        textures.put("Level Option", new Texture("./assets/buttons/Level Option.png"));
        textures.put("Level Option hover", new Texture("./assets/buttons/Hover/Level Option.png"));
        textures.put("Next Page", new Texture("./assets/buttons/Next Page.png"));
        textures.put("Next Page hover", new Texture("./assets/buttons/Hover/Next Page.png"));
        textures.put("Next Page press", new Texture("./assets/buttons/Pressed/Next Page.png"));
        textures.put("Prev Page", new Texture("./assets/buttons/Previous Page.png"));
        textures.put("Prev Page hover", new Texture("./assets/buttons/Hover/Previous Page.png"));
        textures.put("Prev Page press", new Texture("./assets/buttons/Pressed/Previous Page.png"));
        textures.put("Welcome", new Texture("./assets/buttons/Welcome.png"));
        textures.put("Select Level", new Texture("./assets/buttons/Select Level.png"));
        textures.put("Enter Key", new Texture("./assets/buttons/Enter Key.png"));
        textures.put("L", new Texture("./assets/buttons/L.png"));
        textures.put("Start from Lobby", new Texture("./assets/buttons/Start from Lobby.png"));
        textures.put("Enter Start", new Texture("./assets/buttons/Enter Start.png"));
        textures.put("Enter To Start", new Texture("./assets/buttons/Enter To Start.png"));
    }

    /**
     * Get a texture stored in the lookup table
     * @param textureName the human readable name of the texture
     * @return the {@link Texture} mapped to the textureName
     */
    public static Texture getTexture(String textureName) {
        return textures.get(textureName);
    }

    /**
     * Put a texture to the lookup table
     * @param textureName the human readable name of the texture
     * @param texture the {@link Texture} to map to textureName
     */
    public static void putTexture(String textureName, Texture texture) {
        textures.put(textureName, texture);
    }

}
