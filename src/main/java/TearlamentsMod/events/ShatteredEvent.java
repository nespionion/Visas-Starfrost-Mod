package TearlamentsMod.events;

import TearlamentsMod.cards.generic.c_rare.ViciousAstraloud;
import TearlamentsMod.cards.kashtira.b_uncommon.KashtiraRiseheart;
import TearlamentsMod.cards.mannadium.b_uncommon.MannadiumRiumheart;
import TearlamentsMod.cards.scareclaw.b_uncommon.ScareclawReichheart;
import TearlamentsMod.cards.tearlaments.b_uncommon.TearlamentReinoheart;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.*;

import java.util.ArrayList;
import java.util.HashSet;

import static TearlamentsMod.VisasMod.imagePath;
import static TearlamentsMod.VisasMod.makeID;

public class ShatteredEvent extends PhasedEvent {
    public static final String ID = makeID("ShatteredEvent"); //The event's ID

    //The text that will be displayed in the event, loaded based on the ID. The text will be set up later in this tutorial.
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    //For this example, an image from a basegame event is used.
    //private static final String IMG = "images/events/Shattered.jpg";
    //To use your own image, it would look more like
    private static final String IMG = imagePath("events/Shattered.jpg");
    //This would load yourmod/images/events/ExampleEvent.jpg

    public ShatteredEvent() {
        super(ID, NAME, IMG);

        HashSet<AbstractCard> cardsPreRequirement = new HashSet<AbstractCard>();
        cardsPreRequirement.add(new KashtiraRiseheart());
        cardsPreRequirement.add(new TearlamentReinoheart());
        cardsPreRequirement.add(new ScareclawReichheart());
        cardsPreRequirement.add(new MannadiumRiumheart());

        registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() ->
                                playerHasCards(cardsPreRequirement) &&
                                AbstractDungeon.player.hasRelic("StarfrostRelic"), OPTIONS[1])
                        .setOptionResult((i)->{
                            AbstractDungeon.player.loseRelic("StarfrostRelic");

                            AbstractRelic relic1 = new Vajra();
                            AbstractRelic relic2 = new OddlySmoothStone();
                            AbstractRelic relic3 = new BagOfMarbles();
                            AbstractRelic relic4 = new BloodVial();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic1);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic2);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic3);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic4);

                            AbstractEvent.logMetricObtainRelicAtCost(ID, "Obtained Relic", relic1, 0); //Optional, adds information to run history
                            AbstractEvent.logMetricObtainRelicAtCost(ID, "Obtained Relic", relic2, 0); //Optional, adds information to run history
                            AbstractEvent.logMetricObtainRelicAtCost(ID, "Obtained Relic", relic3, 0); //Optional, adds information to run history
                            AbstractEvent.logMetricObtainRelicAtCost(ID, "Obtained Relic", relic4, 0); //Optional, adds information to run history

                            transitionKey("they took relic");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[2]).enabledCondition(() ->
                                playerHasCards(cardsPreRequirement) &&
                                AbstractDungeon.player.hasRelic("StarfrostRelic"), OPTIONS[1])
                        .setOptionResult((i)->{
                            for (AbstractCard c : AbstractDungeon.player.masterDeck.group){
                                AbstractDungeon.player.masterDeck.removeCard(c);
                            }
                            AbstractDungeon.player.masterDeck.addToTop(new ViciousAstraloud());

                            transitionKey("they took card");
                        }))
                .addOption(OPTIONS[3], (i)->transitionKey("they didn't take")));

        registerPhase("they took relic", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[3], (i)->openMap()));
        registerPhase("they took card", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[3], (i)->openMap()));
        registerPhase("they didn't take", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[3], (i)->openMap()));

        transitionKey("start");

    }

    private boolean playerHasCard(AbstractCard c){
        return AbstractDungeon.player.masterDeck.group.contains(c);
    }

    private boolean playerHasCards(HashSet<AbstractCard> cards){
        for (AbstractCard c : cards){
            if (!this.playerHasCard(c)){return false;}
        }
        return true;
    }




}
