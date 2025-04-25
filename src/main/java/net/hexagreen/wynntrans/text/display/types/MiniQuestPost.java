package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniQuestPost extends WynnDisplayText {
    private static final Pattern regex = Pattern.compile("\\[(.+) Lv\\. (\\d+)]");
    private final boolean gather;
    private final String level;
    private final Profession profession;
    private final Text npcTag;

    public static boolean typeChecker(Text text) {
        return text.getString().contains(" Post §2[");
    }

    public MiniQuestPost(Text text) {
        super(text);
        this.gather = text.getString().contains("Gathering");
        Matcher m = regex.matcher(text.getString());
        boolean ignore = m.find();
        this.level = m.group(2);
        this.profession = gather ? Profession.getProfession(m.group(1)) : null;
        this.npcTag = getSibling(0);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.post.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        if(!gather) {
            resultText.append(Text.translatable(translationKey + "slay").setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append(" ");
            resultText.append(Text.translatable(translationKey + "slay.req", level).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))).append("\n");
            resultText.append(npcTag);
        }
        else {
            resultText.append(Text.translatable(translationKey + "gather").setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append(" ");
            resultText.append(Text.translatable(translationKey + "gather.req", profession.getText(), level).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))).append("\n");
            resultText.append(npcTag);
        }
    }
}
