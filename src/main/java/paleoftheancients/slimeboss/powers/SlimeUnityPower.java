package paleoftheancients.slimeboss.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.slimeboss.monsters.SlimeBossest;
import paleoftheancients.slimeboss.monsters.WeirdSlimeThing;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;


public class SlimeUnityPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("SlimeUnityPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public AbstractCreature boss;

    public SlimeUnityPower(AbstractCreature owner, AbstractCreature boss) {
        super();
        this.owner = owner;

        this.loadRegion("dexterity");

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.boss = boss;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(!mo.isDeadOrEscaped() && mo.hasPower(ID) && ((SlimeUnityPower)mo.getPower(ID)).boss == boss) {
                        return;
                    }
                }
                InvinciblePower ip = (InvinciblePower)boss.getPower(InvinciblePower.POWER_ID);
                if(ip != null) {
                    ip.amount = SlimeBossest.getInvincibleValue(boss, true);
                    if(ip.amount == boss.maxHealth) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(boss, owner, ip));
                    } else {
                        ReflectionHacks.setPrivate(ip, InvinciblePower.class, "maxAmt", ip.amount);
                    }
                }
                if(boss instanceof WeirdSlimeThing) {
                    boss.hb.height = ((WeirdSlimeThing)boss).getHeight();
                }
                boss.showHealthBar();
                boss.useShakeAnimation(1.2F);
            }
        });
        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
