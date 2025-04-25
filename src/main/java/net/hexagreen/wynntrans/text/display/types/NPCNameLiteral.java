package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class NPCNameLiteral extends WynnDisplayText {
    private final String valNpcName;
    private final String keyNpcName;
    private final String subKeyNpcName;
    private final Style styleNpcName;
    private final String valNpcTalk;
    private final String keyNpcTalk;
    private final Style styleNpcTalk;
    private final Text npcTag;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() == 1 && text.getString().matches("(?s).+\uE00D\uE00F\uE002\uDB00\uDC02$"))
            return true;
        if(!text.getSiblings().isEmpty()) return false;
        if(text.getString().contains(" Post §2[")) return false;
        return text.getString().contains("\uE00D\uE00F\uE002\uDB00\uDC02");
    }

    public NPCNameLiteral(Text text) {
        super(text);
        String content = getContentString();
        String[] slices = content.split("\\n");
        String npcName = slices.length == 2 ? slices[1] : slices[0];
        String npcTalk = slices.length == 2 ? slices[0] : "";
        this.styleNpcName = parseStyleCode(npcName).withParent(getStyle());
        this.styleNpcTalk = parseStyleCode(npcTalk);
        this.valNpcName = npcName.replaceFirst("(§.)+", "");
        this.valNpcTalk = npcTalk.replaceFirst("(§.)+", "");
        String npcNameNormalized = normalizeStringForKey(this.valNpcName);
        this.keyNpcName = translationKey + npcNameNormalized;
        this.subKeyNpcName = "wytr.mobName." + npcNameNormalized;
        this.keyNpcTalk = valNpcTalk.isBlank() ? null : rootKey + "talk." + npcNameNormalized + "." + DigestUtils.sha1Hex(valNpcTalk).substring(0, 4);
        this.npcTag = getSibling(0);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "name.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(valNpcName.contains("\uE060")) {
            resultText = inputText;
            return;
        }

        resultText = Text.empty();

        if(keyNpcTalk != null) {
            resultText.append(Text.translatableWithFallback(keyNpcTalk, valNpcTalk).setStyle(styleNpcTalk));
            resultText.append("\n");
        }

        if(WTS.checkTranslationDoNotRegister(subKeyNpcName)) {
            resultText.append(Text.translatable(subKeyNpcName).setStyle(styleNpcName));
        }
        else if(WTS.checkTranslationDoNotRegister(keyNpcName)) {
            resultText.append(Text.translatable(keyNpcName).setStyle(styleNpcName));
        }
        else resultText.append(Text.literal(valNpcName).setStyle(styleNpcName));

        resultText.append("\n");
        resultText.append(npcTag);
    }
}
