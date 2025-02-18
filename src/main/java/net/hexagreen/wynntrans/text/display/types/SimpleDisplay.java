package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class SimpleDisplay extends WynnDisplayText {
    private final String keyText;
    private final String valText;
    private final Style styleText;

    public SimpleDisplay(Text text) {
        super(text);
        this.valText = initValText();
        this.keyText = translationKey + DigestUtils.sha1Hex(valText);
        this.styleText = parseStyleCode(inputText.getString()).withParent(getStyle());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(valText.isBlank() || valText.replaceAll("(§.|\\n|À)", "").isBlank()) {
            resultText = inputText;
            return;
        }

        boolean recorded = false;
        if(!getSiblings().isEmpty()) {
            int i = 1;
            if(!inputText.getContent().equals(PlainTextContent.EMPTY)) {
                String valContentText = ((PlainTextContent) inputText.getContent()).string();
                String keyContentText = this.keyText + ".0";
                if(WTS.checkTranslationExist(keyContentText, valContentText)) {
                    resultText = newTranslate(keyContentText).setStyle(styleText);
                }
                else {
                    resultText = Text.literal(valContentText).setStyle(getStyle());
                    debugClass.writeTextAsJSON(inputText, "Display");
                    recorded = true;
                }
            }

            for(Text sibling : getSiblings()) {
                String valText = sibling.getString();
                String keyText = this.keyText + "." + i++;

                if(resultText == null) resultText = Text.empty().setStyle(styleText);
                if(valText.equals("\n")) {
                    resultText.append("\n");
                    continue;
                }

                if(WTS.checkTranslationExist(keyText, valText)) {
                    resultText.append(newTranslate(keyText).setStyle(sibling.getStyle()));
                }
                else {
                    resultText.append(sibling);
                    if(!recorded) {
                        debugClass.writeTextAsJSON(inputText, "Display");
                        recorded = true;
                    }
                }
            }
        }
        else {
            if(WTS.checkTranslationExist(keyText, valText)) {
                resultText = newTranslate(keyText).setStyle(styleText);
            }
            else {
                resultText = inputText;
                debugClass.writeTextAsJSON(inputText, "Display");
            }
        }
    }

    protected String initValText() {
        return inputText.getString().replaceFirst("^(?:§.)+", "");
    }

    protected MutableText newTranslate(String key) {
        return Text.translatable(key);
    }
}
