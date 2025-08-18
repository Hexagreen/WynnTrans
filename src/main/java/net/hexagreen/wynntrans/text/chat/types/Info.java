package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class Info extends WynnSystemText {
    private static final Pattern REGEX = Pattern.compile("\uDAFF\uDFFC\uE01B\uDAFF\uDFFF\uE002\uDAFF\uDFFE|\uDAFF\uDFFC\uE001\uDB00\uDC06");
    private final String hash;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() < 2) return false;
        if(!REGEX.matcher(text.getString()).find()) return false;
        return text.getStyle().getColor() != null && text.getStyle().getColor().getHexCode().equals("#A0AEC0");
    }

    public Info(Text text) {
        super(text);
        StringBuilder sb = new StringBuilder("[Info] ");
        for(Text t : getSiblings()) {
            String str = t.getString();
            sb.append(str);
            if(str.contains("wynn.gg") || str.contains("wynncraft.com")) sb.append(" ");
        }
        String body = sb.toString().trim().replaceAll(" ?\\n ?", " ").replaceAll("  ", " ");
        this.hash = DigestUtils.sha1Hex("[Info] " + body).substring(0, 8);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "info";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        for(int index = 0; getSiblings().size() > index; index++) {
            String keySibling = translationKey + "." + hash + "." + (index + 1);
            String valSibling = getContentString(index).replaceAll(" ?\\n ?", " ");

            if(valSibling.isEmpty()) continue;
            if(valSibling.matches("^§6\\[Event].+")) {
                resultText.append(new InfoEvent(colorCodedToStyled(Text.literal(valSibling))).text());
                return;
            }
            if(valSibling.contains("wynn.gg") || valSibling.contains("wynncraft.com")) {
                resultText.append(Text.literal(valSibling).setStyle(getStyle(index)));
                continue;
            }
            switch(valSibling) {
                case "For more information, visit" -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo").setStyle(getStyle(index)));
                    continue;
                }
                case "Visit" -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo_alt1").setStyle(getStyle(index)));
                    continue;
                }
                case " for more!" -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo_alt2").setStyle(getStyle(index)));
                    continue;
                }
                case " to learn more!" -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo_alt3").setStyle(getStyle(index)));
                    continue;
                }
            }

            if(WTS.checkTranslationExist(keySibling, valSibling)) {
                resultText.append(Text.translatable(keySibling).setStyle(getStyle(index)));
            }
            else {
                resultText.append(Text.literal(valSibling).setStyle(getStyle(index)));
            }
        }
    }
}
