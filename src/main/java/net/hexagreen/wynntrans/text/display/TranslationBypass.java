package net.hexagreen.wynntrans.text.display;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TranslationBypass extends WynnDisplayText {
    private static final Pattern CRATE_REWARD_REGEX = Pattern.compile(
            "\uE012\uE00A\uE008\uE00D\uE012|" +
                    "\uE00F\uE004\uE013\uE012|" +
                    "\uE003\uE008\uE012\uE006\uE014\uE008\uE012\uE004\uE012|" +
                    "\uE004\uE005\uE005\uE004\uE002\uE013\uE012" +
                    "\uE012\uE013\uE018\uE00B\uE004");

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() == 3
                && text.getSiblings().getFirst().getStyle().getFont().getPath().equals("banner/ribbon")
                && CRATE_REWARD_REGEX.matcher(text.getSiblings().getLast().getString()).find())
            return true;
        return text.getString().contains("\uE003\uE008\uE012\uE006\uE014\uE008\uE012\uE004\uE003\uDB00\uDC02");
    }


    public TranslationBypass(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
    }
}
