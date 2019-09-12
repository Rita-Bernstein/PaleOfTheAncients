package paleoftheancients.watcher.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;

public class CustomWrathParticleEffect extends WrathParticleEffect {
    public CustomWrathParticleEffect(AbstractCreature ac) {
        super();
        float x = ac.hb.cX + MathUtils.random(-ac.hb.width / 2.0F - 30.0F * Settings.scale, ac.hb.width / 2.0F + 30.0F * Settings.scale);
        float y = ac.hb.cY + MathUtils.random(-ac.hb.height / 2.0F - 10.0F * Settings.scale, ac.hb.height / 2.0F - 10.0F * Settings.scale);
        TextureAtlas.AtlasRegion atlas = (TextureAtlas.AtlasRegion) ReflectionHacks.getPrivate(this, StanceAuraEffect.class, "img");
        x -= atlas.packedWidth / 2.0F;
        y -= atlas.packedHeight / 2.0F;
        ReflectionHacks.setPrivate(this, WrathParticleEffect.class, "x", x);
        ReflectionHacks.setPrivate(this, WrathParticleEffect.class, "y", y);
    }
}
