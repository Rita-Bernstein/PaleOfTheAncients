package paleoftheancients.watcher.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.purple.TalkToTheHand;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.ironcluck.powers.ChickenBarrierPower;
import paleoftheancients.ironcluck.powers.ChickenBurnPower;
import paleoftheancients.ironcluck.vfx.CuccoSwarmEffect;
import paleoftheancients.thevixen.helpers.RandomPoint;
import paleoftheancients.thevixen.vfx.RandomAnimatedSlashEffect;
import paleoftheancients.watcher.stances.CalmStancePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WatcherBoss extends CustomMonster {
    public static final String ID = PaleMod.makeID("WatcherBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private Bone eyeBone;
    protected TextureAtlas eyeAtlas = null;
    protected Skeleton eyeSkeleton;
    public AnimationState eyeState;
    protected AnimationStateData eyeStateData;

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte TALKTOTHEHAND = 0;

    private int turnCounter;

    public WatcherBoss() {
        super(NAME, ID, 650, 0.0F, -20.0F, 220.0F, 220.0F, (String)null, -125.0F, 20.0F);

        this.moves = new HashMap<>();
        this.moves.put(TALKTOTHEHAND, new EnemyMoveInfo(TALKTOTHEHAND, Intent.ATTACK_DEBUFF, 22, 0, false));
        this.turnCounter = 0;

        this.loadAnimation("images/characters/watcher/idle/skeleton.atlas", "images/characters/watcher/idle/skeleton.json", 1F);
        this.eyeAtlas = new TextureAtlas(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.eyeAtlas);
        json.setScale(Settings.scale / 1.0F);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.json"));
        this.eyeSkeleton = new Skeleton(skeletonData);
        this.eyeSkeleton.setColor(Color.WHITE);
        this.eyeSkeleton.setFlipX(true);
        this.eyeStateData = new AnimationStateData(skeletonData);
        this.eyeState = new AnimationState(this.eyeStateData);
        this.eyeStateData.setDefaultMix(0.2F);
        this.eyeState.setAnimation(0, "None", true);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.7F);
        this.eyeBone = this.skeleton.findBone("eye_anchor");

        this.flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CalmStancePower(this)));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        int multiplier;
        switch(this.nextMove) {

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        this.turnCounter++;
    }

    @Override
    public void getMove(int num) {
        this.setMoveShortcut(TALKTOTHEHAND);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void changeState(String newState) {
        switch(newState) {
            case "CALM":
                this.eyeState.setAnimation(0, "Calm", true);
                break;
            case "DIVINITY":
                this.eyeState.setAnimation(0, "Divinity", true);
                break;
            case "WRATH":
                this.eyeState.setAnimation(0, "Wrath", true);
                break;
            case "NONE":
            default:
                this.eyeState.setAnimation(0, "None", true);
        }

    }


    @Override
    public void damage(DamageInfo info) {
        int prevhp = this.currentHealth;
        super.damage(info);

        if(prevhp > this.currentHealth) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = new String[] {
                PaleMod.getCardName(TalkToTheHand.class)
        };
        DIALOG = monsterStrings.DIALOG;
    }
}
