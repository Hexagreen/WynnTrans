package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class WeeklyObjective extends WynnChatText implements ISpaceProvider {
    private String keyEName = null;
    private String valEName = null;
    private Style styleEName = null;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^ \n +§lObjective Finished").matcher(text.getString()).find();
    }

    public WeeklyObjective(Text text) {
        super(text);

        if(text.getSiblings().size() == 8) {
            this.valEName = getSibling(3).getString().replaceAll("\\n +§.§.", "");
            String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
            this.keyEName = "wytr.eventInfo.eventName." + hash2;
            this.styleEName = parseStyleCode(getContentString(3).replaceAll("(.|\\n)+((?:§.)+).+", "$1"));
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.guildObj";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t1 = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)).append("\n");
        Text t2 = Text.translatable(translationKey + ".1").setStyle(GRAY).append("\n");
        Text t3 = Text.translatable(translationKey + ".2").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild rewards"))).append("\n");
        Text t4 = Text.translatable(translationKey + ".3").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append("\n");

        resultText.append(centerAlign(t1)).append(centerAlign(t2)).append("\n");

        if(keyEName != null) {
            if(WTS.checkTranslationExist(keyEName, valEName)) {
                Text text = Text.translatable(keyEName).setStyle(styleEName);
                resultText.append(centerAlign(text));
            }
            else {
                Text origin = Text.literal(valEName).setStyle(styleEName);
                resultText.append(centerAlign(origin));
            }
            Text origin = getCrateName(getSibling(4));
            resultText.append("\n").append(centerAlign(origin)).append("\n\n");
        }

        resultText.append(centerAlign(t3)).append(centerAlign(t4));
    }

    private Text getCrateName(Text text) {
        String string = text.getString().replaceAll("^\\n +(§.)", "$1");
        return Text.literal(string).setStyle(text.getStyle());
    }
}
