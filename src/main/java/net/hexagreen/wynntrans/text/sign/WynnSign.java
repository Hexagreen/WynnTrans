package net.hexagreen.wynntrans.text.sign;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;

public class WynnSign {
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    private static final String rootKey = "wytr.sign.";
    private final Text[] message;

    public static WynnSign get(Text[] text) {
        return new WynnSign(text);
    }

    public static WynnSign get(SignText signText) {
        return new WynnSign(signText.getMessages(false));
    }

    public static MutableText translateRecommendLevel(Text text) {
        MutableText result = Text.empty();
        text.visit((_style, _string) -> {
            if(!_string.matches("(§.)?\\[?Lv\\. [\\d/]+\\+?]?")) {
                result.append(Text.literal(_string).setStyle(_style));
                return Optional.empty();
            }
            String colorCodeStripped = _string.replaceAll("^ | $", "").replaceFirst("^ ?(§.)+ ?", "");
            boolean bracketed = colorCodeStripped.matches("^\\[.+]$");
            String num = colorCodeStripped.replaceAll(".+?([\\d/]+\\+?).+", "$1");
            Style style = WynnTransText.parseStyleCode(_string).withParent(_style);
            Text level = Text.translatable("wytr.unit.level", num);
            if(bracketed) {
                result.append(Text.empty().setStyle(style).append("[").append(level).append("]"));
            }
            else {
                result.append(Text.empty().setStyle(style).append(level));
            }
            return Optional.empty();
        }, Style.EMPTY);
        return result;
    }

    private WynnSign(Text[] text) {
        this.message = text;
    }

    public Text[] translate() {
        translateLine();
        return message;
    }

    private void translateLine() {
        for(int i = 0; i < message.length; i++) {
            if(message[i].equals(Text.empty())) continue;
            Text forceTranslated = tryForceTranslation(message[i]);
            if(forceTranslated != null) message[i] = forceTranslated;

            String hash = DigestUtils.sha1Hex(message[i].getString()).substring(0, 24);
            String keySign = rootKey + hash;

            MutableText result;
            if(WTS.checkTranslationDoNotRegister(keySign)) {
                result = Text.translatable(keySign).setStyle(message[i].getStyle());
            }
            else {
                result = message[i].copyContentOnly();
            }

            int sIndex = 1;
            for(Text sibling : message[i].getSiblings()) {
                if(WTS.checkTranslationDoNotRegister(keySign + "." + sIndex)) {
                    result.append(Text.translatable(keySign + "." + sIndex).setStyle(sibling.getStyle()));
                }
                else {
                    result.append(sibling);
                }
                sIndex++;
            }
            message[i] = result;
        }
    }

    private Text tryForceTranslation(Text text) {
        if(text.getString().matches("(§.)?\\[?Lv\\. \\d+\\+?]?")) {
            return translateRecommendLevel(text);
        }
        else return null;
    }

    public void registerTranslation() {
        for(Text text : message) {
            if(text.equals(Text.empty())) continue;
            try {
                if(text.getContent() instanceof TranslatableTextContent) return;
            }
            catch(IndexOutOfBoundsException ignore) {
            }

            String hash = DigestUtils.sha1Hex(text.getString()).substring(0, 24);
            String keySign = rootKey + hash;
            String valSign = text.copyContentOnly().getString();

            if(!valSign.isEmpty()) {
                WTS.checkTranslationExist(keySign, valSign);
            }

            int sIndex = 1;
            for(Text sibling : text.getSiblings()) {
                if(sibling.getContent() instanceof TranslatableTextContent) return;

                String valSignSibling = sibling.getString();
                if(valSignSibling.isEmpty()) continue;
                WTS.checkTranslationExist(keySign + "." + sIndex, valSignSibling);
                sIndex++;
            }
        }
        WynnTransText.transportMessage(Text.translatable("wytr.command.signTranslationRegistered"));
    }
}
