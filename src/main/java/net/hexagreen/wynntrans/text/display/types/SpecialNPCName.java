package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SpecialNPCName extends WynnDisplayText {
    private static final Identifier BANNER = Identifier.of("minecraft:banner/pill");

    public static boolean typeChecker(Text text) {
        try {
            return text.getSiblings().get(2).getSiblings().getFirst().getStyle().getFont().equals(BANNER)
                    && text.getString().contains("\uE00D\uE00F\uE002\uDB00\uDC02");
        }
        catch(Exception ignore) {
            return false;
        }
    }

    public SpecialNPCName(Text text) {
        super(flatText(text));
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        int adjust = getSibling(0).getString().isBlank() ? -1 : 0;
        Text icon = adjust == -1 ? Text.empty() : getSibling(0);
        MutableText banner = getSibling(2 + adjust).copy().append(getSibling(3 + adjust));
        String _name = getSibling(4 + adjust).getString().split("\\n")[1];
        String valName = _name.replaceAll("§.", "");
        String keyName = "wytr.name." + normalizeStringForKey(valName);
        Style styleName = parseStyleCode(_name);

        Text name;
        if(WTS.checkTranslationDoNotRegister(keyName)) {
            name = Text.translatable(keyName).setStyle(styleName);
        }
        else {
            name = Text.literal(valName).setStyle(styleName);
        }

        resultText = Text.empty();
        resultText.append(icon).append("\n")
                .append(banner).append("\n")
                .append(name).append("\n")
                .append(getSibling(5 + adjust).copy().append(getSibling(6 + adjust)));
    }
}
