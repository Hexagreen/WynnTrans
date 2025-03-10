package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class SimpleSystemText extends WynnSystemText {
    private static boolean translationRegisterControl = true;
    private final String keyText;
    private final String valText;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE |^\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ").matcher(removeTextBox(text)).find();
    }

    public static void setTranslationControl(boolean control) {
        translationRegisterControl = control;
    }

    public SimpleSystemText(Text text) {
        super(text);
        this.valText = initValText();
        this.keyText = initKeyText();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "text.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(valText.isEmpty() || valText.equals(" ")) {
            resultText = originText.copy();
            return;
        }

        boolean recorded = false;
        if(getSiblings().size() > 1) {
            int i = 1;
            for(Text sibling : getSiblings()) {
                String valText = lineFeedReplacer(sibling.getString());
                String keyText = this.keyText + "." + i++;

                if(checkTranslationExistWithControl(keyText, valText)) {
                    if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
                    resultText.append(newTranslateWithSplit(keyText).setStyle(sibling.getStyle()));
                }
                else {
                    if(resultText == null) resultText = originText.copy();
                    if(translationRegisterControl && !recorded) {
                        debugClass.writeTextAsJSON(originText, "SystemText");
                        debugClass.writeTextAsJSON(inputText, "    --    ");
                        recorded = true;
                    }
                }
            }
        }
        else {
            if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText.append(newTranslateWithSplit(keyText).setStyle(getStyle(0)));
            }
            else {
                resultText = originText.copy();
                if(translationRegisterControl) {
                    debugClass.writeTextAsJSON(originText, "SystemLiteral");
                }
            }
        }
    }

    protected String initValText() {
        return lineFeedReplacer(inputText.getString());
    }

    private String initKeyText() {
        return translationKey + DigestUtils.sha1Hex(replacerRemover(valText));
    }

    private boolean checkTranslationExistWithControl(String key, String value) {
        if(translationRegisterControl) {
            return WTS.checkTranslationExist(key, value);
        }
        else {
            return WTS.checkTranslationDoNotRegister(key);
        }
    }
}
