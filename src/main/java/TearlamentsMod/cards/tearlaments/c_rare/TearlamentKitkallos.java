package TearlamentsMod.cards.tearlaments.c_rare;

import TearlamentsMod.VisasMod;
import TearlamentsMod.action.DrawPileToDiscard;
import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.kashtira.a_common.ScareclawKashtira;
import TearlamentsMod.cards.kashtira.b_uncommon.KashtiraOgre;
import TearlamentsMod.cards.kashtira.b_uncommon.KashtiraRiseheart;
import TearlamentsMod.cards.kashtira.b_uncommon.KashtiraUnicorn;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraFenrir;
import TearlamentsMod.cards.tearlaments.b_uncommon.*;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentRulkallos;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static TearlamentsMod.VisasMod.rand;
import static TearlamentsMod.VisasMod.setRunSeed;

public class TearlamentKitkallos extends EvolvingCard {
    public static final String ID = makeID(TearlamentKitkallos.class.getSimpleName());

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
    private static final int MAGIC_NUMBER = 3;
    private static final int UPG_MAGIC_NUMBER = 2;
    public AbstractCard cardToFuseInto = new TearlamentRulkallos();

    public TearlamentKitkallos() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.TEARLAMENTS);
        tags.add(CustomTags.FUSION);
        this.defineCardToFuseInto();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        ArrayList<AbstractCard> choices = new ArrayList<AbstractCard>();
        choices.add(new TearlamentMerrli());
        choices.add(new TearlamentHavnis());
        choices.add(new TearlamentReinoheart());
        choices.add(new TearlamentScheiren());

        if (!setRunSeed()){
            VisasMod.logger.info("Failed to fetch seed");
            return;
        }

        if (rand.nextInt(2) < 1){
            choices.add(new TearlamentKashtira());
        }

        Collections.shuffle(choices, rand);

        int i = 3;
        while (i < choices.size()) {
            choices.remove(0);
        }

        for (AbstractCard c : choices){
            if (this.upgraded){
                c.upgrade();
            }
            c.setCostForTurn(0);
        }

        addToBot(new ChooseOneAction(choices));
    }

    @Override
    public void triggerOnManualDiscard() {
        // If discarded: add it to hand and set cost to 0 for the turn.
        super.triggerOnManualDiscard();
        addToBot(new DrawPileToDiscard(AbstractDungeon.player, magicNumber));
    }

     

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TearlamentKitkallos();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = new TearlamentRulkallos();
        if (this.upgraded){
            cardToFuseInto.upgrade();
        }
        this.cardsToPreview = cardToFuseInto;
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
