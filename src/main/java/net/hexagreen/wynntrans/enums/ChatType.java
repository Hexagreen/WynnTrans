package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.chat.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(Pattern.compile("^\uE056\uE042"), null),
    PRIVATE_MESSAGE(Pattern.compile("\\[(.+) \\(WC[0-9]+\\) ➤ (.+)]"), null),
    DIALOG_NORMAL(Pattern.compile("^\\n?\\[([0-9]+)/([0-9]+)] .+:"), NpcDialog.class),
    DIALOG_ITEM(Pattern.compile("^\\[([+-])([0-9]+) (.+)]$"), ItemGiveAndTake.class),
    SKILL_COOLDOWN(Pattern.compile("^\\[⬤] .+ has been refreshed!$"), SkillCooldown.class),
    NEW_QUEST(Pattern.compile("^New Quest Started: "), NewQuest.class),
    INFO(Pattern.compile("^\\[Info] "), Info.class),
    INFO_EVENT(Pattern.compile("^\\[Event] "), EventInfo.class),
    CLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching combat level ([0-9]+)!$"), CombatLevelAnnounce.class),
    PLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching level ([0-9]+) in (. .+)!$"), ProfessionLevelAnnounce.class),
    BLACKSMITH(Pattern.compile("^Blacksmith: "), Blacksmith.class),
    IDENTIFIER(Pattern.compile("^Item Identifier: I can't identify this item! "), Identifier.class),
    AREA_ENTER(Pattern.compile("^\\[You are now entering (.+)]$"), AreaEnter.class),
    AREA_LEAVE(Pattern.compile("^\\[You are now leaving (.+)]$"), AreaLeave.class),
    BOMB_THANK(Pattern.compile("^Want to thank (.+)\\? Click here to thank them!$"), BombThanks.class),
    THANK_YOU(Pattern.compile("^You have thanked (.+)$"), BombThankyou.class),
    SHOUT(Pattern.compile("^(.+) \\[WC([0-9]+)] shouts: "), Shout.class),
    CRATE_GET(Pattern.compile("^(.+) has gotten a (.+) from their crate\\. "), CrateGet.class),
    RANKS_LOGIN(Pattern.compile("^. .+ has just logged in!$"), RankJoin.class),
    COMBAT_LEVELUP(Pattern.compile("^(.+) is now combat level ([0-9]+)$"), CombatLevelUp.class),
    PROFESSION_LEVELUP(Pattern.compile("^(.+) is now level ([0-9]+) in (.) (.+)$"), ProfessionLevelUp.class),
    SERVER_RESTART(Pattern.compile("^This world will restart in ([0-9]+) (minutes?|seconds?)\\.$"), ServerRestart.class),
    RESTARTING(Pattern.compile("^The world is restarting!$"), ServerRestarting.class),
    DAILY_REWARD(Pattern.compile("^\\[Daily Rewards: (?:([0-9]+) emeralds)?(?: and )?(?:([0-9]+) items)?]$"), DailyReward.class),
    SPEEDBOOST(Pattern.compile("^\\+([0-9]) minutes speed boost\\."), SpeedBoost.class),
    RESISTANCE(Pattern.compile("^.+ has given you 20% resistance"), Resistance.class),
    PARTYFINDER(Pattern.compile("^Party Finder: Hey (.+), over here! Join the .+ queue and match up with ([0-9]+) other players?!$"), PartyFinder.class),
    MERCHANT(Pattern.compile("^(.+) Merchant: "), Merchants.class),
    DISGUISE(Pattern.compile("^.+ has disguised as a .+!"), Disguise.class),
    FRIEND_JOIN(Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$"), FriendJoin.class),
    DIALOG_LITERAL(Pattern.compile("^§7\\[([0-9]+)/([0-9]+)] §.(.+: )(.+)"), NpcDialogLiteral.class),
    WELCOME(Pattern.compile("^\\n +....Welcome to Wynncraft!\\n"), WelcomeMessage.class),

    NO_TYPE(null, SimpleText.class);


    private final Pattern regex;
    private final Class<? extends WynnChatText> wct;

    ChatType(Pattern regex, Class<? extends WynnChatText> wct) {
        this.regex = regex;
        this.wct = wct;
    }

    public Pattern getRegex() {
        return this.regex;
    }

    public boolean match(Text text, int siblingIndex) {
        return this.regex.matcher(text.getSiblings().get(siblingIndex).getString()).find();
    }

    public boolean matchFullText(Text text) {
        if(this == NO_TYPE) return false;
        return this.regex.matcher(text.getString()).find();
    }

    public static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> chatType.matchFullText(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static boolean findAndRun(Text text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ChatType find = findType(text);
        return find.wct != null && find.wct.cast(find.wct.getMethod("of", Text.class, Pattern.class).invoke(null, text, find.regex)).print();
    }

    public boolean run(Text text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return this.wct.cast(this.wct.getMethod("of", Text.class, Pattern.class).invoke(null, text, this.regex)).print();
    }
}