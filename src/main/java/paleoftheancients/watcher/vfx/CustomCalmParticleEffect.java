package paleoftheancients.watcher.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;

public class CustomCalmParticleEffect extends CalmParticleEffect {
    public CustomCalmParticleEffect(AbstractCreature ac) {
        super();
        float x = ac.hb.cX + MathUtils.random(100.0F, 160.0F) * Settings.scale - 32.0F;
        float y = ac.hb.cY + MathUtils.random(-50.0F, 220.0F) * Settings.scale - 32.0F;
        ReflectionHacks.setPrivate(this, WrathParticleEffect.class, "x", x);
        ReflectionHacks.setPrivate(this, WrathParticleEffect.class, "y", y);

        ReflectionHacks.setPrivate(this, CalmParticleEffect.class, "vX", -(float)ReflectionHacks.getPrivate(this, CalmParticleEffect.class, "vX"));
        ReflectionHacks.setPrivate(this, CalmParticleEffect.class, "dvx", -(float)ReflectionHacks.getPrivate(this, CalmParticleEffect.class, "dvx"));
    }
}
