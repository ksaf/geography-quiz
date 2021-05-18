package com.orestis.velen.quiz.helpPowers;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.extraTime.ExtraTimePowerConfigs;
import com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimePowerConfigs;
import com.orestis.velen.quiz.helpPowers.shield.ShieldPowerConfigs;
import com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs;

import java.text.DecimalFormatSymbols;
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

    private final Map<String, SparseArray<String>> descriptionsMap = new HashMap<>();
    private final Map<String, String> descriptionsTitleMap = new HashMap<>();
    private final Map<String, SparseArray<String>> bonusMap = new HashMap<>();
    private final Map<String, SparseArray<String>> bonusSignMap = new HashMap<>();
    private final Map<String, String> bonusTextMap = new HashMap<>();
    private final Map<String, String> usagesTextMap = new HashMap<>();
    private final Map<String, SparseIntArray> usagesMap = new HashMap<>();
    private final Map<String, String> displayNameMap = new HashMap<>();

    public PowerDescriptions(Context context) {
        PowerCharges charges = new PowerCharges();

        SparseArray<String> shieldDescriptions = new SparseArray<>();
        SparseArray<String> shieldBonuses = new SparseArray<>();
        SparseArray<String> shieldBonusesSign = new SparseArray<>();
        SparseIntArray shieldUsages = new SparseIntArray();
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
            String infinitySign = DecimalFormatSymbols.getInstance().getInfinity();
            String shieldDuration = shieldPowerConfigs.getTurnDurationForPowerLevel(i) < 99 ?
                    String.valueOf(shieldPowerConfigs.getTurnDurationForPowerLevel(i)) :
                    infinitySign;
            shieldBonuses.put(i, shieldDuration);
            shieldBonusesSign.put(i, "");
            shieldUsages.put(i, charges.getChargesFor(SHIELD, i));
        }

        SparseArray<String> freezeTimeDescriptions = new SparseArray<>();
        SparseArray<String> freezeTimeBonuses = new SparseArray<>();
        SparseArray<String> freezeTimeBonusesSign = new SparseArray<>();
        SparseIntArray freezeTimeUsages = new SparseIntArray();
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
            freezeTimeBonuses.put(i, String.valueOf(freezeTimePowerConfigs.getTurnDurationForPowerLevel(i) / 1000));
            freezeTimeBonusesSign.put(i, "");
            freezeTimeUsages.put(i, charges.getChargesFor(FREEZE_TIME, i));
        }

        SparseArray<String> skipDescriptions = new SparseArray<>();
        SparseArray<String> skipBonuses = new SparseArray<>();
        SparseArray<String> skipBonusesSign = new SparseArray<>();
        SparseIntArray skipUsages = new SparseIntArray();
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
            skipBonuses.put(i, String.valueOf(skipPowerConfigs.getBonusChangeForPowerLevel(i)));
            skipBonusesSign.put(i, skipPowerConfigs.getBonusSign(i));
            skipUsages.put(i, charges.getChargesFor(SKIP, i));
        }

        SparseArray<String> fiftyFiftyDescriptions = new SparseArray<>();
        SparseArray<String> fiftyFiftyBonuses = new SparseArray<>();
        SparseArray<String> fiftyFiftyBonusesSign = new SparseArray<>();
        SparseIntArray fiftyFiftyUsages = new SparseIntArray();
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
            fiftyFiftyBonuses.put(i, String.valueOf(fiftyFiftyPowerConfigs.getBonusChangeForPowerLevel(i)));
            fiftyFiftyBonusesSign.put(i, fiftyFiftyPowerConfigs.getBonusSign(i));
            fiftyFiftyUsages.put(i, charges.getChargesFor(FIFTY_FIFTY, i));
        }

        SparseArray<String> extraTimeDescriptions = new SparseArray<>();
        SparseArray<String> extraTimeBonuses = new SparseArray<>();
        SparseArray<String> extraTimeBonusesSign = new SparseArray<>();
        SparseIntArray extraTimeUsages = new SparseIntArray();
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
            extraTimeBonuses.put(i, String.valueOf(extraTimePowerConfigs.getExtraTimeForPowerLevel(i) / 1000));
            extraTimeBonusesSign.put(i, "");
            extraTimeUsages.put(i, 0);
        }

        descriptionsMap.put(SKIP, skipDescriptions);
        descriptionsMap.put(FIFTY_FIFTY, fiftyFiftyDescriptions);
        descriptionsMap.put(SHIELD, shieldDescriptions);
        descriptionsMap.put(FREEZE_TIME, freezeTimeDescriptions);
        descriptionsMap.put(EXTRA_TIME, extraTimeDescriptions);

        descriptionsTitleMap.put(SKIP, context.getString(R.string.skip_starting_description));
        descriptionsTitleMap.put(FIFTY_FIFTY, context.getString(R.string.fifty_fifty_starting_description));
        descriptionsTitleMap.put(SHIELD, context.getString(R.string.shield_starting_description));
        descriptionsTitleMap.put(FREEZE_TIME, context.getString(R.string.freeze_time_starting_description));
        descriptionsTitleMap.put(EXTRA_TIME, context.getString(R.string.extra_time_starting_description));

        bonusMap.put(SKIP, skipBonuses);
        bonusMap.put(FIFTY_FIFTY, fiftyFiftyBonuses);
        bonusMap.put(SHIELD, shieldBonuses);
        bonusMap.put(FREEZE_TIME, freezeTimeBonuses);
        bonusMap.put(EXTRA_TIME, extraTimeBonuses);

        bonusSignMap.put(SKIP, skipBonusesSign);
        bonusSignMap.put(FIFTY_FIFTY, fiftyFiftyBonusesSign);
        bonusSignMap.put(SHIELD, shieldBonusesSign);
        bonusSignMap.put(FREEZE_TIME, freezeTimeBonusesSign);
        bonusSignMap.put(EXTRA_TIME, extraTimeBonusesSign);

        bonusTextMap.put(SKIP, context.getString(R.string.description_bonus));
        bonusTextMap.put(FIFTY_FIFTY, context.getString(R.string.description_bonus));
        bonusTextMap.put(SHIELD, context.getString(R.string.description_turns));
        bonusTextMap.put(FREEZE_TIME, context.getString(R.string.description_duration));
        bonusTextMap.put(EXTRA_TIME, context.getString(R.string.description_extra_time));

        usagesMap.put(SKIP, skipUsages);
        usagesMap.put(FIFTY_FIFTY, fiftyFiftyUsages);
        usagesMap.put(SHIELD, shieldUsages);
        usagesMap.put(FREEZE_TIME, freezeTimeUsages);
        usagesMap.put(EXTRA_TIME, extraTimeUsages);

        usagesTextMap.put(SKIP, context.getString(R.string.description_charges));
        usagesTextMap.put(FIFTY_FIFTY, context.getString(R.string.description_charges));
        usagesTextMap.put(SHIELD, context.getString(R.string.description_charges));
        usagesTextMap.put(FREEZE_TIME, context.getString(R.string.description_charges));
        usagesTextMap.put(EXTRA_TIME, "");

        displayNameMap.put(SKIP, context.getString(R.string.description_power_name_skip));
        displayNameMap.put(FIFTY_FIFTY, context.getString(R.string.description_power_name_fifty_fifty));
        displayNameMap.put(SHIELD, context.getString(R.string.description_power_name_shield));
        displayNameMap.put(FREEZE_TIME, context.getString(R.string.description_power_name_freeze_time));
        displayNameMap.put(EXTRA_TIME, context.getString(R.string.description_power_name_extra_time));
    }

    public String getDescriptionFor(String powerType, int powerLevel) {
        return descriptionsMap.get(powerType).get(powerLevel);
    }

    public String getDescriptionTitle(String powerType) {
        return descriptionsTitleMap.get(powerType);
    }

    public String getBonus(String powerType, int powerLevel) {
        return bonusMap.get(powerType).get(powerLevel);
    }

    public String getBonusSign(String powerType, int powerLevel) {
        return bonusSignMap.get(powerType).get(powerLevel);
    }

    public String getBonusText(String powerType) {
        return bonusTextMap.get(powerType);
    }

    public int getUsages(String powerType, int powerLevel) {
        return usagesMap.get(powerType).get(powerLevel);
    }

    public String getUsagesText(String powerType) {
        return usagesTextMap.get(powerType);
    }

    public String getDisplayName(String powerType) {
        return displayNameMap.get(powerType);
    }

}
