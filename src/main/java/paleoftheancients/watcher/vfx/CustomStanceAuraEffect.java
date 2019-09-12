package paleoftheancients.watcher.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class CustomStanceAuraEffect extends StanceAuraEffect {
    public CustomStanceAuraEffect(AbstractStance.StanceName name, AbstractCreature ac) {
        super(name);
        float x = ac.hb.cX + MathUtils.random(-ac.hb.width / 16.0F, ac.hb.width / 16.0F);
        float y = ac.hb.cY + MathUtils.random(-ac.hb.height / 16.0F, ac.hb.height / 12.0F);
        TextureAtlas.AtlasRegion atlas = (TextureAtlas.AtlasRegion) ReflectionHacks.getPrivate(this, StanceAuraEffect.class, "img");
        x -= atlas.packedWidth / 2.0F;
        y -= atlas.packedHeight / 2.0F;
        ReflectionHacks.setPrivate(this, StanceAuraEffect.class, "x", x);
        ReflectionHacks.setPrivate(this, StanceAuraEffect.class, "y", y);
    }
}
