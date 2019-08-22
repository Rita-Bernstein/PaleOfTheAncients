package paleoftheancients.patches;

import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.CustomDungeon;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong")
public class TempMusicPatch {
    //Lets you start custom music from e.g. an elite fight.
    @SpirePostfixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        if(CustomDungeon.tempmusic.containsKey(key)) {
            PaleMod.logger.info("Starting custom music: " + key);
            return SpireReturn.Return(MainMusic.newMusic(CustomDungeon.tempmusic.get(key)));
        }
        return SpireReturn.Continue();
    }

}

