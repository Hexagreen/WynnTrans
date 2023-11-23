package net.hexagreen.wynntrans.chat;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class WeeklyObjective extends WynnChatText implements ICenterAligned {
    private String keyEName = null;
    private String valEName = null;

    protected WeeklyObjective(Text text, Pattern regex) {
        super(text, regex);

        if(text.getSiblings().size() == 8) {
            this.valEName = getSibling(3).getString().replaceAll("\\n +§.§.", "");
            String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
            this.keyEName = "wytr.eventInfo.eventName." + hash2;
        }
    }

    public static WeeklyObjective of(Text text, Pattern regex) {
        return new WeeklyObjective(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "guildObj";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        Text t1 = newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append("\n");
        Text t2 = newTranslate(parentKey + "_1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append("\n");
        Text t3 = newTranslate(parentKey + "_2").setStyle(
                Style.EMPTY.withColor(Formatting.DARK_AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild rewards"))).append("\n");
        Text t4 = newTranslate(parentKey + "_3").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append("\n");

        resultText.append(getCenterIndent(t1)).append(t1)
                .append(getCenterIndent(t2)).append(t2)
                .append("\n");

        if(keyEName != null) {
            if(WTS.checkTranslationExist(keyEName, valEName)) {
                Text text = newTranslate(keyEName);
                resultText.append(getCenterIndent(text)).append(text);
            }
            else {
                Text origin = getSibling(3);
                resultText.append(getCenterIndent(origin)).append(origin);
            }
            Text origin = getSibling(4);
            resultText.append("\n").append(getCenterIndent(origin)).append(origin).append("\n\n");
        }

        resultText.append(getCenterIndent(t3)).append(t3)
                .append(getCenterIndent(t4)).append(t4);
    }
}