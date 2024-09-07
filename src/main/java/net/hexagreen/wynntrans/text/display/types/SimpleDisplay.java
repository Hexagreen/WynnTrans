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
        return rootKey + "normalDisplay.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(valText.isEmpty() || valText.matches("(§.)+") || valText.equals("À") || valText.equals("§0\n§0")) {
            resultText = inputText;
            return;
        }

        boolean recorded = false;
        if(!inputText.getSiblings().isEmpty()) {
            int i = 1;
            if(!inputText.getContent().equals(PlainTextContent.EMPTY)) {
                String valContentText = ((PlainTextContent) inputText.getContent()).string();
                String keyContentText = this.keyText + "_0";
                if (WTS.checkTranslationExist(keyContentText, valContentText)) {
                    resultText = newTranslate(keyContentText).setStyle(styleText);
                } else {
                    resultText = Text.literal(valContentText).setStyle(styleText);
                    debugClass.writeString2File(inputText.getString(), "getString.txt", "Display");
                    debugClass.writeTextAsJSON(inputText, "Display");
                    recorded = true;
                }
            }

            for(Text sibling : inputText.getSiblings()) {
                String valText = sibling.getString();
                String keyText = this.keyText + "_" + i++;

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
                        debugClass.writeString2File(inputText.getString(), "getString.txt", "Display");
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
                debugClass.writeString2File(inputText.getString(), "literal.txt");
                debugClass.writeTextAsJSON(inputText, "Display");
            }
        }
    }
}
