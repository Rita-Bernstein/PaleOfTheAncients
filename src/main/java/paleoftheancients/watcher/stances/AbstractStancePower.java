package paleoftheancients.watcher.stances;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AbstractStancePower extends AbstractPower implements InvisiblePower {
    protected float particleTimer;
    protected float particleTimer2;
    protected long sfxId;

    public AbstractStancePower(AbstractCreature owner, String POWER_ID, PowerStrings powerStrings) {
        this.owner = owner;
        this.isTurnBased = false;

        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;
        this.description = powerStrings.DESCRIPTIONS[0];
        this.priority = -99;
        this.particleTimer = 0.0F;
        this.particleTimer2 = 0.0F;
        this.sfxId = -1L;
    }

    @Override
    public void updateDescription() {}
}
