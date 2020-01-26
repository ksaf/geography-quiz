package com.orestis.velen.quiz.helpPowers;

import android.content.Context;
import android.util.SparseArray;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.extraTime.ExtraTimePowerConfigs;
import com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimePowerConfigs;
import com.orestis.velen.quiz.helpPowers.shield.ShieldPowerConfigs;
import com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;
import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;
import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;
import static com.orestis.velen.quiz.helpPowers.extraTime.ExtraTimePowerConfigs.EXTRA_TIME_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs.FIFTY_FIFTY_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimePowerConfigs.FREEZE_TIME_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.shield.ShieldPowerConfigs.SHIELD_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs.SKIP_MAX_LEVEL;

public class PowerDescriptions {

    private Map<String, SparseArray<String>> descriptionsMap = new HashMap<>();
    private Context context;

    public PowerDescriptions(Context context) {
        this.context = context;
        PowerCharges charges = new PowerCharges();

        SparseArray<String> shieldDescriptions = new SparseArray<>();
        ShieldPowerConfigs shieldPowerConfigs = new ShieldPowerConfigs();
        for (int i = 0; i <= SHIELD_MAX_LEVEL; i++) {
            String description = context.getString(R.string.shield_starting_description);
            if(i > 0) {
                description = context.getString(R.string.shield_description_part1) + " " + 
                        shieldPowerConfigs.getTurnDurationForPowerLevel(i) + " " +
                        context.getString(R.string.shield_description_part2) + "."
                        + " <br><b><font color='#000000'><i>"
                        + context.getString(R.string.shield_description_part3) + " " + charges.getChargesFor(SHIELD, i) + "</font></b></i>.";
            }
            shieldDescriptions.put(i, description);
        }

        SparseArray<String> freezeTimeDescriptions = new SparseArray<>();
        FreezeTimePowerConfigs freezeTimePowerConfigs = new FreezeTimePowerConfigs();
        for (int i = 0; i <= FREEZE_TIME_MAX_LEVEL; i++) {
            String description = context.getString(R.string.freeze_time_starting_description);
            if(i > 0) {
                description = context.getString(R.string.freeze_time_description_part1) + " "
                        + freezeTimePowerConfigs.getTurnDurationForPowerLevel(i) / 1000
                        + " " + context.getString(R.string.freeze_time_description_part2)
                        + " <br><b><font color='#000000'><i>"
                        + context.getString(R.string.freeze_time_description_part3) + " " + charges.getChargesFor(FREEZE_TIME, i) + "</font></b></i>.";
            }
            freezeTimeDescriptions.put(i, description);
        }

        SparseArray<String> skipDescriptions = new SparseArray<>();
        SkipPowerConfigs skipPowerConfigs = new SkipPowerConfigs();
        for (int i = 0; i <= SKIP_MAX_LEVEL; i++) {
            String description = context.getString(R.string.skip_starting_description);
            if(i > 0) {
                description += " <br><b><font color='#000000'>"
                        + skipPowerConfigs.getBonusSign(i)
                        + skipPowerConfigs.getBonusChangeForPowerLevel(i) 
                        + " " + context.getString(R.string.skip_description_part1) 
                        + "</font></b>" + "."
                        + " <br><b><font color='#000000'><i>" 
                        + context.getString(R.string.skip_description_part2) + " " +
                        + charges.getChargesFor(SKIP, i) + "</font></b></i>.";
            }
            skipDescriptions.put(i, description);
        }

        SparseArray<String> fiftyFiftyDescriptions = new SparseArray<>();
        FiftyFiftyPowerConfigs fiftyFiftyPowerConfigs = new FiftyFiftyPowerConfigs();
        for (int i = 0; i <= FIFTY_FIFTY_MAX_LEVEL; i++) {
            String description = context.getString(R.string.fifty_fifty_starting_description);
            if(i > 0) {
                description += " <br><b><font color='#000000'>"
                        + fiftyFiftyPowerConfigs.getBonusSign(i)
                        + fiftyFiftyPowerConfigs.getBonusChangeForPowerLevel(i)
                        + " " + context.getString(R.string.fifty_fifty_description_part1)
                        + "</font></b>" + "."
                        + "<br><b><font color='#000000'><i>"
                        + context.getString(R.string.fifty_fifty_description_part2) + " " +
                        + charges.getChargesFor(FIFTY_FIFTY, i) + "</font></b></i>.";
            }
            fiftyFiftyDescriptions.put(i, description);
        }

        SparseArray<String> extraTimeDescriptions = new SparseArray<>();
        ExtraTimePowerConfigs extraTimePowerConfigs = new ExtraTimePowerConfigs();
        for (int i = 0; i <= EXTRA_TIME_MAX_LEVEL; i++) {
            String description = context.getString(R.string.extra_time_starting_description);
            if(i > 0) {
                description += " <br><b><font color='#000000'>+"
                        + extraTimePowerConfigs.getExtraTimeForPowerLevel(i) / 1000
                        + " " + context.getString(R.string.extra_time_description_part1)
                        + "</font></b>" + ".";
            }
            extraTimeDescriptions.put(i, description);
        }

        descriptionsMap.put(SKIP, skipDescriptions);
        descriptionsMap.put(FIFTY_FIFTY, fiftyFiftyDescriptions);
        descriptionsMap.put(SHIELD, shieldDescriptions);
        descriptionsMap.put(FREEZE_TIME, freezeTimeDescriptions);
        descriptionsMap.put(EXTRA_TIME, extraTimeDescriptions);
    }

    public String getDescriptionFor(String powerType, int powerLevel) {
        return descriptionsMap.get(powerType).get(powerLevel);
    }
}
