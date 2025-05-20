package TearlamentsMod.cards.generic.a_common;

import TearlamentsMod.VisasMod;
import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.generic.c_rare.ViciousAstraloud;
import TearlamentsMod.cards.kashtira.b_uncommon.KashtiraRiseheart;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraAriseHeart;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraShangriIra;
import TearlamentsMod.cards.mannadium.b_uncommon.MannadiumRiumheart;
import TearlamentsMod.cards.mannadium.c_rare.MannadiumPrimeHeart;
import TearlamentsMod.cards.mannadium.c_rare.MannadiumTrisukta;
import TearlamentsMod.cards.scareclaw.b_uncommon.ScareclawReichheart;
import TearlamentsMod.cards.scareclaw.c_rare.ScareclawTriHeart;
import TearlamentsMod.cards.tearlaments.b_uncommon.TearlamentReinoheart;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentKaleidoHeart;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentKitkallos;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentRulkallos;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static TearlamentsMod.VisasMod.rand;
import static TearlamentsMod.VisasMod.setRunSeed;

public class AgesOfStarsAndFrost extends BaseCard {
    public static final String ID = makeID(AgesOfStarsAndFrost.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 0;
    private static final int UPG_DAMAGE = 0;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 98;

    public AgesOfStarsAndFrost() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE,UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);
        setCostUpgrade(0);

        this.purgeOnUse = true;
        setRunSeed();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<AbstractCard>();
        choices.add(new TearlamentReinoheart());
        choices.add(new MannadiumRiumheart());
        choices.add(new ScareclawReichheart());
        choices.add(new KashtiraRiseheart());
        if (!setRunSeed()){
            VisasMod.logger.info("Failed to fetch seed");
            return;
        }
        Collections.shuffle(choices, rand);

        int i = 3;
        while (i < choices.size()) {
            choices.remove(0);
        }
        addToBot(new ChooseOneAction(choices));
    }

}
