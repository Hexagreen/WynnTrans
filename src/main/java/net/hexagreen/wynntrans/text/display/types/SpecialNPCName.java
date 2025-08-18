package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SpecialNPCName extends WynnDisplayText {
    private static final Identifier BANNER = Identifier.of("minecraft:banner/pill");
    private final Text icon;
    private final MutableText banner;
    private final String keyName;
    private final String valName;
    private final Style styleName;

    public static boolean typeChecker(Text text) {
        try {
            return text.getSiblings().get(2).getSiblings().getFirst().getStyle().getFont().equals(BANNER)
                    && text.getString().contains("\uE00D\uE00F\uE002\uDB00\uDC02");
        }
        catch(Exception ignore) {
            return false;
        }
    }

    private static Text reformPackedText(Text text) {
        if(text.getSiblings().size() == 1) {
            MutableText result = text.copyContentOnly().setStyle(text.getStyle());
            for(Text t : text.getSiblings().getFirst().getSiblings()) {
                result.append(t);
            }
            return result;
        }
        return text;
    }

    public SpecialNPCName(Text text) {
        super(flatText(text));
        this.icon = getSibling(0);
        this.banner = getSibling(2).copy().append(getSibling(3));
        String name = getSibling(4).getString().split("\\n")[1];
        this.valName = name.replaceAll("§.", "");
        this.keyName = "wytr.name." + normalizeStringForKey(valName);
        this.styleName = parseStyleCode(name);
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text name;
        if(WTS.checkTranslationDoNotRegister(keyName)) {
            name = Text.translatable(keyName).setStyle(styleName);
        }
        else {
            name = Text.literal(valName).setStyle(styleName);
        }

        resultText = Text.empty();
        resultText.append(icon).append("\n")
                .append(banner.setStyle(banner.getStyle().withParent(getStyle(2)))).append("\n")
                .append(name).append("\n")
                .append(getSibling(5).copy().append(getSibling(6)));
    }
}
