package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum ChatType {
    NORMAL_CHAT(null, text -> Pattern.compile("^(\uE04E\uE040|\uE044\uE054|\uE040\uE052)").matcher(text.getString()).find()),
    PRIVATE_MESSAGE(null, text -> Pattern.compile("\\[(.+) (?:\\((?:NA|EU|AS)\\d+\\) )?➤ (.+)]").matcher(text.getString()).find()),
    PRIVATE_MESSAGE_SAME_SERVER(null, text -> Pattern.compile("(.+)\uE003(.+):").matcher(text.getString()).find()),
    DIALOG_NORMAL(NpcDialog::new, NpcDialog::typeChecker),
    DIALOG_ITEM(ItemGiveAndTake::new, ItemGiveAndTake::typeChecker),
    SKILL_REFRESH(SkillRefreshed::new, SkillRefreshed::typeChecker),
    NEW_QUEST(NewQuestFocused::new, NewQuestFocused::typeChecker),
    INFO(Info::new, Info::typeChecker),
    INFO_EVENT(InfoEvent::new, InfoEvent::typeChecker),
    INFO_SALE(InfoSale::new, InfoSale::typeChecker),
    INFO_UPDATE(InfoUpdate::new, InfoUpdate::typeChecker),
    CLEVEL_ANNOUNCE(CombatLevelAnnounce::new, CombatLevelAnnounce::typeChecker),
    PLEVEL_ANNOUNCE(ProfessionLevelAnnounce::new, ProfessionLevelAnnounce::typeChecker),
    AREA_ENTER(AreaEnter::new, AreaEnter::typeChecker),
    AREA_LEAVE(AreaLeave::new, AreaLeave::typeChecker),
    BOMB_START(BombStart::new, BombStart::typeChecker),
    BOMB_THANK(BombThanks::new, BombThanks::typeChecker),
    THANK_YOU(BombThankful::new, BombThankful::typeChecker),
    BOMB_THANKED(BombThanked::new, BombThanked::typeChecker),
    BOMB_EXPIRE(BombExpired::new, BombExpired::typeChecker),
    SHOUT_LITERAL(ShoutLiteral::new, ShoutLiteral::typeChecker),
    SHOUT(Shout::new, Shout::typeChecker),
    CRATE_GET(CrateGet::new, CrateGet::typeChecker),
    RANKS_LOGIN(RankJoin::new, RankJoin::typeChecker),
    COMBAT_LEVELUP(NearCombatLevelUp::new, NearCombatLevelUp::typeChecker),
    PROFESSION_LEVELUP(NearProfessionLevelUp::new, NearProfessionLevelUp::typeChecker),
    SERVER_RESTART(ServerRestart::new, ServerRestart::typeChecker),
    DAILY_REWARD(DailyReward::new, DailyReward::typeChecker),
    SPEEDBOOST(SpeedBoost::new, SpeedBoost::typeChecker),
    RESISTANCE(Resistance::new, Resistance::typeChecker),
    PARTYFINDER(PartyFinder::new, PartyFinder::typeChecker),
    DISGUISE(Disguise::new, Disguise::typeChecker),
    DISGUISING(Disguising::new, Disguising::typeChecker),
    FRIEND_JOIN(FriendJoin::new, FriendJoin::typeChecker),
    FRIEND_LEFT(FriendLeft::new, FriendLeft::typeChecker),
    DIALOG_LITERAL(NpcDialogLiteral::new, NpcDialogLiteral::typeChecker),
    WELCOME(WelcomeMessage::new, WelcomeMessage::typeChecker),
    RECRUIT(RecruitMessage::new, RecruitMessage::typeChecker),
    EQUIP_STAT_REQ(EquipmentSkillRequirement::new, EquipmentSkillRequirement::typeChecker),
    EQUIP_LEVEL_REQ(EquipmentLevelRequirement::new, EquipmentLevelRequirement::typeChecker),
    UNUSED_STAT_POINT(UnusedStatPoint::new, UnusedStatPoint::typeChecker),
    POUCH_SOLD(PouchSold::new, PouchSold::typeChecker),
    WEEKLY_OBJECTIVE(WeeklyObjective::new, WeeklyObjective::typeChecker),
    EVENT_CRATE_ALARM(CrateAlert::new, CrateAlert::typeChecker),
    JOIN_QUEUE(JoinQueue::new, JoinQueue::typeChecker),
    WORLD_JOIN_QUEUE(WorldJoinQueue::new, WorldJoinQueue::typeChecker),
    PARTY_JOINED(PartyJoined::new, PartyJoined::typeChecker),
    PARTY_LEAVED(PartyLeaved::new, PartyLeaved::typeChecker),
    SERVER_KICK(KickFromServer::new, KickFromServer::typeChecker),
    SERVER_SWAP_SAVE(ServerSwapSave::new, ServerSwapSave::typeChecker),
    LITTLE_INFORM(LittleInform::new, LittleInform::typeChecker),
    FRIEND_ADD(FriendAdd::new, FriendAdd::typeChecker),
    FRIEND_ADDED(FriendAdded::new, FriendAdded::typeChecker),
    SKIN_APPLIED(SkinApplied::new, SkinApplied::typeChecker),
    CHARACTER_CLASS_CHANGE(CharacterClassChange::new, CharacterClassChange::typeChecker),
    QUICK_TRADE(QuickTrade::new, QuickTrade::typeChecker),
    CAVE_COMPLETE_LITERAL(CaveCompletedLiteral::new, CaveCompletedLiteral::typeChecker),
    BOMB_BELL(BombBell::new, BombBell::typeChecker),
    NOT_YOUR_WEAPON(NotYourWeapon::new, NotYourWeapon::typeChecker),
    MOB_TOTEM_DEPLOYED(MobTotemDeployed::new, MobTotemDeployed::typeChecker),
    MOB_TOTEM_RUNNING(MobTotemRunning::new, MobTotemRunning::typeChecker),
    MOB_TOTEM_RUN_OUT(MobTotemRunOut::new, MobTotemRunOut::typeChecker),

    CRAFTED_STAT_REQ(ItemSkillRequirement::new, ItemSkillRequirement::typeChecker),
    CRAFTED_LEVEL_REQ(ItemLevelRequirement::new, ItemLevelRequirement::typeChecker),
    FINISH_TRADING(TradeFinish::new, TradeFinish::typeChecker),
    PARTIAL_TRADING(TradePartial::new, TradePartial::typeChecker),
    POWDER_MASTER(PowderMaster::new, PowderMaster::typeChecker),
    IDENTIFIER(Identifier::new, Identifier::typeChecker),
    MERCHANT(Merchants::new, Merchants::typeChecker),
    BLACKSMITH(Blacksmith::new, Blacksmith::typeChecker),
    KEY_COLLECTOR(KeyCollector::new, KeyCollector::typeChecker),
    GUILD_INFO(GuildInfo::new, GuildInfo::typeChecker),
    WORLD_EVENT_START(WorldEventStart::new, WorldEventStart::typeChecker),
    TRINKET_COOLDOWN(TrinketCooldown::new, TrinketCooldown::typeChecker),
    TRINKET_ON_ACTIVE(TrinketOnActive::new, TrinketOnActive::typeChecker),
    PLAYER_EFFECT_APPLIED(PlayerEffectApplied::new, PlayerEffectApplied::typeChecker),
    FRIEND_LIST(FriendList::new, FriendList::typeChecker),

    PROXY_SYSTEM_TEXT(ProxySystemText::new, ProxySystemText::typeChecker),
    EVENT_BONFIRE(EventBonfireHearthflames::new, EventBonfireHearthflames::typeChecker),
    TEXT_REMOVAL(EatThisText::new, EatThisText::typeChecker),

    GO_TO_STORE(GoToStore::new, GoToStore::typeChecker),
    STANDARD_SYSTEM_MESSAGE(SimpleSystemText::new, SimpleSystemText::typeChecker),
    NO_TYPE(SimpleText::new, null);


    private final Function<Text, WynnChatText> wct;
    private final Predicate<Text> typeChecker;

    private static ChatType findType(Text text) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> Objects.nonNull(chatType.typeChecker))
                .filter(chatType -> chatType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static boolean findAndRun(Text text) {
        ChatType find = findType(text);
        return find.wct != null && find.wct.apply(text).print();
    }

    ChatType(Function<Text, WynnChatText> wct, Predicate<Text> typeChecker) {
        this.wct = wct;
        this.typeChecker = typeChecker;
    }

    public boolean match(Text text, int siblingIndex) {
        return this.typeChecker.test(text.getSiblings().get(siblingIndex));
    }
}
