package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SlayCounter extends WynnDisplayText {
    private final MutableText counter;
    private String num;
    private String mobName;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 3) return false;
        return text.getString().contains("\uE012\uE00B\uE000\uE018\uE02A\uDB00\uDC02") || text.getString().contains("\uE007\uE014\uE00D\uE013\uE02A\uDB00\uDC02");
    }

    public SlayCounter(Text text) {
        super(text);
        this.counter = Text.empty();
        text.visit((s, t) -> {
            String styleName = Objects.requireNonNull(s.getColor()).getName();
            if(styleName.equals("aqua") && t.matches("\\d+ .+\\n")) {
                String[] split = t.split(" ", 2);
                this.num = split[0];
                this.mobName = split[1].replace("\n", "");
            }
            else if(styleName.equals("dark_green") || styleName.equals("green")) {
                counter.append(Text.literal(t).setStyle(s));
            }
            return Optional.empty();
        }, text.getStyle());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.killCounter";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text t0 = Text.literal(num).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
        Text t1;

        String normalized = "wytr.mobName." + normalizeStringForKey(mobName);
        List<String> singularForms = new ArrayList<>();
        singularForms.add(normalized);
        if(normalized.matches(".+s$")) singularForms.add(normalized.replaceFirst("s$", ""));
        if(normalized.matches(".+es$")) singularForms.add(normalized.replaceFirst("es$", ""));
        if(normalized.matches(".+ies$")) singularForms.add(normalized.replaceFirst("ies$", "y"));
        String key = singularForms.stream()
                .filter(WTS::checkTranslationDoNotRegister)
                .findFirst().orElse(normalized);

        if(WTS.checkTranslationExist(key, mobName)) {
            t1 = Text.translatable(key).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
        }
        else {
            t1 = Text.literal(mobName).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
        }

        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n\n").append(Text.translatable(translationKey, t0, t1).setStyle(GRAY)).append("\n\n").append(counter);
    }
}
