package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;

public class NPCName extends WynnDisplayText {
    private final String valNpcName;
    private final String keyNpcName;
    private final Text npcTalk;
    private final String keyNpcTalk;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().isEmpty()) return false;
        if(text.getString().matches(".+\\nNPC")) return true;
        return text.copyContentOnly().getString().matches("^.+\\n$")
                && text.getSiblings().size() == 1
                && Objects.equals(text.getStyle().getColor(), TextColor.fromFormatting(Formatting.DARK_GREEN));
    }

    public NPCName(Text text) {
        super(text);
        this.valNpcName = getContentString().replaceAll("\n", "");
        String normalizedName = normalizeStringForKey(valNpcName);
        this.keyNpcName = translationKey + normalizedName;
        this.npcTalk = getSibling(0);
        this.keyNpcTalk = rootKey + "talk." + normalizedName + "." + DigestUtils.sha1Hex(npcTalk.getString()).substring(0, 4);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "name.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(WTS.checkTranslationExist(keyNpcName, valNpcName)) {
            resultText = Text.translatable(keyNpcName).setStyle(getStyle()).append("\n");
        }
        else {
            resultText = inputText.copyContentOnly();
        }
        
        if(!npcTalk.getString().equals("NPC")) {
            resultText.append(SimpleDisplay.translateTextTree(npcTalk, keyNpcTalk));
        }
        else resultText.append(npcTalk);
    }
}
