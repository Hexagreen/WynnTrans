package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildInfo extends WynnSystemText {
    private final GuildInformation infoType;

    public static boolean typeChecker(Text text) {
        String input = removeTextBox(text);
        return Pattern.compile("Only .+ left to complete the Weekly Guild Objectives").matcher(input).find()
                || Pattern.compile("has finished their weekly objective").matcher(input).find()
                || Pattern.compile("New Weekly Guild Objectives").matcher(input).find()
                || Pattern.compile("The current guild season will end in").matcher(input).find();
    }

    public GuildInfo(Text text) {
        super(text);
        this.infoType = GuildInformation.findAndGet(inputText);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.guildInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.AQUA));

        switch(infoType) {
            case WEEK_OBJ_COMPLETE ->
                    resultText.append(Text.translatable(translationKey + ".weekObj.complete", infoType.matcher.group(1)));
            case WEEK_OBJ_EXPIRE -> {
                Text time = ITime.translateTime(infoType.matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
                resultText.append(Text.translatable(translationKey + ".weekObj.expire", time));
            }
            case WEEK_OBJ_NEW -> resultText.append(Text.translatable(translationKey + ".weekObj.new"));
            case SEASON_END -> {
                Text time = Text.literal(getContentString(1).replaceAll("\n", "")).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
                resultText.append(Text.translatable(translationKey + ".season.end", time));
            }
            default -> {
                debugClass.writeTextAsJSON(inputText, "UnknownGuildInfo");
                throw new TextTranslationFailException("GuildInfo.class");
            }
        }
    }

    private enum GuildInformation {
        WEEK_OBJ_COMPLETE(Pattern.compile("(.+) has finished their weekly objective")),
        WEEK_OBJ_EXPIRE(Pattern.compile("Only (.+)left to complete the Weekly Guild Objectives!")),
        WEEK_OBJ_NEW(Pattern.compile("New Weekly Guild Objectives are being assigned")),
        SEASON_END(Pattern.compile("The current guild season will end in"));
        private final Pattern infoRegex;
        private Matcher matcher;

        private static GuildInformation findAndGet(Text text) {
            return Arrays.stream(GuildInformation.values())
                    .filter(guildInformation -> guildInformation.find(text))
                    .findFirst()
                    .orElse(null);
        }

        GuildInformation(Pattern infoRegex) {
            this.infoRegex = infoRegex;
            this.matcher = null;
        }

        private boolean find(Text text) {
            Matcher m = this.infoRegex.matcher(text.getString().replaceAll("\\n", ""));
            if(m.find()) {
                this.matcher = m;
                return true;
            }
            return false;
        }
    }
}
