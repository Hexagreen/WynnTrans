package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.CratesTexts;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class CrateReward extends WynnDisplayText {
    private final CratesTexts.Crates tier;
    private final Text type;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("§.(?:Common|Rare|Epic|Godly|§4§8§k...§4 Black Market §8§k...§4) Reward\\n.+");
    }

    public CrateReward(Text text) {
        super(text);
        String[] split = text.getString().split("\\n");
        this.tier = CratesTexts.Crates.find(split[0]);
        this.type = CratesTexts.RewardType.find(split[1]).getTypeText();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.crateReward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, tier.getGradeText()).setStyle(tier.getGradeStyle())).append("\n").append(type);
    }
}
