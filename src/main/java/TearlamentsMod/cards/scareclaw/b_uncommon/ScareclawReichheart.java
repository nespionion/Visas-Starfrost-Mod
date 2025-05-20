package TearlamentsMod.cards.scareclaw.b_uncommon;

import TearlamentsMod.VisasMod;
import TearlamentsMod.action.EvolveSelfCardAction;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraAriseHeart;
import TearlamentsMod.cards.scareclaw.c_rare.ScareclawTriHeart;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ScareclawReichheart extends EvolvingCard {
    public static final String ID = makeID(ScareclawReichheart.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 9;
    private static final int UPG_DAMAGE = 4;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 2;
    private static final int UPG_MAGIC_NUMBER = -1;
    public AbstractCard cardToFuseInto = new ScareclawTriHeart();

    public ScareclawReichheart() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);


        tags.add(CustomTags.SCARECLAW);
        tags.add(CustomTags.CREATURE);
        this.defineCardToFuseInto();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        boolean thisIsFound = AbstractDungeon.actionManager.cardsPlayedThisTurn.remove(this);

        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= magicNumber){
            addToBot(new EvolveSelfCardAction(this));
            VisasMod.logger.info("Starting Evolve Self action");
            this.purgeOnUse = true;
        } else {
            this.purgeOnUse = false;
        }

        if (thisIsFound){AbstractDungeon.actionManager.cardsPlayedThisTurn.add(this);}
    }



    @Override
    public AbstractCard makeCopy() { //Optional
        return new ScareclawReichheart();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = new ScareclawTriHeart();
        if (this.upgraded){
            cardToFuseInto.upgrade();
        }
        this.cardsToPreview = cardToFuseInto;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        AbstractCard c = new ScareclawTriHeart();
        if (this.upgraded){
            c.upgrade();
        }
        return c;
    }
}
