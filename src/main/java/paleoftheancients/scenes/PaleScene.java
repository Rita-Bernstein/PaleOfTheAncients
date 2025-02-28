package paleoftheancients.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect;
import paleoftheancients.PaleMod;

import java.util.ArrayList;

public class PaleScene extends AbstractScene {

    private ArrayList<DeathScreenFloatyEffect> particles;
    private Texture filledPixel;
    private Color color;
    private boolean addColor;

    private Color[] possibilities;

    public PaleScene() {
        super("endingScene/scene.atlas");
        particles = new ArrayList<>();
        this.ambianceName = "AMBIANCE_BEYOND";
        this.fadeInAmbiance();
        this.filledPixel = ImageMaster.loadImage(PaleMod.assetPath("images/misc/filledpixel.png"));
        this.color = Color.DARK_GRAY.cpy();
        this.possibilities = new Color[]{this.color, Color.CYAN, Color.OLIVE, Color.PURPLE, Color.GOLD};
        this.color.a = 0F;
        this.addColor = true;
    }

    @Override
    public void update() {
        if (this.particles.size() < 60) {
            DeathScreenFloatyEffect dsfe;
            this.particles.add(dsfe = new DeathScreenFloatyEffect());
            AbstractDungeon.effectList.add(dsfe);
        }

        for(int i = this.particles.size() - 1; i >= 0; i--) {
            DeathScreenFloatyEffect dfe = this.particles.get(i);
            dfe.update();
            if(dfe.isDone) {
                this.particles.remove(i);
            }
        }


        if(this.addColor) {
            this.color.a += Gdx.graphics.getDeltaTime() / 8F;
            if(this.color.a >= 0.3F) {
                this.addColor = false;
                this.color.a = 0.3F;
            }
        } else {
            this.color.a -= Gdx.graphics.getDeltaTime() / 8F;
            if(this.color.a <= 0F) {
                this.addColor = true;
                this.color = this.possibilities[MathUtils.random(this.possibilities.length - 1)].cpy();
                this.color.a = 0F;
            }
        }

    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(filledPixel, 0F, 0F, Settings.WIDTH / 2F, Settings.HEIGHT / 2F, Settings.WIDTH, Settings.HEIGHT, 1F, 1F, 0, 0, 0, 1, 1, false, false);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch spriteBatch) {

    }

    @Override
    public void renderCampfireRoom(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.WHITE);
        this.renderAtlasRegionIf(spriteBatch, this.campfireBg, true);
        for(final DeathScreenFloatyEffect dfe : this.particles) {
            dfe.render(spriteBatch);
        }
    }

    @Override
    public void randomizeScene() {

    }
}
