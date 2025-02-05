package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class SimpleTitle extends WynnTitleText {
    private final String keyTitle;

    public SimpleTitle(Text text) {
        super(text);
        this.keyTitle = translationKey + DigestUtils.sha1Hex(text.getString());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String contentString = getContentString();
        if(!contentString.isEmpty()) {
            if(WTS.checkTranslationExist(keyTitle, contentString)) {
                resultText = Text.translatable(keyTitle).setStyle(getStyle());
            }
            else {
                resultText = Text.literal(contentString).setStyle(getStyle());
            }
        }
        else {
            resultText = Text.empty();
        }

        if(!getSiblings().isEmpty()) {
            int i = 1;
            for(Text sibling : getSiblings()) {
                String keySibling = keyTitle + "." + i++;
                if(WTS.checkTranslationExist(keySibling, sibling.getString())) {
                    resultText.append(Text.translatable(keySibling).setStyle(sibling.getStyle()));
                }
                else {
                    resultText.append(sibling);
                }
            }
        }
    }
}
