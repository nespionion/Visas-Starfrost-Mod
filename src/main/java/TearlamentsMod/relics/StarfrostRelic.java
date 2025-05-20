package TearlamentsMod.relics;

import TearlamentsMod.VisasMod;
import TearlamentsMod.cards.generic.e_generated.Anger;
import TearlamentsMod.cards.generic.e_generated.Fear;
import TearlamentsMod.cards.generic.e_generated.Peace;
import TearlamentsMod.cards.generic.e_generated.Sorrow;
import TearlamentsMod.character.Visas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.DevaPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static TearlamentsMod.VisasMod.*;

public class StarfrostRelic extends BaseRelic{

    private static final String NAME = "StarfrostRelic"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public StarfrostRelic() {
        super(ID, NAME, Visas.Meta.CARD_COLOR, RARITY, SOUND);
        rand = null;
    }

    @Override
    public void obtain() {
        super.obtain();
        setRunSeed();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
//        ArrayList<AbstractCard> stanceChoices = new ArrayList();
//        stanceChoices.add(new Anger());
//        stanceChoices.add(new Peace());
//        stanceChoices.add(new Sorrow());
//        stanceChoices.add(new Fear());
//
//        this.addToBot(new ChooseOneAction(stanceChoices));

        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractPower> powers = new ArrayList<AbstractPower>();

        if (!setRunSeed()){
            VisasMod.logger.info("Failed to fetch seed");
            return;
        }

        int randomBounded = rand.nextInt(100); // 0 to 99

        if (randomBounded < 60){
            powers.add(new VigorPower(p, 1));
            powers.add(new PlatedArmorPower(p, 1));
            powers.add(new BlurPower(p, 1));
        } else if (randomBounded < 85) {
            powers.add(new DexterityPower(p, 1));
            powers.add(new StrengthPower(p, 1));
            powers.add(new MetallicizePower(p, 1));
        } else if (randomBounded < 95){
            powers.add(new ThousandCutsPower(p, 1));
            powers.add(new AfterImagePower(p, 1));
        } else if (randomBounded < 99){
            powers.add(new BufferPower(p, 1));
            powers.add(new ArtifactPower(p, 1));
            powers.add(new IntangiblePlayerPower(p, 1));
        } else {
            powers.add(new EchoPower(p, 1));
            powers.add(new DemonFormPower(p, 1));
            powers.add(new DevaPower(p));
        }

        Collections.shuffle(powers, rand);

        addToBot(new ApplyPowerAction(p, p, powers.get(0)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
