package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MerchantName extends WynnDisplayText {
    private static final Identifier MERCHANT = Identifier.of("minecraft:merchant");

    public static boolean typeChecker(Text text) {
        try {
            if(text.getSiblings().getFirst().getStyle().getFont().equals(MERCHANT)) {
                return text.getString().contains("Merchant");
            }
        }
        catch(Exception ignore) {
        }
        return false;
    }

    public MerchantName(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "merchant.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text icon = getSibling(0);
        String valMerchant = ((PlainTextContent) getSibling(2).getContent()).string().replaceFirst("\\n", "");
        String keyMerchant = normalizeStringForKey(valMerchant.replaceFirst(" Merchant", ""));
        Text npcTag = getSibling(2).getSiblings().getFirst();

        resultText = Text.empty().setStyle(getStyle());
        resultText.append(icon).append("\n");
        if(WTS.checkTranslationExist(translationKey + keyMerchant, valMerchant)) {
            resultText.append(Text.translatable(translationKey + keyMerchant).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        else {
            resultText.append(Text.literal(valMerchant).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        resultText.append("\n").append(npcTag);
    }
}
