package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildInfo extends WynnChatText {
    private final GuildInformation infoType;

    public GuildInfo(Text text, Pattern regex) {
        super(text, regex);
        this.infoType = GuildInformation.findAndGet(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "guildInfo";
    }

    @Override
    protected void build() {
        resultText = newTranslate(parentKey);

        switch(infoType) {
            case WEEK_OBJ_COMPLETE -> {
                resultText.append(newTranslate(parentKey + ".weekObj.complete", infoType.matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
            case WEEK_OBJ_EXPIRE -> {
                Text time = Text.literal(infoType.matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
                resultText.append(newTranslate(parentKey + ".weekObj.expire", time).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
            case WEEK_OBJ_NEW -> {
                resultText.append(newTranslate(parentKey + "weekObj.new").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
            default ->  {
                debugClass.writeTextAsJSON(inputText, "UnknownGuildInfo");
            }
        }
    }

    private enum GuildInformation {
        WEEK_OBJ_COMPLETE(Pattern.compile("^ (.+) has finished their weekly objective")),
        WEEK_OBJ_EXPIRE(Pattern.compile("^ Only (.+) left to complete the Weekly Guild")),
        WEEK_OBJ_NEW(Pattern.compile("^ New Weekly Guild Objectives are being assigned"));

        private final Pattern infoRegex;
        private Matcher matcher;

        GuildInformation(Pattern infoRegex) {
            this.infoRegex = infoRegex;
            this.matcher = null;
        }

        private boolean find(Text text) {
            Matcher m = this.infoRegex.matcher(text.getSiblings().get(0).getString());
            if(m.find()) {
                this.matcher = m;
                return true;
            }
            return false;
        }

        private static GuildInformation findAndGet(Text text) {
            return Arrays.stream(GuildInformation.values())
                    .filter(guildInformation -> guildInformation.find(text))
                    .findFirst().orElse(null);
        }
    }
}
