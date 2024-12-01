package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MerchantName extends WynnDisplayText {
    private final Text icon;
    private final String keyMerchant;
    private final String valMerchant;

    public static boolean typeChecker(Text text) {
        try {
            if(text.getSiblings().getFirst().getStyle().getFont().equals(Identifier.of("minecraft:merchant"))) {
                return text.getString().contains("Merchant");
            }
        }
        catch(Exception ignore) {
        }
        return false;
    }

    public MerchantName(Text text) {
        super(text);
        this.icon = getSibling(0);
        this.valMerchant = getSibling(1).getString().replaceFirst("^\\n§d", "").replaceFirst("\\n§7NPC$", "");
        this.keyMerchant = valMerchant.replaceFirst(" Merchant", "").replaceAll(" ", "");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "merchant.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(icon).append("\n");
        if(WTS.checkTranslationExist(translationKey + keyMerchant, valMerchant)) {
            resultText.append(Text.translatable(translationKey + keyMerchant).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        else {
            resultText.append(Text.literal(valMerchant).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        resultText.append("\n").append(Text.literal("NPC").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
