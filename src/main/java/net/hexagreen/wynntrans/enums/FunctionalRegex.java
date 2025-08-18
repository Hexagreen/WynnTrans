package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.text.WynnTransText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public enum FunctionalRegex {
    DIALOG_PLACEHOLDER(Pattern.compile("^À+$")),
    SELECTION_OPTION(Pattern.compile("^ {3}\\[[0-9]] .+")),
    SELECTION_END(Pattern.compile("^ +(?:Select|CLICK) an option to continue$")),
    DIALOG_END(Pattern.compile("^ +Press (?:SHIFT|SNEAK) to continue$")),
    QUEST_COMPLETE(Pattern.compile("^ +\\[(?:Mini-)?Quest Completed]")),
    DIALOG_ALERT(Pattern.compile("^\\[!] .+$")),
    MINI_QUEST_DESC(Pattern.compile("^Bring \\[.+] to the (?:Slaying|Gathering) Post|^You need to be .+ Lv\\. \\d+ to start this Mini-Quest")),
    LITERAL_NPC_DIALOG(Pattern.compile("^§.\\[\\d+/\\d+] §..+: §..+"));

    private final Pattern regex;

    FunctionalRegex(Pattern regex) {
        this.regex = regex;
    }

    public Pattern getRegex() {
        return this.regex;
    }

    public boolean match(Text text) {
        for(Text sibling : text.getSiblings()) {
            if(this.regex.matcher(sibling.getString()).find()) {
                return true;
            }
        }
        return false;
    }

    public boolean match(Text text, int siblingIndex) {
        try {
            Text reformed = WynnTransText.flatTextToFilled(WynnTransText.colorCodedToStyled(text.getSiblings().get(siblingIndex)));
            return this.regex.matcher(reformed.getString()).find();
        }
        catch(IndexOutOfBoundsException ignore) {
            return false;
        }
    }
}
