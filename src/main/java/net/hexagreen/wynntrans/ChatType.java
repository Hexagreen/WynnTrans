package net.hexagreen.wynntrans;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public enum ChatType {
    DIALOG_NORMAL(Pattern.compile("^\\[[0-9]+/[0-9]+] .+:")),
    DIALOG_ITEM(Pattern.compile("^\\[[+\\-][0-9] .+]$")),
    DIALOG_PLACEHOLDER(Pattern.compile("^À+$")),
    SELECTION_OPTION(Pattern.compile("^   \\[[0-9]] .+")),
    NEW_QUEST(Pattern.compile("^New Quest Started: ")),
    SELECTION_END(Pattern.compile("^ +(Select|CLICK) an option to continue$")),
    DIALOG_END(Pattern.compile("^ +Press (SHIFT|SNEAK) to continue$"));


    private final Pattern regex;
    ChatType(Pattern regex) {
        this.regex = regex;
    }

    public boolean match(Text text){
        for(Text sibling : text.getSiblings()){
            if(this.regex.matcher(sibling.getString()).find()){
                return true;
            }
        }
        return false;
    }

    public boolean match(Text text, int siblingIndex){
        return this.regex.matcher(text.getSiblings().get(siblingIndex).getString()).find();
    }
}
