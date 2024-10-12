package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(null, Pattern.compile("^\uE056\uE042")),
    PRIVATE_MESSAGE(null, Pattern.compile("\\[(.+) (?:\\(WC\\d+\\) )?➤ (.+)]")),
    PRIVATE_MESSAGE_SAME_SERVER(null, Pattern.compile("(.+)\uE003(.+):")),
    DIALOG_NORMAL(NpcDialog.class, Pattern.compile("^\\n?\\[(\\d+)/(\\d+)] .+:")),
    DIALOG_ITEM(ItemGiveAndTake.class, Pattern.compile("^§.\\[([+-])(\\d+%?) (.+)]$")),
    SKILL_REFRESH(SkillRefreshed.class, Pattern.compile("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$")),
    NEW_QUEST(NewQuestFocused.class, Pattern.compile("^(?:New |Mini-)?Quest(?:line)? Started: ")),
    INFO(Info.class, Pattern.compile("^\\[Info] ")),
    INFO_EVENT(InfoEvent.class, Pattern.compile("^\\[Event] ")),
    INFO_SALE(InfoSale.class, Pattern.compile("^\\[Sale] ")),
    INFO_UPDATE(InfoUpdate.class, Pattern.compile("^\\[Major Update] ")),
    CLEVEL_ANNOUNCE(CombatLevelAnnounce.class, Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching combat §flevel (\\d+)§7!$")),
    PLEVEL_ANNOUNCE(ProfessionLevelAnnounce.class, Pattern.compile("\\[§7!§8] §7Congratulations to (.+) for reaching §flevel (\\d+) in §f(.)§7 (.+)§7!$")),
    AREA_ENTER(AreaEnter.class, Pattern.compile("^§7\\[You are now entering (.+)]$")),
    AREA_LEAVE(AreaLeave.class, Pattern.compile("^§7\\[You are now leaving (.+)]$")),
    BOMB_START(BombStart.class, Pattern.compile("^.+ has thrown an? .+ Bomb")),
    BOMB_THANK(BombThanks.class, Pattern.compile("^Want to thank (.+)\\? Click here to thank them!$")),
    THANK_YOU(BombThankful.class, Pattern.compile("^§7You have thanked (.+)$")),
    BOMB_EXPIRE(BombExpired.class, Pattern.compile("^You can buy (.+) [bB]ombs at our store")),
    SHOUT_LITERAL(ShoutLiteral.class, Pattern.compile("^§5(.+) \\[WC(\\d+)] shouts: §d")),
    SHOUT(Shout.class, Pattern.compile("^(.+) \\[WC(\\d+)] shouts: ")),
    CRATE_GET(CrateGet.class, Pattern.compile("^(.+) has gotten a (.+) from their crate\\. ")),
    RANKS_LOGIN(RankJoin.class, Pattern.compile("^. .+ has just logged in!$")),
    COMBAT_LEVELUP(NearCombatLevelUp.class, Pattern.compile("^(.+) is now combat level (\\d+)$")),
    PROFESSION_LEVELUP(NearProfessionLevelUp.class, Pattern.compile("^§6(.+) is now level (\\d+) in §f(.)§6 (.+)$")),
    SERVER_RESTART(ServerRestart.class, Pattern.compile("^§cThis world will restart in (\\d+) (minutes?|seconds?)\\.$")),
    DAILY_REWARD(DailyReward.class, Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$")),
    SPEEDBOOST(SpeedBoost.class, Pattern.compile("^\\+(\\d) minutes speed boost\\.")),
    RESISTANCE(Resistance.class, Pattern.compile("^.+ has given you (\\d+%) resistance(?: and (\\d+%) strength)?")),
    PARTYFINDER(PartyFinder.class, Pattern.compile("^§5Party Finder:§d Hey (.+), over here! Join the (§..+)§d queue and match up with §e(\\d+) other players?§d!$")),
    DISGUISE(Disguise.class, Pattern.compile("^.+ has disguised as a .+!")),
    DISGUISING(Disguising.class, Pattern.compile("^§3You are (now|no longer) disguised as an? §b(.+)")),
    FRIEND_JOIN(FriendJoin.class, Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$")),
    FRIEND_LEFT(FriendLeft.class, Pattern.compile("^§a(.+) left the game\\.$")),
    DIALOG_LITERAL(NpcDialogLiteral.class, Pattern.compile("^§7\\[(\\d+)/(\\d+)] §.(.+: )(.+)")),
    WELCOME(WelcomeMessage.class, Pattern.compile("^\\n +....Welcome to Wynncraft!\\n")),
    RECRUIT(RecruitMessage.class, Pattern.compile("^\\n +§6§lEnjoying Wynncraft\\?")),
    EQUIP_STAT_REQ(EquipmentSkillRequirement.class, Pattern.compile("^§c(.+)§4 requires your (.+) skill to be at least §c(\\d+)\\.")),
    EQUIP_LEVEL_REQ(EquipmentLevelRequirement.class, Pattern.compile("^§c(.+)§4 is for combat level §c(\\d+)§4\\+ only\\.")),
    UNUSED_STAT_POINT(UnusedStatPoint.class, Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?! §4Right-Click")),
    POUCH_SOLD(PouchSold.class, Pattern.compile("^§dYou have sold (§7\\d+)§d ingredients for a total of (§a.+)§d")),
    WEEKLY_OBJECTIVE(WeeklyObjective.class, Pattern.compile("^ \n +§lObjective Finished")),
    EVENT_CRATE_ALARM(CrateAlert.class, Pattern.compile("^Use /crates to open your (.+ Crate)!$")),
    JOIN_QUEUE(JoinQueue.class, Pattern.compile("^\\n +§b§lYou are in queue to join!")),
    WORLD_JOIN_QUEUE(WorldJoinQueue.class, Pattern.compile("^\\n +§b§lYou are in world WC(\\d+)!")),
    PARTY_JOINED(PartyJoined.class, Pattern.compile("^(.+) has joined your party, say hello!$")),
    PARTY_LEAVED(PartyLeaved.class, Pattern.compile("^(.+) has left the party.$")),
    SERVER_KICK(KickFromServer.class, Pattern.compile("^Kicked from (?:(WC\\d+)|(DUMMY)): ")),
    SERVER_SWAP_SAVE(ServerSwapSave.class, Pattern.compile("^§7Saving your player data before switching to §fWC\\d+")),
    LITTLE_INFORM(LittleInform.class, Pattern.compile("^(?:§.)?\\[(?:§.)?!(?:§.)?] .+")),
    FRIEND_ADD(FriendAdd.class, Pattern.compile("^(.+) has been added to your friends!$")),
    FRIEND_ADDED(FriendAdded.class, Pattern.compile("^(.+) has added you as a friend!$")),
    SKIN_APPLIED(SkinApplied.class, Pattern.compile("^§7You have set your (weapon|helmet) skin to (.+)$")),
    CHARACTER_CLASS_CHANGE(CharacterClassChange.class, Pattern.compile("^§4Your character's class has been successfully changed to ")),
    QUICK_TRADE(QuickTrade.class, Pattern.compile("^\n(.+) would like to trade!\n")),
    CAVE_COMPLETE_LITERAL(CaveCompletedLiteral.class, Pattern.compile("^ \\n§2 +\\[Cave Completed]")),
    BOMB_BELL(BombBell.class, Pattern.compile("^\\[Bomb Bell] ")),
    NOT_YOUR_WEAPON(NotYourWeapon.class, Pattern.compile("§c(.+)§4 is not a §c(.+)§4 weapon\\. You must use a §c.+\\.")),
    MOB_TOTEM_DEPLOYED(MobTotemDeployed.class, Pattern.compile(" has placed a mob totem in ")),
    MOB_TOTEM_RUNNING(MobTotemRunning.class, Pattern.compile("You are inside of (.+)'s mob totem")),
    MOB_TOTEM_RUN_OUT(MobTotemRunOut.class, Pattern.compile("(.+)'s Mob Totem has run out")),

    CRAFTED_STAT_REQ(ItemSkillRequirement.class, Pattern.compile("This item requires your (.+) skill to be at least §c(\\d+)\\.")),
    CRAFTED_LEVEL_REQ(ItemLevelRequirement.class, Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\.")),
    FINISH_TRADING(TradeFinish.class, Pattern.compile("Finished (?:buying|selling) ")),
    PARTIAL_TRADING(TradePartial.class, Pattern.compile("(\\d+)x .+ has been (?:Bought|Sold)\\.$")),
    POWDER_MASTER(PowderMaster.class, Pattern.compile("Powder Master: ")),
    IDENTIFIER(Identifier.class, Pattern.compile("Item Identifier: ")),
    MERCHANT(Merchants.class, Pattern.compile("([\\w ]+) Merchant:")),
    BLACKSMITH(Blacksmith.class, Pattern.compile("Blacksmith: ")),
    KEY_COLLECTOR(KeyCollector.class, Pattern.compile("Key Collector:")),
    GUILD_INFO_WEEKLY_EXPIRE(GuildInfo.class, Pattern.compile("Only .+ left to complete the Weekly Guild Objectives")),
    GUILD_INFO_WEEKLY_COMPLETE(GuildInfo.class, Pattern.compile("has finished their weekly objective")),
    GUILD_INFO_WEEKLY_NEW(GuildInfo.class, Pattern.compile("New Weekly Guild Objectives")),
    GUILD_INFO_SEASON_END(GuildInfo.class, Pattern.compile("The current guild season will end in")),
    WORLD_EVENT_START(WorldEventStart.class, Pattern.compile("The (.+) World Event starts in (\\d+\\w)!")),
    TRINKET_COOLDOWN(TrinketCooldown.class, Pattern.compile("is on cooldown for ")),
    TRINKET_ON_ACTIVE(TrinketOnActive.class, Pattern.compile("is active for ")),
    PLAYER_EFFECT_APPLIED(PlayerEffectApplied.class, Pattern.compile("You now have the (.+)\\.")),
    FRIEND_LIST(FriendList.class, Pattern.compile("(.+)'s friends \\((\\d+)\\): ")),

    ANNIHILATION(ProxySystemText.class, Pattern.compile("Corruption Portal in .+!$")),
    BANK_PAGE_ADDED(ProxySystemText.class, Pattern.compile("You have unlocked page \\d+ in your bank")),

    EVENT_BONFIRE_CARRYING_FLAME(EventBonfireHearthflames.class, Pattern.compile("You are carrying \\d+ hearthflames")),
    EVENT_BONFIRE_RETURN_FLAME(EventBonfireHearthflames.class, Pattern.compile("Returning \\d+ hearthflames")),
    EVENT_BONFIRE_REMOVE_FLAME(EventBonfireHearthflames.class, Pattern.compile("Returned \\d+ hearthflames")),

    TRADE_ITEM_REVEAL(EatThisText.class, Pattern.compile("^§dYou've revealed:")),
    GO_TO_STORE(GoToStore.class, Pattern.compile("wynncraft\\.com/store")),
    STANDARD_SYSTEM_MESSAGE(SimpleSystemText.class, Pattern.compile("^\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE |^\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ")),
    NO_TYPE(SimpleText.class, null);


    private final Pattern regex;
    private final Class<? extends WynnChatText> wct;

    private static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> Objects.nonNull(chatType.regex))
                .filter(chatType -> chatType.regex.matcher(removeTextBox(text)).find())
                .findFirst().orElse(NO_TYPE);
    }

    public static boolean findAndRun(Text text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ChatType find = findType(text);
        return find.wct != null && find.wct.cast(find.wct.getConstructor(Text.class, Pattern.class).newInstance(text, find.regex)).print();
    }

    private static String removeTextBox(Text text) {
        return text.getString().replaceAll("(?<=.) ?\\n? ?\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ?", " ");
    }

    ChatType(Class<? extends WynnChatText> wct, Pattern regex) {
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
