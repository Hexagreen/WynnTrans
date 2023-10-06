package net.hexagreen.wynntrans;

import net.hexagreen.wynntrans.texts.NpcDialog;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
@SuppressWarnings("DataFlowIssue")
public class IncomeTextHandler {
    private boolean concatingSelection;
    private MutableText gluedDialog;
    private List<Text> backgroundText;
    private List<Text> pendingText;
    private byte pendingTime;
    private byte pendingCounter;
    private boolean selectionDialogReady;
    private static final long PENDINGTICKTHRES = 2;

    public IncomeTextHandler() {
        this.concatingSelection = false;
        this.gluedDialog = MutableText.of(TextContent.EMPTY);
        this.backgroundText = Lists.newArrayList();
        this.pendingText = Lists.newArrayList();
        this.selectionDialogReady = false;
        this.pendingTime = 0;
        this.pendingCounter = 0;
    }

    public void sendPendingText() {
        if(this.pendingCounter == 4) {
            if(++this.pendingTime >= PENDINGTICKTHRES) {
                this.pendingCounter = 0;
                this.pendingTime = 0;
                MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
                for(Text background : this.pendingText) {
                    MinecraftClient.getInstance().player.sendMessage(background);
                }
            }
        }
    }

    public boolean sortIncomeText(Text text) {
        try{
            if(TextContent.EMPTY.equals(text.getContent())) {
                if(!text.getString().contains("\n")) {
                    return this.analyseSinglelineText(text);
                } else {
                    return this.analyseMultilineText(text);
                }
            }
            else {
                return this.analyseLiteralText(text);
            }
        } catch(Exception e) {
            debugClass.writeString2File(text.getString(), "exception.txt");
            debugClass.writeTextAsJSON(text);
        }
        return false;
    }

    private boolean analyseLiteralText(Text text) {
        WynnTransText out = WynnTransText.of(text);
        switch (ChatType.findType(text)) {
            case COMBAT_LEVELUP -> out = WynnTrans.translationBuilder.buildCombatLevelUpTranslation(text);
            case NO_TYPE -> debugClass.writeString2File(text.getString(), "literal.txt");
        }
        this.printText(out);
        return true;
    }

    private boolean analyseSinglelineText(Text text) {
        WynnTransText out = WynnTransText.of(text);
        switch (ChatType.findType(text)) {
            case NORMAL_CHAT, PRIVATE_MESSAGE -> {
                return false;
            }
            case SHOUT -> out = WynnTrans.translationBuilder.buildShoutTranslation(text);
            case INFO -> out = WynnTrans.translationBuilder.buildInfoTranslation(text);
            case INFO_EVENT -> out = WynnTrans.translationBuilder.buildEventInfoTranslation(text);
            case CLEVEL_ANNOUNCE -> out = WynnTrans.translationBuilder.buildCLevelAnnounceTranslation(text);
            case PLEVEL_ANNOUNCE -> out = WynnTrans.translationBuilder.buildPLevelAnnounceTranslation(text);
            case BLACKSMITH -> out = WynnTrans.translationBuilder.buildBlacksmithTranslation(text);
            case IDENTIFIER -> out = WynnTrans.translationBuilder.buildIdentifierTranslation(text);
            case AREA_ENTER -> out = WynnTrans.translationBuilder.buildAreaEnterTranslation(text);
            case AREA_LEAVE -> out = WynnTrans.translationBuilder.buildAreaLeaveTranslation(text);
            case BOMB_THANK -> out = WynnTrans.translationBuilder.buildThanksTranslation(text);
            case THANK_YOU -> out = WynnTrans.translationBuilder.buildThankyouTranslation(text);
            case CRATE_GET -> out = WynnTrans.translationBuilder.buildCrateGetTranslation(text);
//            case ITEMBOMB_THROWN -> {
//            }
//            case ITEMBOMB_MESSAGE -> {
//            }
            case RANKS_LOGIN -> out = WynnTrans.translationBuilder.buildRankLogInTranslation(text);
            case COMBAT_LEVELUP -> out = WynnTrans.translationBuilder.buildCombatLevelUpTranslation(text);
            case PROFESSION_LEVELUP -> out = WynnTrans.translationBuilder.buildProfessionLevelUpTranslation(text);
            case SERVER_RESTART -> out = WynnTrans.translationBuilder.buildServerRestartTranslation(text);
            case RESTARTING -> out = WynnTrans.translationBuilder.buildRestartingTranslation(text);
            case DAILY_REWARD -> out = WynnTrans.translationBuilder.buildDailyRewardTranslation(text);
            case DISGUISE -> out = WynnTrans.translationBuilder.buildDisguiseTranslation(text);
            case SKILL_COOLDOWN -> out = WynnTrans.translationBuilder.buildSkillCooldownTranslation(text);
            case SPEEDBOOST -> out = WynnTrans.translationBuilder.buildSpeedboostTranslation(text);
            case RESISTANCE -> out = WynnTrans.translationBuilder.buildResistanceTranslation(text);
            case PARTYFINDER -> out = WynnTrans.translationBuilder.buildPartyFinderTranslation(text);
            case MERCHANT -> out = WynnTrans.translationBuilder.buildMerchantTranslation(text);
            case NO_TYPE -> {
                if(text.getSiblings().size() == 1) {
                    out = WynnTrans.translationBuilder.buildSimpleTextTranslation(text);
                }
                else {
                    debugClass.writeString2File(text.getString(), "getString.txt");
                    debugClass.writeString2File(text.toString(), "toString.txt");
                    debugClass.writeTextAsJSON(text);
                    out = WynnTransText.of(text);
                }
            }
        }
        this.printText(out);
        return true;
    }

    private boolean analyseMultilineText(Text text) {
        if(ChatType.DIALOG_PLACEHOLDER.match(text, 2)) {
            this.pendingCounter = 0;
            this.printFocusedText(text);
            return true;
        }
        if(this.concatingSelection && (ChatType.SELECTION_END.match(text) || ChatType.SELECTION_OPTION.match(text))) {
            this.pendingCounter = 0;
            this.concatSelectionDialog(text, ChatType.SELECTION_END.match(text));
            return true;
        }
        List<Text> sibling = text.getSiblings();
        switch (sibling.size()) {
            case 5 -> {
                if (sibling.get(1).equals(Text.of("\n"))
                        && sibling.get(1).equals(sibling.get(3))) {
                    return this.processConfirmlessDialog(text);
                }
            }
            case 9 -> {
                if (sibling.get(1).equals(Text.of("\n"))
                        && sibling.get(1).equals(sibling.get(3))
                        && sibling.get(3).equals(sibling.get(5))
                        && sibling.get(5).equals(sibling.get(7))
                ) {
                    return this.processConfirmableDialog(text);
                }
            }
        }
        return false;
    }

    private boolean processConfirmableDialog(Text text) {
        if(ChatType.DIALOG_END.match(text, 6)) {
            this.pendingCounter = 0;
            WynnTransText out = WynnTransText.of(text);
            if(ChatType.DIALOG_NORMAL.match(text, 2)) {
                NpcDialog.of(text, ChatType.DIALOG_NORMAL.getRegex(), "confirmable").print();
            }
            else if(ChatType.NEW_QUEST.match(text, 2)){
                out.setSiblingByIndex(WynnTrans.translationBuilder.buildNewQuestTranslation(text.getSiblings().get(2)), 2);
            }
//            else if(ChatType.DIALOG_ITEM.match(text, 2)) {
//
//            }
            else {
                out.setSiblingByIndex(WynnTrans.translationBuilder.buildNarrationTranslation(text.getSiblings().get(2)), 2);
            }
            out.setSiblingByIndex(WynnTrans.translationBuilder.buildPressShiftContinue(text.getSiblings().get(6)), 6);
            this.printFocusedText(out);
            return true;
        }
        else if(ChatType.SELECTION_OPTION.match(text, 6)) {
            this.pendingCounter = 0;
            this.concatingSelection = !ChatType.SELECTION_END.match(text);
            if(this.concatingSelection) {
                this.concatSelectionDialog(text, false);
                return true;
            }
        }
        else {
            this.backgroundText.add(text);
            this.pendingCounter++;
            if(this.backgroundText.size() == 4) {
                this.pendingText = this.backgroundText;
                this.backgroundText = Lists.newArrayList();
            }
            return true;
        }
        return false;
    }

    private boolean processConfirmlessDialog(Text text) {
        this.pendingCounter = 0;
        WynnTransText out = WynnTransText.of(text);
        if(ChatType.DIALOG_NORMAL.match(text, 2)) {
            NpcDialog.of(text, ChatType.DIALOG_NORMAL.getRegex(), "confirmless").print();
        }
        else {
            out.setSiblingByIndex(WynnTrans.translationBuilder.buildNarrationTranslation(text.getSiblings().get(2)), 2);
        }
        this.printFocusedText(out);
        return true;
    }

    private void concatSelectionDialog(Text text, boolean isLastDialog) {
        if(this.selectionDialogReady){
            for(Text sibling : text.getSiblings()) {
                this.gluedDialog.append(sibling);
            }
            if(isLastDialog) {
                this.concatingSelection = false;
                this.selectionDialogReady = false;
                NpcDialog.of(gluedDialog, ChatType.DIALOG_NORMAL.getRegex(), "selection").print();
                this.gluedDialog = MutableText.of(TextContent.EMPTY);
            }
            else{
                this.gluedDialog.append(Text.of("\n"));
            }
        }
        else {
            if(ChatType.SELECTION_END.match(text)){
                this.selectionDialogReady = true;
            }
        }
    }

    private void printText(Text text) {
        MinecraftClient.getInstance().player.sendMessage(text);
    }

    private void printFocusedText(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
        MinecraftClient.getInstance().player.sendMessage(text);
    }
}