package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.enums.CratesTexts;
import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.Text;

public class CrateRewardTier extends WynnTitleText {
    private final CratesTexts.Crates crate;

    public static boolean typeChecker(Text text) {
        return text.getString().replaceAll("\\||§.", "").matches("(Common|Rare|Epic|Godly|Black Market) Tier");
    }

    public CrateRewardTier(Text text) {
        super(text);
        this.crate = CratesTexts.Crates.find(inputText.getString().replaceAll("§.|\\|| |Tier", ""));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.crateTier";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, crate.getGradeText()).setStyle(crate.getGradeStyle());
    }
}
