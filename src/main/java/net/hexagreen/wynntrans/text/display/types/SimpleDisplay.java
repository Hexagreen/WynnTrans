package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
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
        this.valText = inputText.getString().replaceFirst("^(?:§.)+", "");
        this.keyText = parentKey + DigestUtils.sha1Hex(valText);
        this.styleText = parseStyleCode(inputText.getString().replace(valText, "")).withParent(getStyle());
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(valText.isBlank() || valText.replaceAll("(§0|\\n|À)", "").isBlank()) {
            resultText = inputText;
            return;
        }

        boolean recorded = false;
        if(!getSiblings().isEmpty()) {
            int i = 1;
            if(!inputText.getContent().equals(PlainTextContent.EMPTY)) {
                String valContentText = ((PlainTextContent) inputText.getContent()).string();
                String keyContentText = this.keyText + "_0";
                if(WTS.checkTranslationExist(keyContentText, valContentText)) {
                    resultText = Text.translatable(keyContentText).setStyle(styleText);
                }
                else {
                    resultText = Text.literal(valContentText).setStyle(styleText);
                    debugClass.writeTextAsJSON(inputText, "Display");
                    recorded = true;
                }
            }

            for(Text sibling : getSiblings()) {
                String valText = sibling.getString();
                String keyText = this.keyText + "_" + i++;

                if(resultText == null) resultText = Text.empty().setStyle(styleText);
                if(valText.equals("\n")) {
                    resultText.append("\n");
                    continue;
                }

                if(WTS.checkTranslationExist(keyText, valText)) {
                    resultText.append(Text.translatable(keyText).setStyle(sibling.getStyle()));
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
                resultText = Text.translatable(keyText).setStyle(styleText);
            }
            else {
                resultText = inputText;
                debugClass.writeTextAsJSON(inputText, "Display");
            }
        }
    }
}
