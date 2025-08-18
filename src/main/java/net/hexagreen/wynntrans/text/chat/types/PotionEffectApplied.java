package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Identifications;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PotionEffectApplied extends WynnChatText {

    public static final Pattern POTION_REGEX = Pattern.compile("§.\\[([+-]\\d+)(/[35]s|%|) (.+) for (\\d+ (?:seconds|minutes))]");

    public static boolean typeChecker(Text text) {
        return POTION_REGEX.matcher(text.getString()).find();
    }

    public PotionEffectApplied(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.potionEffect";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String contentString;
        if(getSiblings().size() == 2) {
            Text icon = getSibling(0);
            contentString = inputText.getString().replaceFirst(" " + icon.getString() + " ", " ");
        }
        else contentString = getContentString();

        Matcher matcher = POTION_REGEX.matcher(contentString);
        if(matcher.find()) {
            Style style = parseStyleCode(contentString);
            String amount = matcher.group(1);
            String unit = matcher.group(2);
            Text power;
            if(unit.matches("/\\ds")) {
                power = Text.literal(amount + "/").append(ITime.translateTime(unit.replaceFirst("/", "")));
            }
            else {
                power = Text.literal(amount).append(unit);
            }
            Text effect = Identifications.findIdentification(matcher.group(3)).getTranslatedText();
            if(getSiblings().size() == 2) effect = Text.empty().append(getSibling(0)).append(" ").append(effect);
            Text timer = ITime.translateTime(matcher.group(4));
            resultText = Text.translatable(translationKey, power, effect, timer).setStyle(style);
        }
        else resultText = inputText;
    }
}
