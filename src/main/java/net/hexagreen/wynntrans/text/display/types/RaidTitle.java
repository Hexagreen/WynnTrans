package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RaidTitle extends WynnDisplayText {
    private final Text raidName;
    private final String levelReq;
    private final Text questReq;

    public static boolean typeChecker(Text text) {
        return text.getString().contains("\uE011\uE000\uE008\uE003\uDB00\uDC02");
    }

    public RaidTitle(Text text) {
        super(text);
        String[] split = text.getString().split("\\n");
        this.raidName = getRaidName(split[1]);
        this.levelReq = split[3].replaceAll(".+: (§.+)$", "$1");
        this.questReq = getQuestReq(split[4].replaceAll(".+: (§.+)$", "$1"));
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text level = Text.translatable("wytr.requirement.combat", levelReq).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        Text quest = Text.translatable("wytr.requirement.quest", questReq).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        resultText = Text.empty().setStyle(getStyle())
                .append(getSibling(0)).append("\n")
                .append(raidName).append("\n\n")
                .append(level).append("\n")
                .append(quest).append("\n");
    }

    private Text getRaidName(String string) {
        Style style = parseStyleCode(string.replaceAll("((?:§.)+).+", "$1"));
        String valName = string.replaceAll("^(?:§.)+", "");
        String keyName = "wytr.raid." + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return Text.translatable(keyName).setStyle(style);
        }
        else {
            return Text.literal(valName).setStyle(style);
        }
    }

    private Text getQuestReq(String string) {
        Style style = parseStyleCode(string.replaceAll("((?:§.)+).+", "$1"));
        String valName = string.replaceAll("^(?:§.)+", "");
        String keyName = "wytr.quest" + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return Text.translatable(keyName).setStyle(style);
        }
        else {
            return Text.literal(valName).setStyle(style);
        }
    }
}
