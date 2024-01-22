package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Merchants extends WynnChatText {
    private final String keyMerchantName;
    private final String valMerchantName;

    public Merchants(Text text, Pattern regex) {
        super(text, regex);
        String merchantName = matcher.group(1);
        this.keyMerchantName = "wytr.merchant." + merchantName.replace(" ","");
        this.valMerchantName = merchantName + " Merchant";
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "merchant";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        if(WTS.checkTranslationExist(keyMerchantName, valMerchantName)) {
            resultText.append(newTranslate(keyMerchantName).setStyle(getStyle(0)));
            resultText.append(Text.literal(": ").setStyle(getStyle(0)));
        }
        else {
            resultText.append(getSibling(0));
        }

        if(getContentString(1).contains("Thank you for your business")) {
            resultText.append(newTranslate(parentKey + ".confirm").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("cannot afford that")) {
            resultText.append(newTranslate(parentKey + ".noEmerald").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("don't have enough space")) {
            resultText.append(newTranslate(parentKey + ".noSpace").setStyle(getStyle(1)));
        }
    }
}
