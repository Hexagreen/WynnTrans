package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(Pattern.compile("^\uE056\uE042"), null),
    PRIVATE_MESSAGE(Pattern.compile("\\[(.+) (?:\\(WC\\d+\\) )?➤ (.+)]"), null),
    DIALOG_NORMAL(Pattern.compile("^\\n?\\[(\\d+)/(\\d+)] .+:"), NpcDialog.class),
    DIALOG_ITEM(Pattern.compile("^§.\\[([+-])(\\d+%?) (.+)]$"), ItemGiveAndTake.class),
    SKILL_REFRESH(Pattern.compile("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$"), SkillRefreshed.class),
    NEW_QUEST(Pattern.compile("^(?:New |Mini-)?Quest(?:line)? Started: "), NewQuestFocused.class),
    INFO(Pattern.compile("^\\[Info] "), Info.class),
    INFO_EVENT(Pattern.compile("^\\[Event] "), InfoEvent.class),
    INFO_SALE(Pattern.compile("^\\[Sale] "), InfoSale.class),
    INFO_UPDATE(Pattern.compile("^\\[Major Update] "), InfoUpdate.class),
    CLEVEL_ANNOUNCE(Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching combat §flevel (\\d+)§7!$"), CombatLevelAnnounce.class),
    PLEVEL_ANNOUNCE(Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching §flevel (\\d+) in §f(.)§7 (.+)§7!$"), ProfessionLevelAnnounce.class),
    AREA_ENTER(Pattern.compile("^§7\\[You are now entering (.+)]$"), AreaEnter.class),
    AREA_LEAVE(Pattern.compile("^§7\\[You are now leaving (.+)]$"), AreaLeave.class),
    BOMB_START(Pattern.compile("^.+ has thrown an? .+ Bomb"), BombStart.class),
    BOMB_THANK(Pattern.compile("^Want to thank (.+)\\? Click here to thank them!$"), BombThanks.class),
    THANK_YOU(Pattern.compile("^§7You have thanked (.+)$"), BombThankful.class),
    BOMB_EXPIRE(Pattern.compile("^You can buy (.+) [bB]ombs at our store"), BombExpired.class),
    SHOUT_LITERAL(Pattern.compile("^§5(.+) \\[WC(\\d+)] shouts: §d"), ShoutLiteral.class),
    SHOUT(Pattern.compile("^(.+) \\[WC(\\d+)] shouts: "), Shout.class),
    CRATE_GET(Pattern.compile("^(.+) has gotten a (.+) from their crate\\. "), CrateGet.class),
    RANKS_LOGIN(Pattern.compile("^. .+ has just logged in!$"), RankJoin.class),
    COMBAT_LEVELUP(Pattern.compile("^(.+) is now combat level (\\d+)$"), NearCombatLevelUp.class),
    PROFESSION_LEVELUP(Pattern.compile("^§6(.+) is now level (\\d+) in §f(.)§6 (.+)$"), NearProfessionLevelUp.class),
    SERVER_RESTART(Pattern.compile("^§cThis world will restart in (\\d+) (minutes?|seconds?)\\.$"), ServerRestart.class),
    DAILY_REWARD(Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$"), DailyReward.class),
    SPEEDBOOST(Pattern.compile("^\\+(\\d) minutes speed boost\\."), SpeedBoost.class),
    RESISTANCE(Pattern.compile("^.+ has given you (\\d+%) resistance(?: and (\\d+%) strength)?"), Resistance.class),
    PARTYFINDER(Pattern.compile("^§5Party Finder:§d Hey (.+), over here! Join the (§..+)§d queue and match up with §e(\\d+) other players?§d!$"), PartyFinder.class),
    DISGUISE(Pattern.compile("^.+ has disguised as a .+!"), Disguise.class),
    DISGUISING(Pattern.compile("^§3You are (now|no longer) disguised as an? §b(.+)"), Disguising.class),
    FRIEND_JOIN(Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$"), FriendJoin.class),
    FRIEND_LEFT(Pattern.compile("^§a(.+) left the game\\.$"), FriendLeft.class),
    DIALOG_LITERAL(Pattern.compile("^§7\\[(\\d+)/(\\d+)] §.(.+: )(.+)"), NpcDialogLiteral.class),
    WELCOME(Pattern.compile("^\\n +....Welcome to Wynncraft!\\n"), WelcomeMessage.class),
    RECRUIT(Pattern.compile("^\\n +§6§lEnjoying Wynncraft\\?"), RecruitMessage.class),
    EQUIP_STAT_REQ(Pattern.compile("^§c(.+)§4 requires your (.+) skill to be at least §c(\\d+)\\."), EquipmentSkillRequirement.class),
    EQUIP_LEVEL_REQ(Pattern.compile("^§c(.+)§4 is for combat level §c(\\d+)§4\\+ only\\."), EquipmentLevelRequirement.class),
    UNUSED_STAT_POINT(Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?! §4Right-Click"), UnusedStatPoint.class),
    POUCH_SOLD(Pattern.compile("^§dYou have sold (§7\\d+)§d ingredients for a total of (§a.+)§d"), PouchSold.class),
    WEEKLY_OBJECTIVE(Pattern.compile("^ \n +§lObjective Finished"), WeeklyObjective.class),
    EVENT_CRATE_ALARM(Pattern.compile("^Use /crates to open your (.+ Crate)!$"), CrateAlert.class),
    JOIN_QUEUE(Pattern.compile("^\\n +§b§lYou are in queue to join!"), JoinQueue.class),
    WORLD_JOIN_QUEUE(Pattern.compile("^\\n +§b§lYou are in world WC(\\d+)!"), WorldJoinQueue.class),
    PARTY_JOINED(Pattern.compile("^(.+) has joined your party, say hello!$"), PartyJoined.class),
    PARTY_LEAVED(Pattern.compile("^(.+) has left the party.$"), PartyLeaved.class),
    SERVER_KICK(Pattern.compile("^Kicked from (?:(WC\\d+)|(DUMMY)): "), KickFromServer.class),
    SERVER_SWAP_SAVE(Pattern.compile("^§7Saving your player data before switching to §fWC\\d+"), ServerSwapSave.class),
    LITTLE_INFORM(Pattern.compile("^(?:§.)?\\[(?:§.)?!(?:§.)?] .+"), LittleInform.class),
    FRIEND_ADD(Pattern.compile("^(.+) has been added to your friends!$"), FriendAdd.class),
    FRIEND_ADDED(Pattern.compile("^(.+) has added you as a friend!$"), FriendAdded.class),
    SKIN_APPLIED(Pattern.compile("^§7You have set your (weapon|helmet) skin to (.+)$"), SkinApplied.class),
    CHARACTER_CLASS_CHANGE(Pattern.compile("^§4Your character's class has been successfully changed to "), CharacterClassChange.class),
    QUICK_TRADE(Pattern.compile("^\n(.+) would like to trade!\n"), QuickTrade.class),
    CAVE_COMPLETE_LITERAL(Pattern.compile("^ \\n§2 +\\[Cave Completed]"), CaveCompletedLiteral.class),
    BOMB_BELL(Pattern.compile("^\\[Bomb Bell] "), BombBell.class),
    NOT_YOUR_WEAPON(Pattern.compile("§c(.+)§4 is not a §c(.+)§4 weapon\\. You must use a §c.+\\."), NotYourWeapon.class),
    MOB_TOTEM_DEPLOYED(Pattern.compile(" has placed a mob totem in "), MobTotemDeployed.class),
    MOB_TOTEM_RUNNING(Pattern.compile("You are inside of (.+)'s mob totem"), MobTotemRunning.class),
    MOB_TOTEM_RUN_OUT(Pattern.compile("(.+)'s Mob Totem has run out"), MobTotemRunOut.class),

    CRAFTED_STAT_REQ(Pattern.compile("This item requires your (.+) skill to be at least §c(\\d+)\\."), ItemSkillRequirement.class),
    CRAFTED_LEVEL_REQ(Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\."), ItemLevelRequirement.class),
    FINISH_TRADING(Pattern.compile("Finished (?:buying|selling) "), TradeFinish.class),
    PARTIAL_TRADING(Pattern.compile("(\\d+)x .+ has been (?:Bought|Sold)\\.$"), TradePartial.class),
    POWDER_MASTER(Pattern.compile("Powder Master: "), PowderMaster.class),
    IDENTIFIER(Pattern.compile("Item Identifier: "), Identifier.class),
    MERCHANT(Pattern.compile("([\\w ]+) Merchant:"), Merchants.class),
    BLACKSMITH(Pattern.compile("Blacksmith: "), Blacksmith.class),
    KEY_COLLECTOR(Pattern.compile("Key Collector:"), KeyCollector.class),
    GUILD_INFO_WEEKLY_EXPIRE(Pattern.compile("Only .+ left to complete the Weekly Guild Objectives"), GuildInfo.class),
    GUILD_INFO_WEEKLY_COMPLETE(Pattern.compile("has finished their weekly objective"), GuildInfo.class),
    GUILD_INFO_WEEKLY_NEW(Pattern.compile("New Weekly Guild Objectives"), GuildInfo.class),
    GUILD_INFO_SEASON_END(Pattern.compile("The current guild season will end in"), GuildInfo.class),
    WORLD_EVENT_START(Pattern.compile("The (.+) World Event starts in (\\d+\\w)!"), WorldEventStart.class),
    TRINKET_COOLDOWN(Pattern.compile("is on cooldown for "), TrinketCooldown.class),
    TRINKET_ON_ACTIVE(Pattern.compile("is active for "), TrinketOnActive.class),
    PLAYER_EFFECT_APPLIED(Pattern.compile("You now have the (.+)\\."), PlayerEffectApplied.class),
    FRIEND_LIST(Pattern.compile("(.+)'s friends \\((\\d+)\\): "), FriendList.class),

    ANNIHILATION(Pattern.compile("Corruption Portal in .+!$"), ProxySystemText.class),
    BANK_PAGE_ADDED(Pattern.compile("You have unlocked page \\d+ in your bank"), ProxySystemText.class),

    EVENT_BONFIRE_CARRYING_FLAME(Pattern.compile("You are carrying \\d+ hearthflames"), EventBonfireHearthflames.class),
    EVENT_BONFIRE_RETURN_FLAME(Pattern.compile("Returning \\d+ hearthflames"), EventBonfireHearthflames.class),
    EVENT_BONFIRE_REMOVE_FLAME(Pattern.compile("Returned \\d+ hearthflames"), EventBonfireHearthflames.class),

    TRADE_ITEM_REVEAL(Pattern.compile("^§dYou've revealed:"), EatThisText.class),
    GO_TO_STORE(Pattern.compile("wynncraft\\.com/store"), GoToStore.class),
    STANDARD_SYSTEM_MESSAGE(Pattern.compile("^\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE |^\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 "), SimpleSystemText.class),
    NO_TYPE(null, SimpleText.class);


    private final Pattern regex;
    private final Class<? extends WynnChatText> wct;

    private static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values()).filter(chatType -> Objects.nonNull(chatType.regex)).filter(chatType -> chatType.regex.matcher(removeTextBox(text)).find()).findFirst().orElse(NO_TYPE);
    }

    public static boolean findAndRun(Text text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ChatType find = findType(text);
        return find.wct != null && find.wct.cast(find.wct.getConstructor(Text.class, Pattern.class).newInstance(text, find.regex)).print();
    }

    private static String removeTextBox(Text text) {
        return text.getString().replaceAll("(?<=.) ?\\n? ?\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ?", " ");
    }

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
}
