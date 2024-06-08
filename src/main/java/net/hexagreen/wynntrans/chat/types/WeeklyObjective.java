package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class WeeklyObjective extends WynnChatText implements ICenterAligned {
    private String keyEName = null;
    private String valEName = null;
    private Style styleEName = null;

    public WeeklyObjective(Text text, Pattern regex) {
        super(text, regex);

        if(text.getSiblings().size() == 8) {
            this.valEName = getSibling(3).getString().replaceAll("\\n +§.§.", "");
            String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
            this.keyEName = "wytr.eventInfo.eventName." + hash2;
            this.styleEName = getStyle(3);
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "guildObj";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        Text t1 = newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append("\n");
        Text t2 = newTranslate(parentKey + ".1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append("\n");
        Text t3 = newTranslate(parentKey + ".2").setStyle(
                Style.EMPTY.withColor(Formatting.DARK_AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild rewards"))).append("\n");
        Text t4 = newTranslate(parentKey + ".3").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)).append("\n");

        resultText.append(getCenterIndent(t1)).append(t1)
                .append(getCenterIndent(t2)).append(t2)
                .append("\n");

        if(keyEName != null) {
            if(WTS.checkTranslationExist(keyEName, valEName)) {
                Text text = newTranslate(keyEName).setStyle(styleEName);
                resultText.append(getCenterIndent(text)).append(text);
            }
            else {
                Text origin = getSibling(3);
                resultText.append(getCenterIndent(origin)).append(origin);
            }
            Text origin = getCrateName(getSibling(4));
            resultText.append("\n").append(getCenterIndent(origin)).append(origin).append("\n\n");
        }

        resultText.append(getCenterIndent(t3)).append(t3)
                .append(getCenterIndent(t4)).append(t4);
    }

    private Text getCrateName(Text text) {
        String string = text.getString().replace("^ +", "");
        return Text.literal(string).setStyle(text.getStyle());
    }
}
