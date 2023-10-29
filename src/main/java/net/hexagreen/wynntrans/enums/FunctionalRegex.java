package net.hexagreen.wynntrans.enums;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public enum FunctionalRegex {
    DIALOG_PLACEHOLDER(Pattern.compile("^À+$")),
    SELECTION_OPTION(Pattern.compile("^   \\[[0-9]] .+")),
    SELECTION_END(Pattern.compile("^ +(Select|CLICK) an option to continue$")),
    DIALOG_END(Pattern.compile("^ +Press (SHIFT|SNEAK) to continue$")),
    QUEST_COMPLETE(Pattern.compile("^ +\\[Quest Completed]"));

    private final Pattern regex;

    FunctionalRegex(Pattern regex) {
        this.regex = regex;
    }

    public Pattern getRegex() {
        return this.regex;
    }

    public boolean match(Text text){
        for(Text sibling : text.getSiblings()) {
            if(this.regex.matcher(sibling.getString()).find()){
                return true;
            }
        }
        return false;
    }

    public boolean match(Text text, int siblingIndex) {
        return this.regex.matcher(text.getSiblings().get(siblingIndex).getString()).find();
    }
}
