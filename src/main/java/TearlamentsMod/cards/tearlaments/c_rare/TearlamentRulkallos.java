package TearlamentsMod.cards.tearlaments.c_rare;

import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TearlamentRulkallos extends EvolvingCard {
    public static final String ID = makeID(TearlamentRulkallos.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 27;
    private static final int UPG_DAMAGE = 8;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 3;
    private static final int UPG_MAGIC_NUMBER = 5;
    public AbstractCard cardToFuseInto = this;

    public TearlamentRulkallos() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.TEARLAMENTS);
        tags.add(CustomTags.FUSION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        //  Upgrade all tearlament cards in hand
        for (AbstractCard c : p.hand.group){
            if (c.hasTag(CustomTags.TEARLAMENTS)){
                addToBot(new UpgradeSpecificCardAction(c));
            }
        }

        //  Upgrade all tearlament cards in deck
        for (AbstractCard c : p.drawPile.group){
            if (c.hasTag(CustomTags.TEARLAMENTS)){
                addToBot(new UpgradeSpecificCardAction(c));
            }
        }

        //  Upgrade all tearlament cards in discard
        for (AbstractCard c : p.discardPile.group){
            if (c.hasTag(CustomTags.TEARLAMENTS)){
                addToBot(new UpgradeSpecificCardAction(c));
            }
        }

        //  Upgrade all tearlament cards in exhaust
        for (AbstractCard c : p.exhaustPile.group){
            if (c.hasTag(CustomTags.TEARLAMENTS)){
                addToBot(new UpgradeSpecificCardAction(c));
            }
        }
    }

    @Override
    public void triggerOnScry() {
        super.triggerOnScry();
        this.triggerOnManualDiscard();
    }

    @Override
    public void triggerOnManualDiscard() {
        // If discarded: add it to hand and set cost to 0 for the turn.
        super.triggerOnManualDiscard();
        addToBot(new DiscardToHandAction(this));
        for (int i = 0; i < this.cost; i++) {
            addToBot(new ReduceCostForTurnAction(this, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TearlamentRulkallos();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = this;
        this.cardsToPreview = null;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        AbstractCard c = new TearlamentRulkallos();
        if (this.upgraded){
            c.upgrade();
        }
        return c;
    }
}
