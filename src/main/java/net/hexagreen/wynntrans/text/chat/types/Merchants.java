package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Merchants extends WynnSystemText {
    private final String keyMerchantName;
    private final String valMerchantName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("([\\w ]+) Merchant:").matcher(text.getString()).find();
    }

    public Merchants(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("([\\w ]+) Merchant:").matcher(inputText.getString().replaceAll("\n", ""));
        boolean ignore = matcher.find();
        String merchantName = matcher.group(1);
        this.keyMerchantName = "wytr.merchant." + merchantName.replace(" ", "");
        this.valMerchantName = merchantName + " Merchant";
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.merchant";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle());

        if(WTS.checkTranslationExist(keyMerchantName, valMerchantName)) {
            resultText.append(Text.translatable(keyMerchantName).append(": "));
        }
        else {
            resultText.append(getSibling(0)).append(" ");
        }

        if(lineFeedRemover(getContentString(1)).contains("Thank you for your business")) {
            resultText.append(Text.translatable(translationKey + ".confirm").setStyle(getStyle(1)));
        }
        else if(lineFeedRemover(getContentString(1)).contains("cannot afford that")) {
            resultText.append(Text.translatable(translationKey + ".noEmerald").setStyle(getStyle(1)));
        }
        else if(lineFeedRemover(getContentString(1)).contains("don't have enough space")) {
            resultText.append(Text.translatable(translationKey + ".noSpace").setStyle(getStyle(1)));
        }
    }
}
