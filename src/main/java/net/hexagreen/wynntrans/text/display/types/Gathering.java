package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Locale;

public class Gathering extends WynnDisplayText {
    private final String lineFeed;
    private final Style bracketStyle;
    private final String barStyle;
    private final double progress;
    private final String profession;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().replaceAll("§.", "").matches("\\n*\\[\\|\\|\\|.+\\|\\|\\|]");
    }

    public Gathering(Text text) {
        super(text);
        this.lineFeed = inputText.getString().replaceAll("[^\\n]", "");
        String bracket = inputText.getString().replace("\n", "");
        this.bracketStyle = parseStyleCode(bracket.substring(0, 2));
        this.barStyle = bracket.substring(3, 5);
        String bar = bracket.replaceAll("§.[\\[\\]]", "");
        String[] split = bar.split("§8");
        int color = split[0].replaceFirst("§.", "").length();
        int gray = split.length == 2 ? split[1].length() : 0;
        this.progress = (double) color / (color + gray);
        this.profession = bar.replaceAll("§.", "").replaceAll("\\W", "").toLowerCase(Locale.ENGLISH);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.gathering";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        StringBuilder bar = new StringBuilder(Text.translatable(translationKey + "." + profession).getString());
        bar.insert((int) (progress * bar.length()), "§8");

        resultText = Text.empty().append(lineFeed);
        resultText.append(Text.translatable(translationKey, barStyle + bar).setStyle(bracketStyle));
    }
}
