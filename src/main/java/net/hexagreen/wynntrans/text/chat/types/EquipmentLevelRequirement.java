package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquipmentLevelRequirement extends WynnChatText {
    private final Text equipName;
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§c(.+)§4 is for combat level §c(\\d+)§4\\+ only\\.").matcher(text.getString()).find();
    }

    public EquipmentLevelRequirement(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^§c(.+)§4 is for combat level §c(\\d+)§4\\+ only\\.").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.equipName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.level = Text.literal(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.equipLevelReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, equipName, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
