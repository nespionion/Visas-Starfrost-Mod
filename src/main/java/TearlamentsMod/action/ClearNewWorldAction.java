package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static TearlamentsMod.VisasMod.rand;
import static TearlamentsMod.VisasMod.setRunSeed;

public class ClearNewWorldAction extends RecycleAction {
    private static final AbstractPlayer p = AbstractDungeon.player;
    private final int drawAmount;
    public static String[] TEXT = new String[2];
    public AbstractCard.CardType cardTypeToGet;
    private boolean isRandom;

    public ClearNewWorldAction(int drawAmount){
        super();
        this.drawAmount = drawAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.isRandom = true;
        this.amount = 1;
        TEXT[0] = "Select a card to discard:";
        TEXT[1] = "Select a card to add to hand";
        setRunSeed();
    }

    public ClearNewWorldAction(int drawAmount, boolean isRandom){
        super();
        this.drawAmount = drawAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.isRandom = isRandom;
        TEXT[0] = "Select a card to discard:";
        TEXT[1] = "Select a card to add to hand";
        setRunSeed();
    }

    @Override
    public void update(){
        if (this.duration == this.startDuration) {
            if (p.hand.isEmpty()) {
                VisasMod.logger.info("No cards in hand");
                this.isDone = true;
                this.tickDuration();
                return;
            } else if (p.hand.size() == 1) {

                AbstractCard cardDiscarded = p.hand.getBottomCard();
                p.hand.moveToDiscardPile(p.hand.getBottomCard());
                cardDiscarded.triggerOnManualDiscard();
                VisasMod.logger.info("Discard only card successful");

                this.clearNewWorldRandom(cardDiscarded);

                this.tickDuration();

            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                this.tickDuration();
            }

        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractCard c = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);

            p.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
            VisasMod.logger.info("Discard Successful");

            this.clearNewWorldRandom(c);

            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

            this.isDone = true;
        }
        this.tickDuration();
        return;
    }

    private void clearNewWorldRandom(AbstractCard c){
        AbstractCard.CardType cardTypeToGet = getCardTypeToGet(c);
        VisasMod.logger.info("Started Random Selection");
        addToTop(new DrawPileToHandAction(1, cardTypeToGet));
    }

    private static AbstractCard.CardType getCardTypeToGet(AbstractCard cardDiscarded) {

        VisasMod.logger.info("Selecting Type");
        if (!setRunSeed()){
            return AbstractCard.CardType.ATTACK;
        }

        ArrayList<AbstractCard.CardType> cardTypes = new ArrayList<AbstractCard.CardType>();
                // {AbstractCard.CardType.ATTACK, AbstractCard.CardType.SKILL, AbstractCard.CardType.POWER};

        if (cardDiscarded.type != AbstractCard.CardType.ATTACK && !p.drawPile.getCardsOfType(AbstractCard.CardType.ATTACK).isEmpty()){
            cardTypes.add(AbstractCard.CardType.ATTACK);
        }
        if (cardDiscarded.type != AbstractCard.CardType.POWER && !p.drawPile.getCardsOfType(AbstractCard.CardType.POWER).isEmpty()) {
            cardTypes.add(AbstractCard.CardType.POWER);
        }
        if (cardDiscarded.type != AbstractCard.CardType.SKILL && !p.drawPile.getCardsOfType(AbstractCard.CardType.SKILL).isEmpty()) {
            cardTypes.add(AbstractCard.CardType.SKILL);
        }
        if (p.drawPile.getCardsOfType(AbstractCard.CardType.SKILL).isEmpty() &&
                p.drawPile.getCardsOfType(AbstractCard.CardType.ATTACK).isEmpty() &&
                p.drawPile.getCardsOfType(AbstractCard.CardType.POWER).isEmpty()){
            return AbstractCard.CardType.ATTACK;
        }

        if (p.drawPile.group.isEmpty() || cardTypes.isEmpty()) {
            return AbstractCard.CardType.ATTACK;
        }

        int n = rand.nextInt(cardTypes.size());
        return cardTypes.get(n);
    }
}
