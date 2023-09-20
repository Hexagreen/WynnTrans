package net.hexagreen.wynntrans;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("DataFlowIssue")
public class TranslationBuilder {
    private static final String rootKey = "wytr.";
    private static final String dirFuncional = "func.";
    private static final String dirNpcName = "name.";
    private static final String dirDialog = "dialog.";
    private static final String dirNarration = "narration.";
    private static final String dirSelection = "selOpt.";
    private static final String dirQuest = "quest.";
    private static final String dirNormal = "normalText.";
    private static final Pattern dialogCounter = Pattern.compile("^\\[([0-9])+/([0-9])+] $");

    private final MinecraftClient client;

    public TranslationBuilder() {
        this.client = MinecraftClient.getInstance();
    }

    /**
     * Build a new NPC-Dialog-Form text contains {@link net.minecraft.text.TranslatableTextContent} from parameter.
     * <p><div class="fabric"><table border=1>
     * <caption>NPC Dialog Example</caption>
     * <tr>
     *  <th><b>Fields</b></th>&emsp;<th><b>Contains</b></th>
     * </tr>
     * <tr>
     *  <td>context</td>&emsp;<td><U>[1/2] </U></td>
     * </tr>
     * <tr>
     *  <td>siblings[0]</td>&emsp;<td><U>Tasim: </U></td>
     * </tr>
     * <tr>
     *  <td>siblings[n]</td>&emsp;<td><U>Hello, Bro.</U></td>
     * </tr>
     * </table></div></p>
     * @param text Original text object
     * @return Edited text that contains translation key
     */
    public WynnTransText buildNPCDialogTranslation(Text text) {
        String playername = client.player.getName().getString();
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = dialogCounter.matcher(((LiteralTextContent) text.getContent()).string());
        if(!m.find()) return wText;
        String dialogIdx = m.group(1) + ".";
        String dialogLen = m.group(2) + ".";
        String valName = getContentLiteral(text, 0).replace(": ","");
        String npcName = valName.replace(" ", "").replace(".", "");
        String keyName = rootKey + dirNpcName + npcName;
        String keyDialogBase = rootKey + dirDialog + npcName + "." + dialogLen + dialogIdx;
        String tDialog = text.getString().replace(playername, "%1$s");
        String hash = DigestUtils.sha1Hex(tDialog).substring(0, 8);
        WynnTransText colon = WynnTransText.of(Text.literal(": ").setStyle(text.getSiblings().get(0).getStyle()));


        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyName, valName)) {
            wText.getSiblingByIndex(0).setTranslateContent(keyName);
            wText.getSiblingByIndex(0).addSibling(colon);
        }

        if(text.getSiblings().size() == 2){
            String keyDialog = keyDialogBase + hash;
            String valueDialog = getContentLiteral(text, 1).replace(playername, "%1$s");
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyDialog, valueDialog)) {
                if(getContentLiteral(text, 1).contains(playername)) {
                    wText.getSiblingByIndex(1).setTranslateContent(keyDialog, playername);
                }
                else {
                    wText.getSiblingByIndex(1).setTranslateContent(keyDialog);
                }
            }
        }
        else {
            for(int index = 1; text.getSiblings().size() > index; index++) {
                String keyDialog = keyDialogBase + hash + "_" + index;
                String valueDialog = getContentLiteral(text, index).replace(playername, "%1$s");
                if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyDialog, valueDialog)) {
                    if(getContentLiteral(text, index).contains(playername)) {
                        wText.getSiblingByIndex(index).setTranslateContent(keyDialog, playername);
                    }
                    else {
                        wText.getSiblingByIndex(index).setTranslateContent(keyDialog);
                    }
                }
            }
        }

        return wText;
    }

    /**
     * Build new selection dialog from glued dialog.
     * @param text Fully concatenated selection dialog text
     * @return Edited formed text that contains translation keys
     */
    public WynnTransText buildNPCSelectionTranslation(Text text) {
        String playername = client.player.getName().getString();
        WynnTransText wText = WynnTransText.of(text);
        int firstSelectionIdx = 6;
        int tooltipIndex = 10;

        for(int i = text.getSiblings().size() - 1; i > 0; i--) {
            if(ChatType.SELECTION_END.match(text, i)) {
                tooltipIndex = i;
                break;
            }
        }

        int lastSelectionIdx = tooltipIndex - 4;

        Text parantText = text.getSiblings().get(2);
        Matcher m = dialogCounter.matcher(((LiteralTextContent) parantText.getContent()).string());
        if(!m.find()) return wText;
        String dialogIdx = m.group(1) + ".";
        String dialogLen = m.group(2) + ".";
        String valName = getContentLiteral(text, 0).replace(": ","");
        String npcName = valName.replace(" ", "").replace(".", "");
        String keyDialogBase = rootKey + dirDialog + npcName + "." + dialogLen + dialogIdx;
        String parantDialog = parantText.getString().replace(playername, "%1$s");
        String parantHash = DigestUtils.sha1Hex(parantDialog).substring(0, 8);
        String keySelOptBase = keyDialogBase + parantHash + "." + dirSelection;

        wText.setSiblingByIndex(WynnTrans.translationBuilder.buildNPCDialogTranslation(text.getSiblings().get(2)), 2);
        wText.setSiblingByIndex(WynnTrans.translationBuilder.buildSelectOptionContinue(text.getSiblings().get(tooltipIndex)), tooltipIndex);

        for(int i = firstSelectionIdx; i <= lastSelectionIdx; i = i + 2) {
            Text origin = text.getSiblings().get(i).getSiblings().get(2);
            wText.getSiblingByIndex(i).setSiblingByIndex(buildNPCSelectionSibling(origin, keySelOptBase), 2);
        }

        return wText;
    }

    private WynnTransText buildNPCSelectionSibling(Text text, String keyBase) {
        WynnTransText wText = WynnTransText.of(text);
        String hash = DigestUtils.sha1Hex(text.getString()).substring(0, 4);
        String keyOption = keyBase + hash;
        String valOption = ((LiteralTextContent) text.getContent()).string();

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyOption, valOption)) {
            wText.setTranslateContent(keyOption);
        }

        return wText;
    }

    public WynnTransText buildNarrationTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyNarrationBase = rootKey + dirNarration;
        String tNarration = text.getString();
        String hash = DigestUtils.sha1Hex(tNarration);

        if(text.getSiblings().size() == 0){
            String keyNarration = keyNarrationBase + hash;
            String valNarration = ((LiteralTextContent) text.getContent()).string();
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarration, valNarration)) {
                wText.setTranslateContent(keyNarration);
            }
        }
        else {
            String keyNarrContent = keyNarrationBase + hash + "_1";
            String valNarrContent = ((LiteralTextContent) text.getContent()).string();
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarrContent, valNarrContent)) {
                wText.setTranslateContent(keyNarrContent);
            }
            for(int index = 0; text.getSiblings().size() > index; index++) {
                String keyNarrSibling = keyNarrationBase + hash + "_" + (index + 2);
                String valNarrSibling = getContentLiteral(text, index);
                if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarrSibling, valNarrSibling)) {
                    wText.getSiblingByIndex(index).setTranslateContent(keyNarrSibling);
                }
            }
        }

        return wText;
    }

    public WynnTransText buildNewQuestTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyNewQuest = rootKey + dirFuncional + "NewQuest";
        String valQuestName = getContentLiteral(text, 0);
        String tQuestName = valQuestName.replace(" ", "");
        String keyQuestName = rootKey + dirQuest + tQuestName;

        wText.setTranslateContent(keyNewQuest);

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyQuestName, valQuestName)) {
            wText.getSiblingByIndex(0).setTranslateContent(keyQuestName);
        }

        return wText;
    }

    public WynnTransText buildPressShiftContinue(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + dirFuncional + "PressShift";
        String keyIndent = keyBase + "_indent";

        wText.setTranslateContent(keyIndent);

        for (int index = 0; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "_" + index;

            wText.getSiblingByIndex(index).setTranslateContent(keySibling);
        }

        return wText;
    }

    public WynnTransText buildSelectOptionContinue(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + dirFuncional + "SelectOption";
        String keyIndent = keyBase + "_indent";

        wText.setTranslateContent(keyIndent);

        for (int index = 0; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "_" + index;

            wText.getSiblingByIndex(index).setTranslateContent(keySibling);
        }

        return wText;
    }

    public WynnTransText buildShoutTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyShout = rootKey + dirFuncional + "Shout";
        Matcher m = ChatType.SHOUT.getRegex().matcher(text.getSiblings().get(0).getString());
        if(!m.find()) return wText;
        String name = m.group(1);
        String server = m.group(2);

        wText.getSiblingByIndex(0).setTranslateContent(keyShout, name, server);

        return wText;
    }

    public WynnTransText buildInfoTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + "info";
        String tInfoBody = text.getString();
        String hash = DigestUtils.sha1Hex(tInfoBody).substring(0, 8);

        wText.getSiblingByIndex(0).setTranslateContent(keyBase);

        for (int index = 1; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "." + hash + "_" + index;
            String valSibling = getContentLiteral(text, index);
            if(valSibling.isEmpty()) continue;
            if(valSibling.contains("wynncraft.com")) continue;
            if(valSibling.equals("For more information, visit ")) {
                wText.getSiblingByIndex(index).setTranslateContent(keyBase + ".MoreInfo");
                continue;
            }
            if(valSibling.equals("Visit ")) {
                wText.getSiblingByIndex(index).setTranslateContent(keyBase + ".MoreInfo_alt1");
                continue;
            }
            if(valSibling.equals(" for more!")) {
                wText.getSiblingByIndex(index).setTranslateContent(keyBase + ".MoreInfo_alt2");
                continue;
            }
            if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keySibling, valSibling)) {
                wText.getSiblingByIndex(index).setTranslateContent(keySibling);
            }
        }

        return wText;
    }

    public WynnTransText buildEventInfoTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + "eventInfo";
        String tEInfoBody = text.getString();
        String hash = DigestUtils.sha1Hex(tEInfoBody).substring(0, 8);
        String valEName = getContentLiteral(text, 1);
        String hash2 = DigestUtils.sha1Hex(valEName).substring(0,4);
        String keyEName = keyBase + "EventName" + hash2;

        wText.getSiblingByIndex(0).setTranslateContent(keyBase);

        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyEName, valEName)) {
            wText.getSiblingByIndex(1).setTranslateContent(keyEName);
        }

        for (int index = 2; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "." + hash + "_" + index;
            String valSibling = getContentLiteral(text, index);
            if(valSibling.isEmpty()) continue;
            if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keySibling, valSibling)) {
                wText.getSiblingByIndex(index).setTranslateContent(keySibling);
            }
        }

        return wText;
    }

    public WynnTransText buildCLevelAnnounceTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.CLEVEL_ANNOUNCE.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        wText.removeSibling(6, 7);
        String level = m.group(2);
        WynnTransText playerName = getPlayerName(text, 4);
        String keyCAnnounce = rootKey + dirFuncional + "LevelAnnounce.Combat";

        wText.getSiblingByIndex(3).setTranslateContent(keyCAnnounce + "_pre", level);
        wText.setSiblingByIndex(playerName, 4);
        wText.getSiblingByIndex(5).setTranslateContent(keyCAnnounce + "_suf", level);
        wText.getSiblingByIndex(3).removeStyle();
        wText.getSiblingByIndex(5).removeStyle();

        return wText;
    }

    public WynnTransText buildPLevelAnnounceTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.PLEVEL_ANNOUNCE.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        wText.removeSibling(6, 12);
        WynnTransText playerName = getPlayerName(text, 4);
        String level = m.group(2);
        String profName = m.group(3);
        String keyPAnnounce = rootKey + dirFuncional + "LevelAnnounce.Profession";

        wText.getSiblingByIndex(3).setTranslateContent(keyPAnnounce + "_pre", level, profName);
        wText.setSiblingByIndex(playerName, 4);
        wText.getSiblingByIndex(5).setTranslateContent(keyPAnnounce + "_suf", level, profName);
        wText.getSiblingByIndex(3).removeStyle();
        wText.getSiblingByIndex(5).removeStyle();

        return wText;
    }

    public WynnTransText buildBlacksmithTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBlacksmith = rootKey + dirFuncional + "Blacksmith";
        String textStr = text.getString();

        wText.getSiblingByIndex(0).setTranslateContent(keyBlacksmith);

        if(textStr.contains("I can't buy")) {
            String keySmithNo = keyBlacksmith + ".No";
            wText.getSiblingByIndex(1).setTranslateContent(keySmithNo);
        }
        else if(textStr.contains("You sold me")) {
            String keySmithSold = keyBlacksmith + ".Sold";
            Matcher m = Pattern.compile("^You sold me: (.+)?$").matcher(getContentLiteral(text, 1));
            if(!m.find()) return wText;
            String soldItem = m.group(1) == null ? "" : m.group(1);
            int lastIndex = text.getSiblings().size() - 1;

            wText.getSiblingByIndex(1).setTranslateContent(keySmithSold + "_1", soldItem);
            wText.getSiblingByIndex(lastIndex - 2).setTranslateContent(keySmithSold + "_2");
            wText.getSiblingByIndex(lastIndex).setTranslateContent(keySmithSold + "_3");
        }
        else if(textStr.contains("I can only")) {
            String keySmithOver = keyBlacksmith + ".Over";
            wText.getSiblingByIndex(1).setTranslateContent(keySmithOver);
        }
        else {
            debugClass.writeString2File(textStr, "getString.txt");
            debugClass.writeString2File(text.toString(), "toString.txt");
        }

        return wText;
    }

    public WynnTransText buildIdentifierTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyIden = rootKey + dirFuncional + "Identifier";

        wText.getSiblingByIndex(0).setTranslateContent(keyIden);

        for (int index = 1; text.getSiblings().size() > index; index++) {
            String keySibling = keyIden + "_" + index;
            wText.getSiblingByIndex(index).setTranslateContent(keySibling);
        }

        return wText;
    }

    public WynnTransText buildAreaEnterTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.AREA_ENTER.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String areaName = m.group(1);
        String keyAreaEnter = rootKey + dirFuncional + "Area.Enter";

        wText.getSiblingByIndex(0).setTranslateContent(keyAreaEnter, areaName);

        return wText;
    }

    public WynnTransText buildAreaLeaveTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.AREA_LEAVE.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String areaName = m.group(1);
        String keyAreaLeave = rootKey + dirFuncional + "Area.Leave";

        wText.getSiblingByIndex(0).setTranslateContent(keyAreaLeave, areaName);

        return wText;
    }

    public WynnTransText buildThanksTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        WynnTransText playerName = getPlayerName(text, 1);
        playerName.addStyle(Style.EMPTY.withColor(Formatting.AQUA));
        String keyThanks = rootKey + dirFuncional + "BombThanks";

        wText.getSiblingByIndex(0).setTranslateContent(keyThanks + "_1_pre");
        wText.setSiblingByIndex(playerName,1);
        wText.getSiblingByIndex(2).setTranslateContent(keyThanks + "_1_suf");
        wText.getSiblingByIndex(3).setTranslateContent(keyThanks + "_2");

        return wText;
    }

    public WynnTransText buildThankyouTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.THANK_YOU.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        WynnTransText playerName;
        if(text.getSiblings().size() == 2) {
            playerName = getPlayerName(text, 1);
        }
        else {
            playerName = WynnTransText.of(Text.of(m.group(1)));
        }
        playerName.addStyle(Style.EMPTY.withColor(Formatting.AQUA));
        String keyThankYou = rootKey + dirFuncional + "ThankYou";

        wText.getSiblingByIndex(0).setTranslateContent(keyThankYou + "_pre");
        wText.addSibling(playerName);
        wText.addTranslateSibling(keyThankYou + "_suf");
        wText.getSiblingByIndex(2).addStyle(wText.getSiblingByIndex(0).getStyle());

        return wText;
    }

    public WynnTransText buildCrateGetTranslation(Text text){
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.CRATE_GET.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String keyCrateGet = rootKey + dirFuncional + "CrateReward";
        WynnTransText playerName;
        if(text.getSiblings().size() > 5) {
            wText.getSiblingByIndex(2).setTranslateContent(keyCrateGet + "_1");
            wText.getSiblingByIndex(4).setTranslateContent(keyCrateGet + "_2");
        }
        else {
            playerName = WynnTransText.of(Text.of(m.group(1)));
            wText.addSibling(0, playerName);
            wText.getSiblingByIndex(1).setTranslateContent(keyCrateGet + "_1");
            wText.getSiblingByIndex(3).setTranslateContent(keyCrateGet + "_2");
        }

        return wText;
    }

    public WynnTransText buildRankLogInTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyRankLogIn = rootKey + dirFuncional + "RankLogIn";

        wText.getSiblingByIndex(2).setTranslateContent(keyRankLogIn);

        return wText;
    }

    public WynnTransText buildCombatLevelUpTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.COMBAT_LEVELUP.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        WynnTransText playerName;
        if(text.getSiblings().size() != 0) {
            wText.removeSibling(1,1);
            playerName = getPlayerName(text, 0);
        }
        else {
            playerName = WynnTransText.of(Text.of(m.group(1)));
        }
        String level = m.group(2);
        String keyCombatLvUp = rootKey + dirFuncional + "CombatLevelUp";

        wText.addSibling(0, playerName);
        wText.getSiblingByIndex(1).setTranslateContent(keyCombatLvUp, level);

        return wText;
    }

    public WynnTransText buildProfessionLevelUpTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.PROFESSION_LEVELUP.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        WynnTransText playerName;
        if(text.getSiblings().size() == 4) {
            wText.removeSibling(2, 3);
            playerName = getPlayerName(text, 0);
        }
        else {
            wText.removeSibling(1, 2);
            playerName = WynnTransText.of(Text.of(m.group(1)));
            playerName.addStyle(Style.EMPTY.withColor(Formatting.GOLD));
        }
        String level = m.group(2);
        String professionIcon = "§f" + m.group(3);
        String profession = m.group(4);
        String keyProfLvUp = rootKey + dirFuncional + "ProfLevelUp";

        wText.addSibling(0, playerName);
        wText.getSiblingByIndex(1).setTranslateContent(keyProfLvUp, level, professionIcon, profession);

        return wText;
    }

    public WynnTransText buildSimpleTextTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + dirNormal;
        String valText = text.getString();
        String hash = DigestUtils.sha1Hex(valText);
        String keyText = keyBase + hash;

        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyText, valText)) {
            wText.getSiblingByIndex(0).setTranslateContent(keyText);
        }

        return wText;
    }

    public WynnTransText buildServerRestartTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.SERVER_RESTART.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String number = m.group(1);
        String unit = m.group(2);
        String keyBase = rootKey + dirFuncional + "ServerRestart";

        wText.getSiblingByIndex(0).setTranslateContent(keyBase + "_" + unit, number);

        return wText;
    }

    public WynnTransText buildRestartingTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyRestarting = rootKey + dirFuncional + "Restarting";

        wText.getSiblingByIndex(0).setTranslateContent(keyRestarting);

        return wText;
    }

    public WynnTransText buildDailyRewardTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.DAILY_REWARD.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String emeralds = m.group(1);
        String items = m.group(2);
        String keyDailyReward = rootKey + dirFuncional + "DailyReward";
        String keyDailyRewardEmerald = rootKey + dirFuncional + "DailyReward.Emerald";
        String keyDailyRewardAnd = rootKey + dirFuncional + "DailyReward.and";
        String keyDailyRewardItem = rootKey + dirFuncional + "DailyReward.Item";

        wText.getSiblingByIndex(0).setTranslateContent(keyDailyReward);
        if(emeralds != null) {
            wText.getSiblingByIndex(1).setTranslateContent(keyDailyRewardEmerald, emeralds);
            if(items != null) {
                wText.getSiblingByIndex(2).setTranslateContent(keyDailyRewardAnd);
                wText.getSiblingByIndex(3).setTranslateContent(keyDailyRewardItem, items);
            }
        }
        else {
            wText.getSiblingByIndex(1).setTranslateContent(keyDailyRewardItem, items);
        }

        return wText;
    }

    public WynnTransText buildDisguiseTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyDisguise = rootKey + dirFuncional + "Disguise";

        wText.getSiblingByIndex(1).setTranslateContent(keyDisguise + "_1");
        wText.getSiblingByIndex(3).setTranslateContent(keyDisguise + "_2");

        return wText;
    }

    public WynnTransText buildSkillCooldownTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keySkillCooldown = rootKey + dirFuncional + "SkillCooldown";

        wText.getSiblingByIndex(4).setTranslateContent(keySkillCooldown);

        return wText;
    }

    public WynnTransText buildSpeedboostTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.SPEEDBOOST.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String duration = m.group(1);
        String keySpeedboost = rootKey + dirFuncional + "Speedboost";

        wText.getSiblingByIndex(0).setTranslateContent(keySpeedboost, duration);
        wText.getSiblingByIndex(1).setTranslateContent(keySpeedboost + "_suf");
        wText.addTranslateSibling(0, keySpeedboost + "_pre");
        wText.getSiblingByIndex(0).addStyle(Style.EMPTY.withColor(Formatting.GRAY));

        return wText;
    }

    public  WynnTransText buildResistanceTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyResistance = rootKey + dirFuncional + "Resistance";
        int lastIndex = text.getSiblings().size() - 1;

        wText.getSiblingByIndex(1).setTranslateContent(keyResistance + "_1");
        wText.getSiblingByIndex(2).setTranslateContent(keyResistance + "_2");
        wText.getSiblingByIndex(lastIndex).setTranslateContent(keyResistance + "_5");
        if(lastIndex == 5) {
            wText.getSiblingByIndex(3).setTranslateContent(keyResistance + "_3");
            wText.getSiblingByIndex(4).setTranslateContent(keyResistance + "_4");
        }

        return wText;
    }

    /**
     * Get content of target sibling refer by index as string
     *
     * @param text Target text
     * @param index Target sibling index
     * @return Some string when content is not empty, blank string otherwise
     */
    private String getContentLiteral(Text text, int index) {
        TextContent content = text.getSiblings().get(index).getContent();
        if("empty".equals(content.toString())) return "";
        return ((LiteralTextContent) content).string();
    }

    /**
     * Get player name from target sibling refer by index as string
     *
     * @param text Target text
     * @param index Target sibling index
     * @return Player name Text
     */
    private WynnTransText getPlayerName(Text text, int index) {
        String name = getContentLiteral(text, index);
        if("".equals(name)) {
            return WynnTransText.of(text.getSiblings().get(index).getSiblings().get(0));
        }
        else {
            return WynnTransText.of(text.getSiblings().get(index));
        }
    }
}
