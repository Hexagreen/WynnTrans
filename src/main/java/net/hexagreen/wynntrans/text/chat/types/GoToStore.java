package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class GoToStore extends WynnChatText {
    private final Text linkText;
    private final String key;
    private final String val;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("wynncraft\\.com/store").matcher(text.getString()).find();
    }

    public GoToStore(Text text) {
        super(text);
        this.linkText = findLinkText();
        this.val = inputText.getString().replaceFirst("wynncraft\\.com/store[\\w/]*", "%s");
        this.key = translationKey + DigestUtils.sha1Hex(val);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "text.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        if(WTS.checkTranslationExist(key, val)) {
            resultText = Text.translatable(key, linkText).setStyle(getStyle());
        }
        else {
            resultText = inputText;
        }
    }

    private Text findLinkText() {
        for(Text sibling : getSiblings()) {
            if(sibling.getString().contains("wynncraft.com/store")) {
                return sibling;
            }
        }
        return null;
    }
}
