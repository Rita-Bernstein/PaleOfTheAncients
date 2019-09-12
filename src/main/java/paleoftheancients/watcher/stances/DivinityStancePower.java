package paleoftheancients.watcher.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import paleoftheancients.PaleMod;
import paleoftheancients.watcher.vfx.CustomDivinityParticleEffect;
import paleoftheancients.watcher.vfx.CustomStanceAuraEffect;

public class DivinityStancePower extends AbstractStancePower {
    public static final String POWER_ID = PaleMod.makeID("DivinityStancePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DivinityStancePower(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 3.0F : damage;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.2F;
                AbstractDungeon.effectsQueue.add(new CustomDivinityParticleEffect(this.owner));
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new CustomStanceAuraEffect(AbstractStance.StanceName.DIVINITY, this.owner));
        }
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster) this.owner, AbstractStance.StanceName.DIVINITY.name()));
        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(this.owner.hb.cX, this.owner.hb.cY, AbstractStance.StanceName.DIVINITY));
    }

    @Override
    public void onRemove() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster) this.owner, AbstractStance.StanceName.NONE.name()));
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
