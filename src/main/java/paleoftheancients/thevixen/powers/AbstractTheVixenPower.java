package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AbstractTheVixenPower extends AbstractPower {
    private static final String BASE_DIR = PaleMod.assetPath("images/TheVixen/powers/");

    public AbstractTheVixenPower(String imgName) {
        this.region128 =
                new TextureAtlas.AtlasRegion(
                        ImageMaster.loadImage(BASE_DIR + "128/" + imgName), 0, 0, 128, 128);
        this.region48 =
                new TextureAtlas.AtlasRegion(
                        ImageMaster.loadImage(BASE_DIR + "48/" + imgName), 0, 0, 48, 48);
    }
}
