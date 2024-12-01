package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class SpecialNPCName extends WynnDisplayText {
    private final Text icon;
    private final MutableText banner;
    private final String keyName;
    private final String valName;
    private final Style styleName;

    public static boolean typeChecker(Text text) {
        try {
            return text.getSiblings().get(2).getSiblings().getFirst().getStyle().getFont().equals(Identifier.of("minecraft:banner/pill"))
                    && text.getString().contains("\n§7NPC");
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
        super(reformPackedText(text));
        this.icon = getSibling(0);
        this.banner = getSibling(2).getSiblings().getFirst().copy();
        String name = getSibling(2).getSiblings().getLast().getString().split("\\n")[1];
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
        resultText.append(icon).append("\n").append(banner.setStyle(banner.getStyle().withParent(getStyle(2)))).append("\n").append(name).append("\n").append(Text.literal("NPC").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
