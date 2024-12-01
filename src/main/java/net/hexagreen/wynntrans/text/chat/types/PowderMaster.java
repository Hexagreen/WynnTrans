package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Elements;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PowderMaster extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("Powder Master: ").matcher(text.getString()).find();
    }

    public PowderMaster(Text text) {
        super(text, null);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.powderMaster";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty().append(header).setStyle(getStyle());

        resultText.append(Text.translatable(translationKey).append(": "));

        if(getContentString(1).contains("I can't infuse powders")) {
            resultText.append(newTranslateWithSplit(translationKey + ".cantInfuse").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("I can't remove powders")) {
            resultText.append(newTranslateWithSplit(translationKey + ".cantRemove").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("You can only upgrade")) {
            resultText.append(newTranslateWithSplit(translationKey + ".cantUpgrade").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("An item is required")) {
            resultText.append(newTranslateWithSplit(translationKey + ".itemFirst").setStyle(getStyle(1)));
        }
        else if(getContentString(1).contains("You are attempting to")) {
            Text powderType = Elements.findElement(getContentString(1).replaceAll("\n", "").replaceAll(".+ upgrade an ", "").replaceAll(" powder, so .+", "")).getElement();
            resultText.append(newTranslateWithSplit(translationKey + ".upgradeIllegal", powderType).setStyle(getStyle(1)));
        }
        else throw new TextTranslationFailException("PowderMaster.class");
    }
}
