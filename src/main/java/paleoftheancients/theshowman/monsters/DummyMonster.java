package paleoftheancients.theshowman.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class DummyMonster extends AbstractMonster {
    public BobEffect effect;

    public DummyMonster(float hb_x, float hb_y, float hb_w, float hb_h, Texture t) {
        super("", "", 1, hb_x, hb_y, hb_w, hb_h, null);
        this.img = t;
        this.effect = new BobEffect();
    }

    @Override
    public void takeTurn() {

    }

    @Override
    protected void getMove(int i) {

    }

    @SpireOverride
    protected void renderIntent(SpriteBatch sb) {
        SpireSuper.call(sb);
    }
    @Override
    public void renderHealth(SpriteBatch sb) {

    }

    public void refresh() {
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
    }

    @Override
    public void update() {
        this.effect.update();
        this.updateIntent();
    }

    @SpireOverride
    protected void updateIntent() {
        SpireSuper.call();
    }
}
