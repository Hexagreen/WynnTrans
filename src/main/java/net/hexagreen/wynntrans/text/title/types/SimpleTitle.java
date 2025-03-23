package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;

public class SimpleTitle extends WynnTitleText {
    private final String keyTitle;
    private final boolean canSimplify;
    private String simplifiedKey;
    private boolean recorded;

    public SimpleTitle(Text text) {
        super(flatText(text));
        this.keyTitle = translationKey + DigestUtils.sha1Hex(getValText(text.getString()));
        this.canSimplify = isCanSimplify(inputText);
        this.recorded = false;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(!emptyChecker(inputText.copyContentOnly().setStyle(getStyle()))) {
            String contentString = getContentString().replaceFirst("^(§.)+", "");
            Style contentStyle = parseStyleCode(getContentString()).withParent(getStyle());
            if(canSimplify) {
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
                    String keySibling = keyTitle + "." + i++;
                    String valSibling = sibling.getString().replaceFirst("^(§.)+", "");
                    Style styleSibling = parseStyleCode(sibling.getString()).withParent(sibling.getStyle());
                    if(canSimplify) {
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
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean emptyChecker(Text text) {
        String string = text.getString().replaceAll("§.", "");
        return string.isEmpty() || string.matches("Á+|\\d+|\\.+|[✹❉❋✦✤]") || !Objects.equals(text.getStyle().getFont(), Identifier.ofVanilla("default"));
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
