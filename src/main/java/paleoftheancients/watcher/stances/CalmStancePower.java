package paleoftheancients.watcher.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.watcher.vfx.CustomCalmParticleEffect;
import paleoftheancients.watcher.vfx.CustomStanceAuraEffect;

public class CalmStancePower extends AbstractStancePower {
    public static final String POWER_ID = PaleMod.makeID("CalmStancePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public CalmStancePower(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.04F;
                AbstractDungeon.effectsQueue.add(new CustomCalmParticleEffect(this.owner));
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new CustomStanceAuraEffect(AbstractStance.StanceName.CALM, this.owner));
        }
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster) this.owner, AbstractStance.StanceName.CALM.name()));
        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
    }

    @Override
    public void onRemove() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }
}
