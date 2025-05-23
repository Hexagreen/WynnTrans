package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class SystemNPCName extends WynnDisplayText {
    private static final Identifier MERCHANT = Identifier.of("minecraft:merchant");
    private final Text icon;

    public static boolean typeChecker(Text text) {
        try {
            if(text.getSiblings().getFirst().getStyle().getFont().equals(MERCHANT)) {
                return text.getString().substring(0, 1).matches("[\uE002-\uE008]");
            }
        }
        catch(Exception ignore) {
        }
        return false;
    }

    public SystemNPCName(Text text) {
        super(text);
        this.icon = getSibling(0);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        MutableText npcName = Text.empty();
        MutableText npcDesc = Text.empty();

        String str = inputText.getString();
        Style purple = Style.EMPTY.withColor(Formatting.LIGHT_PURPLE);
        if(str.contains("Blacksmith")) {
            npcName.append(Text.translatable("wytr.func.blacksmith").setStyle(purple));
            npcDesc.append(Text.translatable("wytr.display.blacksmith.desc").setStyle(GRAY));
        }
        else if(str.contains("Identifier")) {
            npcName.append(Text.translatable("wytr.func.identifier").setStyle(purple));
            npcDesc.append(getSibling(2).getSiblings().getFirst());
        }
        else if(str.contains("Powder Master")) {
            npcName.append(Text.translatable("wytr.func.powderMaster").setStyle(purple));
            npcDesc.append(getSibling(2).getSiblings().getFirst());
        }
        else if(str.contains("Party Finder")) {
            npcName.append(Text.translatable("wytr.func.partyFinder").setStyle(purple));
            npcDesc.append(Text.translatable("wytr.display.partyFinder.desc").setStyle(GRAY));
        }
        else if(str.contains("Key Collector")) {
            npcName.append(Text.translatable("wytr.func.keyCollector").setStyle(purple));
            npcDesc.append(getSibling(2).getSiblings().getFirst());
        }
        else if(str.contains("Housing Master")) {
            npcName.append(Text.translatable("wytr.display.housingMaster").setStyle(purple));
            npcDesc.append(Text.translatable("wytr.display.housingMaster.desc").setStyle(GRAY));
        }
        else if(str.contains("Trade Market")) {
            npcName.append(Text.translatable("wytr.display.tradeMarket").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            npcDesc.append(Text.translatable("wytr.display.tradeMarket.desc").setStyle(GRAY));
        }
        else throw new TextTranslationFailException("SystemNPCName");

        resultText = Text.empty();
        resultText.append(icon).append("\n");
        resultText.append(npcName).append("\n");
        resultText.append(npcDesc);
    }
}
