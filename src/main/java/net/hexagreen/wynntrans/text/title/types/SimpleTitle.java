package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.title.ProxySimpleTitle;
import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTitle extends WynnTitleText {
    private final String keyTitle;
    private final boolean canSimplify;
    private String simplifiedKey;
    private boolean recorded;

    public SimpleTitle(Text text) {
        super(flatText(text));
        this.keyTitle = translationKey + DigestUtils.sha1Hex(getValText(text.getString()).replaceFirst("^(§.)+", ""));
        this.canSimplify = isCanSimplify(inputText);
        this.recorded = false;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String strippedKey = translationKey + DigestUtils.sha1Hex(inputText.getString().replaceAll("§.", ""));
        if(!emptyChecker(inputText.copyContentOnly().setStyle(getStyle()))) {
            String contentString = getContentString().replaceFirst("^(§.)+", "");
            Style contentStyle = parseStyleCode(getContentString()).withParent(getStyle());
            MutableText tmp;
            if(!(this instanceof ProxySimpleTitle) && (tmp = translateColorShifted(strippedKey, getContentString(), contentStyle)) != null) {
                resultText = tmp;
            }
            else if(canSimplify) {
                if(WTS.checkTranslationExist(simplifiedKey, getValText(contentString))) {
                    resultText = newTranslate(simplifiedKey).setStyle(contentStyle);
                }
                else {
                    recordUnregistered();
                    resultText = Text.literal(contentString).setStyle(contentStyle);
                }
            }
            else if(WTS.checkTranslationExist(keyTitle, getValText(contentString))) {
                resultText = newTranslate(keyTitle).setStyle(contentStyle);
            }
            else {
                recordUnregistered();
                resultText = Text.literal(contentString).setStyle(contentStyle);
            }
        }
        else {
            resultText = inputText.copyContentOnly().setStyle(getStyle());
        }

        if(!getSiblings().isEmpty()) {
            int i = 1;
            for(Text sibling : getSiblings()) {
                if(!emptyChecker(sibling.copy().setStyle(sibling.getStyle().withParent(getStyle())))) {
                    String keySibling = keyTitle + "." + i;
                    String valSibling = sibling.getString().replaceFirst("^(§.)+", "");
                    Style styleSibling = parseStyleCode(sibling.getString()).withParent(sibling.getStyle());
                    MutableText tmp;
                    if((tmp = translateColorShifted(strippedKey + "." + i, sibling.getString(), styleSibling)) != null) {
                        resultText.append(tmp);
                    }
                    else if(canSimplify) {
                        if(WTS.checkTranslationExist(simplifiedKey, getValText(valSibling))) {
                            resultText.append(newTranslate(simplifiedKey).setStyle(styleSibling));
                        }
                        else {
                            recordUnregistered();
                            resultText.append(sibling);
                        }
                    }
                    else if(WTS.checkTranslationExist(keySibling, getValText(valSibling))) {
                        resultText.append(newTranslate(keySibling).setStyle(styleSibling));
                    }
                    else {
                        recordUnregistered();
                        resultText.append(sibling);
                    }
                }
                else {
                    resultText.append(sibling);
                }
                i++;
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean emptyChecker(Text text) {
        String string = text.getString().replaceAll("§.", "");
        return string.isEmpty() || string.matches("Á+|\\d+|\\.+|[✹❉❋✦✤]|[^A-z]+") || !Objects.equals(text.getStyle().getFont(), Identifier.ofVanilla("default"));
    }

    private boolean isCanSimplify(Text text) {
        int counter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for(Text sibling : text.getSiblings()) {
            if(!sibling.getString().isEmpty() && sibling.getStyle().getFont().equals(Identifier.of("minecraft:default"))) {
                counter++;
                stringBuilder.append(sibling.getString());
            }
        }
        if(counter == 1) {
            this.simplifiedKey = translationKey + DigestUtils.sha1Hex(getValText(stringBuilder.toString()));
            return true;
        }
        return false;
    }

    private MutableText translateColorShifted(String key, String string, Style style) {
        string = string + "§r";

        Matcher m = Pattern.compile("(§.)+.(§.)+.(§.)+|(§.)+.(§.)+").matcher(string);
        int leadingCodeLength = string.replaceFirst("^((?:§.)+).+", "$1").length();
        int appendedCodeLength = 0;
        if(m.find()) {
            String notColored = string.replaceAll("§.", "");
            StringBuilder translatedString;
            if(WTS.checkTranslationExist(key, notColored)) {
                translatedString = new StringBuilder(Text.translatable(key).getString());
            }
            else {
                recordUnregistered();
                translatedString = new StringBuilder(notColored);
            }
            int translatedStringLength = translatedString.length();
            do {
                String[] colorCode = m.group().split("(?<!§).(?=§)");
                int codeLength = m.group().length() - (colorCode.length - 1);
                int codeIndex = Math.max(0, m.start() - leadingCodeLength - appendedCodeLength);
                int insertIndex = (int) (((double) codeIndex / notColored.length()) * translatedStringLength) + appendedCodeLength;
                if(colorCode.length == 3)
                    translatedString.insert(Math.min(insertIndex + 2, translatedStringLength), colorCode[2]);
                translatedString.insert(Math.min(insertIndex + 1, translatedStringLength), colorCode[1]);
                translatedString.insert(insertIndex, colorCode[0]);
                appendedCodeLength += codeLength;
            }
            while(m.find());
            return Text.literal(translatedString.toString()).setStyle(style);
        }
        return null;
    }

    protected MutableText newTranslate(String key) {
        return Text.translatable(key);
    }

    protected String getValText(String original) {
        return original;
    }

    private void recordUnregistered() {
        if(!recorded) debugClass.writeTextAsJSON(inputText, "Title");
        recorded = true;
    }
}
