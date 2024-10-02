package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Merchants extends WynnSystemText {
    private final String keyMerchantName;
    private final String valMerchantName;

    public Merchants(Text text, Pattern regex) {
        super(text, regex);
        String merchantName = matcher.group(1);
        this.keyMerchantName = "wytr.merchant." + merchantName.replace(" ", "");
        this.valMerchantName = merchantName + " Merchant";
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.merchant";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append(header).setStyle(getStyle());

        if(WTS.checkTranslationExist(keyMerchantName, valMerchantName)) {
            resultText.append(newTranslate(keyMerchantName).append(": "));
        }
        else {
            resultText.append(getSibling(0)).append(" ");
        }

        if(lineFeedRemover(getContentString(1)).contains("Thank you for your business")) {
            resultText.append(newTranslate(parentKey + ".confirm").setStyle(getStyle(1)));
        }
        else if(lineFeedRemover(getContentString(1)).contains("cannot afford that")) {
            resultText.append(newTranslate(parentKey + ".noEmerald").setStyle(getStyle(1)));
        }
        else if(lineFeedRemover(getContentString(1)).contains("don't have enough space")) {
            resultText.append(newTranslate(parentKey + ".noSpace").setStyle(getStyle(1)));
        }
    }
}
