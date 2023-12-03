package net.hexagreen.wynntrans.sign;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.commons.codec.digest.DigestUtils;

public class WynnSign {
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    private static final String rootKey = "wytr.sign.";
    private final Text[] message;

    private WynnSign(Text[] text) {
        this.message = text;
    }

    public static WynnSign get(Text[] text) {
        return new WynnSign(text);
    }

    public static WynnSign get(SignText signText) {
        return new WynnSign(signText.getMessages(false));
    }

    public Text[] translate() {
        translateLine();
        return message;
    }

    private void translateLine() {
        for(int i = 0; 4 > i; i++) {
            if(message[i].equals(Text.empty())) continue;
            if(systemSignFilter(message[i])) break;

            String hash = DigestUtils.sha1Hex(message[i].getString()).substring(0, 24);
            String keySign = rootKey + hash;

            MutableText result = Text.empty();
            int sIndex = 1;
            for(Text sibling : message[i].getSiblings()) {
                if(WTS.checkTranslationDoNotRegister(keySign + "_" + sIndex)) {
                    result.append(Text.translatable(keySign + "_" + sIndex).setStyle(sibling.getStyle()));
                }
                else {
                    result.append(sibling);
                }
                sIndex++;
            }
            message[i] = result;
        }
    }

    public void registerTranslation() {
        for(int i = 0; 4 > i; i++) {
            if(message[i].equals(Text.empty())) continue;
            try {
                if (message[i].getSiblings().get(0).getContent() instanceof TranslatableTextContent) return;
            }
            catch(IndexOutOfBoundsException ignore) {}

            String hash = DigestUtils.sha1Hex(message[i].getString()).substring(0, 24);
            String keySign = rootKey + hash;

            int sIndex = 1;
            for(Text sibling : message[i].getSiblings()) {
                String valSignSibling = sibling.getString();
                WTS.checkTranslationExist(keySign + "_" + sIndex, valSignSibling);
                sIndex++;
            }
        }
    }

    private boolean systemSignFilter(Text text) {
        switch(text.getString().toLowerCase().replaceAll(" ?\\d", "")) {
            case "[merchant]", "[buyer]", "[identifier]", "spawn", "eventswarm", "[powder]",
                    "next wave", "checker", "mob", "pos", "stop checker", "sounds", "sound",
                    "particles", "wave end", "remove wall", "CMD", "RESET CMD", "COMMAND BLOCK", "OPEN CMD" -> {
                return true;
            }
        }
        return false;
    }
}
