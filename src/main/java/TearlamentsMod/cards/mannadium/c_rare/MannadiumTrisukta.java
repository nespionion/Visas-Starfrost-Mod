package TearlamentsMod.cards.mannadium.c_rare;

import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.character.Visas;
import TearlamentsMod.orbs.FearlessOrb;
import TearlamentsMod.orbs.MeekOrb;
import TearlamentsMod.orbs.TorridOrb;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;

import java.util.ArrayList;
import java.util.Collections;

import static TearlamentsMod.VisasMod.rand;

public class MannadiumTrisukta extends EvolvingCard {
    public static final String ID = makeID(MannadiumTrisukta.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 18;
    private static final int UPG_DAMAGE = 5;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 1;
    public AbstractCard cardToFuseInto = this;

    public MannadiumTrisukta() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.MANNADIUM);
        tags.add(CustomTags.FUSION);
        this.defineCardToFuseInto();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (this.upgraded) {
            this.addToBot(new ChannelAction(getRandomOrb(true)));
        }

        this.addToBot(new ChannelAction(getRandomOrb(true)));
    }


    public static AbstractOrb getRandomOrb(boolean useCardRng) {
        ArrayList<AbstractOrb> orbs = new ArrayList<AbstractOrb>();
        orbs.add(new MeekOrb());
        orbs.add(new TorridOrb());
        orbs.add(new FearlessOrb());

        Collections.shuffle(orbs, rand);

        return orbs.get(0);
    }


    @Override
    public AbstractCard makeCopy() { //Optional
        return new MannadiumTrisukta();
    }

    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = this;
        this.cardsToPreview = null;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        AbstractCard c = new MannadiumTrisukta();
        if (this.upgraded){
            c.upgrade();
        }
        return c;
    }
}
