package paleoftheancients.slimeboss.monsters;

import paleoftheancients.PaleMod;
import paleoftheancients.slimeboss.powers.SlimeSplitPower;
import paleoftheancients.slimeboss.powers.SlimeUnityPower;
import paleoftheancients.thevixen.cards.status.BossBurn;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;

public class SlimeBossest extends SlimeBoss implements WeirdSlimeThing {
    public static String ID = PaleMod.makeID("SlimeBossest");
    public static int INVINCIBLE_DIVIDER = 3;
    public static int SPLIT_AMOUNT = 2;
    private boolean suicided;
    private float height;

    public SlimeBossest() {
        super();
        this.setHp(this.maxHealth * 2);
        this.drawX -= 100 * Settings.scale;
        this.suicided = false;
        this.height = this.hb.height;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SlimeSplitPower(this, SPLIT_AMOUNT), SPLIT_AMOUNT));
        applyInvincible(this);
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
            case 3:
                AbstractMonster mo;
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1.0F, 0.1F));
                AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_SPLIT"));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mo = new SpikeSlimest_L(-385.0F, 20.0F, 0, this.currentHealth), false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new SlimeUnityPower(mo, this)));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mo = new AcidSlimest_L(120.0F, -8.0F, 0, this.currentHealth), false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new SlimeUnityPower(mo, this)));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                this.suicided = true;
                ReflectionHacks.setPrivate(this, SlimeBoss.class, "firstTurn", true);
                break;

            case 4:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SLIME_ATTACK"));
                AbstractCard bossburn = new BossBurn();
                bossburn.upgrade();

                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(bossburn, 3));

                this.setMove((byte)2, Intent.UNKNOWN);
                break;

            default:
                super.takeTurn();
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if(reform(this)) {
            super.die(triggerRelics);
        }
    }

    public static boolean reform(final AbstractMonster mo) {
        if(mo.hasPower(SlimeSplitPower.POWER_ID)) {
            AbstractPower p = mo.getPower(SlimeSplitPower.POWER_ID);
            SlimeBossest sb = new SlimeBossest();
            if(p.amount == 1) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, mo, p));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(mo, mo, p, 1));
            }
            AbstractDungeon.actionManager.addToBottom(new HealAction(mo, mo, mo.maxHealth));
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(mo));

            if(((WeirdSlimeThing)mo).getSuicided()) {
                ((WeirdSlimeThing)mo).resetSuicided();
                InvinciblePower ip = (InvinciblePower) mo.getPower(InvinciblePower.POWER_ID);
                if(ip != null) {
                    ip.amount = 0;
                    ReflectionHacks.setPrivate(ip, InvinciblePower.class, "maxAmt", 0);
                    mo.hb.height = 0;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public static void applyInvincible(AbstractMonster mo) {
        if(!mo.hasPower(InvinciblePower.POWER_ID)) {
            int count = getInvincibleValue(mo);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, mo, new InvinciblePower(mo, count), count));
        }
    }
    public static int getInvincibleValue(AbstractCreature mo) {
        return getInvincibleValue(mo, false);
    }
    public static int getInvincibleValue(AbstractCreature mo, boolean slimeUnity) {
        int count = mo.maxHealth;
        int divider = 1;
        if(!slimeUnity) {
            divider = INVINCIBLE_DIVIDER;
        } else {
            AbstractPower pow = mo.getPower(SlimeSplitPower.POWER_ID);
            if(pow != null) {
                divider += pow.amount;
            }
        }
        if (mo.maxHealth % divider > 0) {
            count++;
        }
        return count;
    }

    @Override
    public boolean getSuicided() {
        return suicided;
    }
    @Override
    public void resetSuicided() {
        this.suicided = false;
    }
    @Override
    public float getHeight() {
        return height;
    }
}
