package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

public class NPCNameLiteral extends WynnDisplayText {
    private final String valNpcName;
    private final String keyNpcName;
    private final String subKeyNpcName;
    private final Style styleNpcName;
    private final String valNpcTalk;
    private final String keyNpcTalk;
    private final Style styleNpcTalk;
    private final String trailer;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() == 1 && text.getString().contains("\n§7NPC")) return true;
        if(!text.getSiblings().isEmpty()) return false;
        if(text.getString().contains(" Post §2[")) return false;
        return text.getString().contains("\n§7NPC");
    }

    public NPCNameLiteral(Text text) {
        super(text);
        String content = inputText.getString().replaceAll("\\n§7NPC(?:\\n|.)*", "");
        String npcName = content.replaceFirst("(?:.|\\n)+\\n", "");
        String npcTalk = content.replaceFirst("\\n?.+$", "");
        this.styleNpcName = parseStyleCode(npcName);
        this.styleNpcTalk = parseStyleCode(npcTalk);
        this.valNpcName = npcName.replaceFirst("(§.)+", "");
        this.valNpcTalk = npcTalk.replaceFirst("(§.)+", "");
        String npcNameNormalized = normalizeStringForKey(this.valNpcName);
        this.keyNpcName = translationKey + npcNameNormalized;
        this.subKeyNpcName = "wytr.mobName." + npcNameNormalized;
        this.keyNpcTalk = valNpcTalk.isBlank() ? null : rootKey + "talk." + npcNameNormalized + "." + DigestUtils.sha1Hex(valNpcTalk).substring(0, 4);
        this.trailer = inputText.getString().replaceFirst("(?:.|\\n)+§7NPC\\n?", "");
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
            if(WTS.checkTranslationExist(keyNpcTalk, valNpcTalk)) {
                resultText.append(Text.translatable(keyNpcTalk).setStyle(styleNpcTalk));
            }
            else resultText.append(Text.literal(valNpcTalk).setStyle(styleNpcTalk));

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
        resultText.append(Text.literal("NPC").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        if(!trailer.isEmpty()) {
            resultText.append("\n");
            resultText.append(trailer);
        }
    }
}
