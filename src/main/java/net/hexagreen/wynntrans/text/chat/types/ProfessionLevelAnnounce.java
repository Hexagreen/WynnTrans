package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfessionLevelAnnounce extends WynnChatText {
    private final String level;
    private final Text playerName;
    private final Text profession;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching §flevel (\\d+) in §f(.)§7 (.+)§7!$").matcher(text.getString()).find();
    }

    public ProfessionLevelAnnounce(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching §flevel (\\d+) in §f(.)§7 (.+)§7!$").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.level = "§f" + matcher.group(2);
        String profIcon = matcher.group(3) + " ";
        String keyProfName = "wytr.profession." + matcher.group(4).toLowerCase();
        this.playerName = Text.literal(matcher.group(1));
        this.profession = Text.literal(profIcon).setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(Text.translatable(keyProfName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if(!text.getSiblings().isEmpty()) throw new TextTranslationFailException("ProfessionLevelAnnounce.class");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.levelAnnounce.profession";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        resultText.append(Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(Text.literal("!")).append(Text.literal("] ").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(Text.translatable(translationKey, playerName, level, profession));
    }
}
