package net.hexagreen.wynntrans;

import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(Pattern.compile("^\uE056\uE042")),
    DIALOG_NORMAL(Pattern.compile("^\\[[0-9]+/[0-9]+] .+:")),
    DIALOG_ITEM(Pattern.compile("^\\[[+\\-][0-9] .+]$")),
    DIALOG_PLACEHOLDER(Pattern.compile("^À+$")),
    SELECTION_OPTION(Pattern.compile("^   \\[[0-9]] .+")),
    NEW_QUEST(Pattern.compile("^New Quest Started: ")),
    SELECTION_END(Pattern.compile("^ +(Select|CLICK) an option to continue$")),
    DIALOG_END(Pattern.compile("^ +Press (SHIFT|SNEAK) to continue$")),
    INFO(Pattern.compile("^\\[Info] ")),
    INFO_EVENT(Pattern.compile("^\\[Event] ")),
    CLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching combat level ([0-9]+)!$")),
    PLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching level ([0-9]+) in (. .+)!$")),
    BLACKSMITH(Pattern.compile("^Blacksmith: ")),
    IDENTIFIER(Pattern.compile("^Item Identifier: I can't identify this item! ")),
    AREA_ENTER(Pattern.compile("^\\[You are now entering (.+)]$")),
    AREA_LEAVE(Pattern.compile("^\\[You are now leaving (.+)]$")),
    BOMB_THANK(Pattern.compile("^Want to thank (.+)\\? Click here to thank them!$")),
    THANK_YOU(Pattern.compile("^You have thanked (.+)$")),
    SHOUT(Pattern.compile("^(.+) \\[WC([0-9]+)] shouts: ")),
    CRATE_GET(Pattern.compile("^(.+) has gotten a (.+) from their crate\\. ")),
    ITEMBOMB_THROWN(Pattern.compile("^(.+) has thrown an Item Bomb!$")),
    ITEMBOMB_MESSAGE(Pattern.compile("^Everybody gets 2 Random Items! ")),
    RANKS_LOGIN(Pattern.compile("^. .+ has just logged in!$")),
    COMBAT_LEVELUP(Pattern.compile("^(.+) is now combat level ([0-9]+)$")),
    PROFESSION_LEVELUP(Pattern.compile("^(.+) is now level ([0-9]+) in (.) (.+)$")),
    SERVER_RESTART(Pattern.compile("^This world will restart in ([0-9]+) (minutes?|seconds?)\\.$")),
    RESTARTING(Pattern.compile("^The world is restarting!$")),
    DAILY_REWARD(Pattern.compile("^\\[Daily Rewards: (?:([0-9]+) emeralds)?(?: and )?(?:([0-9]+) items)?]$")),
    PRIVATE_MESSAGE(Pattern.compile("\\[(.+) \\(WC[0-9]+\\) ➤ (.+)]")),
    SKILL_COOLDOWN(Pattern.compile("^\\[⬤] .+ has been refreshed!$")),
    SPEEDBOOST(Pattern.compile("^\\+([0-9]) minutes speed boost\\.")),
    RESISTANCE(Pattern.compile("^.+ has given you 20% resistance")),
    DISGUISE(Pattern.compile("^.+ has disguised as a .+!")),

    NO_TYPE(null);


    private final Pattern regex;
    ChatType(Pattern regex) {
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

    public boolean matchFullText(Text text) {
        if(this.regex == null) return false;
        return this.regex.matcher(text.getString()).find();
    }

    public static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> chatType.matchFullText(text))
                .findAny().orElse(NO_TYPE);
    }
}
