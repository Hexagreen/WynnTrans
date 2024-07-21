package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.chat.types.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(Pattern.compile("^\uE056\uE042"), null),
    PRIVATE_MESSAGE(Pattern.compile("\\[(.+) (?:\\(WC[0-9]+\\) )?➤ (.+)]"), null),
    DIALOG_NORMAL(Pattern.compile("^\\n?\\[([0-9]+)/([0-9]+)] .+:"), NpcDialog.class),
    DIALOG_ITEM(Pattern.compile("^\\[([+-])([0-9]+) (.+)]$"), ItemGiveAndTake.class),
    SKILL_REFRESH(Pattern.compile("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$"), SkillRefreshed.class),
    NEW_QUEST(Pattern.compile("^(?:New |Mini-)?Quest(?:line)? Started: "), NewQuestFocused.class),
    INFO(Pattern.compile("^\\[Info] "), Info.class),
    INFO_EVENT(Pattern.compile("^\\[Event] "), InfoEvent.class),
    INFO_SALE(Pattern.compile("^\\[Sale] "), InfoSale.class),
    INFO_UPDATE(Pattern.compile("^\\[Major Update] "), InfoUpdate.class),
    CLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching combat level ([0-9]+)!$"), CombatLevelAnnounce.class),
    PLEVEL_ANNOUNCE(Pattern.compile("^\\[!] Congratulations to (.+) for reaching level ([0-9]+) in (.) (.+)!$"), ProfessionLevelAnnounce.class),
    BLACKSMITH(Pattern.compile("^Blacksmith: "), Blacksmith.class),
    IDENTIFIER(Pattern.compile("^Item Identifier: "), Identifier.class),
    AREA_ENTER(Pattern.compile("^§7\\[You are now entering (.+)]$"), AreaEnter.class),
    AREA_LEAVE(Pattern.compile("^§7\\[You are now leaving (.+)]$"), AreaLeave.class),
    BOMB_THANK(Pattern.compile("^Want to thank (.+)\\? Click here to thank them!$"), BombThanks.class),
    THANK_YOU(Pattern.compile("^You have thanked (.+)$"), BombThankyou.class),
    SHOUT(Pattern.compile("^§5(.+) \\[WC([0-9]+)] shouts: §d"), Shout.class),
    CRATE_GET(Pattern.compile("^(.+) has gotten a (.+) from their crate\\. "), CrateGet.class),
    RANKS_LOGIN(Pattern.compile("^. .+ has just logged in!$"), RankJoin.class),
    COMBAT_LEVELUP(Pattern.compile("^(.+) is now combat level ([0-9]+)$"), NearCombatLevelUp.class),
    PROFESSION_LEVELUP(Pattern.compile("^(.+) is now level ([0-9]+) in (.) (.+)$"), NearProfessionLevelUp.class),
    SERVER_RESTART(Pattern.compile("^This world will restart in ([0-9]+) (minutes?|seconds?)\\.$"), ServerRestart.class),
    RESTARTING(Pattern.compile("^The world is restarting!$"), ServerRestarting.class),
    DAILY_REWARD(Pattern.compile("^§7\\[Daily Rewards: (?:§a([0-9]+) emeralds§7)?(?: and )?(?:§b([0-9]+) items§7)?]$"), DailyReward.class),
    SPEEDBOOST(Pattern.compile("^\\+([0-9]) minutes speed boost\\."), SpeedBoost.class),
    RESISTANCE(Pattern.compile("^.+ has given you 20% resistance"), Resistance.class),
    PARTYFINDER(Pattern.compile("^§5Party Finder:§d Hey (.+), over here! Join the (§..+)§d queue and match up with §e([0-9]+) other players?§d!$"), PartyFinder.class),
    MERCHANT(Pattern.compile("^(.+) Merchant: "), Merchants.class),
    DISGUISE(Pattern.compile("^.+ has disguised as a .+!"), Disguise.class),
    DISGUISING(Pattern.compile("^You are (now|no longer) disguised as a"), Disguising.class),
    FRIEND_JOIN(Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$"), FriendJoin.class),
    FRIEND_LEFT(Pattern.compile("^(.+) left the game\\.$"), FriendLeft.class),
    DIALOG_LITERAL(Pattern.compile("^§7\\[([0-9]+)/([0-9]+)] §.(.+: )(.+)"), NpcDialogLiteral.class),
    WELCOME(Pattern.compile("^\\n +....Welcome to Wynncraft!\\n"), WelcomeMessage.class),
    RECRUIT(Pattern.compile("^\\n +§6§lEnjoying Wynncraft\\?"), RecruitMessage.class),
    EQUIP_STAT_REQ(Pattern.compile("requires your (.+) skill to be at least"), EquipmentSkillRequirement.class),
    EQUIP_LEVEL_REQ(Pattern.compile("is for combat level "), EquipmentLevelRequirement.class),
    UNUSED_STAT_POINT(Pattern.compile("^§4You have §c§l(?:(\\d+) unused Skill Points§4?)?(?: and )?§b§l(?:(\\d+) unused Ability Points?)?! §4Right-Click"), UnusedStatPoint.class),
    POUCH_SOLD(Pattern.compile("^You have sold \\d+ ingredients"), PouchSold.class),
    SKIP_TUTORIAL(Pattern.compile("^You may skip the tutorial"), SkipTutorial.class),
    WEEKLY_OBJECTIVE(Pattern.compile("^ \n +§lObjective Finished"), WeeklyObjective.class),
    GUILD_INFO(Pattern.compile("§3\\[INFO§3]"), GuildInfo.class),
    EVENT_CRATE_ALARM(Pattern.compile("^Use /crates to open your (.+ Crate)!$"), CrateAlert.class),
    WORLD_JOIN_QUEUE(Pattern.compile("^\\n +§b§lYou are in world WC(\\d+)!"), WorldJoinQueue.class),
    OFFLINE_SOULPOINT(Pattern.compile("^\\[You have received (\\d+) soul points while you were offline]$"), OfflineSoulpoint.class),
    PARTY_JOINED(Pattern.compile("^(.+) has joined your party, say hello!$"), PartyJoined.class),
    PARTY_LEAVED(Pattern.compile("^(.+) has left the party.$"), PartyLeaved.class),
    KEY_COLLECTOR(Pattern.compile("^Key Collector: "), KeyCollector.class),
    SERVER_KICK(Pattern.compile("^Kicked from WC(\\d+): "), KickFromServer.class),
    SERVER_SWAP_SAVE(Pattern.compile("^Saving your player data before switching to WC\\d+"), ServerSwapSave.class),
    FRIEND_LIST(Pattern.compile("(.+)'s friends \\((\\d+)\\): "), FriendList.class),
    FINISH_TRADING(Pattern.compile("^Finished (?:buying|selling) "), TradeFinish.class),
    PARTIAL_TRADING(Pattern.compile("^(\\d+)x .+ has been (?:purchased|sold)\\.$"), TradePartial.class),
    LITTLE_INFORM(Pattern.compile("^\\[!] .+"), LittleInform.class),
    BOMB_EXPIRE(Pattern.compile("^.+'s bomb has expired\\. You can buy (.+) [bB]ombs at"), BombExpired.class),
    FRIEND_ADD(Pattern.compile("^(.+) has been added to your friends!$"), FriendAdd.class),
    FRIEND_ADDED(Pattern.compile("^(.+) has added you as a friend!$"), FriendAdded.class),
    PLAYER_EFFECT_APPLIED(Pattern.compile("^You now have the (.+)\\."), PlayerEffectApplied.class),
    SKIN_APPLIED(Pattern.compile("^You have set your (weapon|helmet) skin to (.+)$"), SkinApplied.class),
    CHARACTER_CLASS_CHANGE(Pattern.compile("^Your character's class has been successfully changed to "), CharacterClassChange.class),
    POWDER_MASTER(Pattern.compile("^Powder Master: "), PowderMaster.class),
    QUICK_TRADE(Pattern.compile("^\n(.+) would like to trade!\n"), QuickTrade.class),
    CAVE_COMPLETE_LITERAL(Pattern.compile("^ \\n§2 +\\[Cave Completed]"), CaveCompletedLiteral.class),


    GO_TO_STORE(Pattern.compile("wynncraft\\.com/store"), GoToStore.class),
    ITEM_BOMB_IGNORE(Pattern.compile("^Everybody gets 2 Random Items!"), EatThisText.class),
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

    public static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> Objects.nonNull(chatType.regex))
                .filter(chatType -> chatType.regex.matcher(text.getString()).find())
                .findFirst().orElse(NO_TYPE);
    }

    public static boolean findAndRun(Text text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ChatType find = findType(text);
        return find.wct != null && find.wct.cast(find.wct.getConstructor(Text.class, Pattern.class).newInstance(text, find.regex)).print();
    }
}
