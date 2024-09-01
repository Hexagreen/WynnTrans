package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ICenterAligned;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AreaDiscovery extends WynnChatText implements ICenterAligned {
    private static final Pattern AREA_NAME = Pattern.compile(": §[ef](.+)§d( \\(\\+\\d+ XP\\))");
    private final String keyAreaName;
    private final String valAreaName;
    private final String experience;
    private final Style styleTitle;
    private final Style styleAreaName;
    private final boolean shortForm;

    public AreaDiscovery(Text text, Pattern regex) {
        super(text, regex);
        Matcher areaMatcher = AREA_NAME.matcher(getSibling(1).getString());
        String areaName = areaMatcher.find() ? areaMatcher.group(1) : "UNKNOWN AREA";
        this.keyAreaName = rootKey + "area." + normalizeStringAreaName(areaName);
        this.valAreaName = areaName;
        this.experience = areaMatcher.group(2);
        this.shortForm = text.getSiblings().size() == 2;
        this.styleTitle = shortForm ? Style.EMPTY.withColor(Formatting.GRAY) : Style.EMPTY.withColor(Formatting.GOLD);
        this.styleAreaName = shortForm ? Style.EMPTY.withColor(Formatting.WHITE) : Style.EMPTY.withColor(Formatting.YELLOW);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.areaDiscovered";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        MutableText t0 = Text.empty().append(newTranslate(parentKey).setStyle(styleTitle));
        if(WTS.checkTranslationExist(keyAreaName, valAreaName)) {
            t0.append(newTranslate(keyAreaName).setStyle(styleAreaName))
                    .append(Text.literal(experience).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        else {
            t0.append(Text.literal(valAreaName).setStyle(styleAreaName))
                    .append(Text.literal(experience).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        resultText.append(getCenterIndent(t0)).append(t0).append("\n");

        if(shortForm) return;
        else resultText.append("\n");

        for(int i = 2; inputText.getSiblings().size() > i; i++) {
            MutableText line = Text.empty();
            String keyAreaLore = keyAreaName + "." + (i - 1);
            String valAreaLore = getSibling(i).getString().replaceAll("^ *§7", "");
            if(WTS.checkTranslationExist(keyAreaLore, valAreaLore)) {
                line.append(newTranslate(keyAreaLore).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            else {
                line.append(Text.literal(valAreaLore).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            resultText.append(getCenterIndent(line)).append(line).append("\n");
        }
    }
}
