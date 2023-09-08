package net.hexagreen.wynntrans;

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
    private boolean slownessRemoved;
    private long slownessRemovedTime;
    private boolean selectionDialogReady;
    private static final long SLOWNESS_APPLY_DIFFERANCE = 2;

    public IncomeTextHandler() {
        this.concatingSelection = false;
        this.gluedDialog = MutableText.of(TextContent.EMPTY);
        this.backgroundText = Lists.newArrayList();
        this.pendingText = Lists.newArrayList();
        this.slownessRemovedTime = 0;
        this.selectionDialogReady = false;
        this.slownessRemoved = false;
    }

    public void onSlownessRemoved() {
        this.slownessRemoved = true;
        this.backgroundText = Lists.newArrayList();
        this.slownessRemovedTime = MinecraftClient.getInstance().world.getTime();
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
    }

    public void onSlownessApplied() {
        this.slownessRemoved = false;
    }

    public void sendPendingText() {
        if(this.slownessRemoved) {
            if(MinecraftClient.getInstance().world.getTime() - this.slownessRemovedTime >= SLOWNESS_APPLY_DIFFERANCE) {
                for(Text background : this.pendingText) {
                    MinecraftClient.getInstance().player.sendMessage(background);
                }
                this.slownessRemoved = false;
            }
        }
    }

    public boolean sortIncomeText(Text text) {
        if(TextContent.EMPTY.equals(text.getContent())) {
            if(!text.getString().contains("\n")) {
                return this.analyseSinglelineText(text);
            } else {
                return this.analyseMultilineText(text);
            }
        }
        return false;
    }

    private boolean analyseSinglelineText(Text text) {
        WynnTransText out = WynnTransText.of(text);
        switch (ChatType.findType(text)) {
            case NORMAL_CHAT -> {
                return false;
            }
            case SHOUT -> out = WynnTrans.translationBuilder.buildShoutTranslation(text);
            case INFO -> out = WynnTrans.translationBuilder.buildInfoTranslation(text);
            case INFO_EVENT -> out = WynnTrans.translationBuilder.buildEventInfoTranslation(text);
            case CLEVEL_ANNOUNCE -> out = WynnTrans.translationBuilder.buildCLevelAnnounceTranslation(text);
            case PLEVEL_ANNOUNCE -> out = WynnTrans.translationBuilder.buildPLevelAnnounceTranslation(text);
            case BLACKSMITH_NO -> out = WynnTrans.translationBuilder.buildBlacksmithNoTranslation(text);
            case BLACKSMITH_SOLD -> out = WynnTrans.translationBuilder.buildBlacksmithSoldTranslation(text);
            case IDENTIFIER -> out = WynnTrans.translationBuilder.buildIdentifierTranslation(text);
            case AREA_ENTER -> out = WynnTrans.translationBuilder.buildAreaEnterTranslation(text);
            case AREA_LEAVE -> out = WynnTrans.translationBuilder.buildAreaLeaveTranslation(text);
            case BOMB_THANK -> out = WynnTrans.translationBuilder.buildThanksTranslation(text);
            case THANK_YOU -> out = WynnTrans.translationBuilder.buildThankyouTranslation(text);
            case CRATE_GET -> out = WynnTrans.translationBuilder.buildCrateGetTranslation(text);
            case ITEMBOMB_THROWN -> {
                return false;
            }
            case ITEMBOMB_MESSAGE -> {
                return false;
            }
            case NO_TYPE -> {
                debugClass.writeString2File(text.getString(), "getString.txt");
                debugClass.writeString2File(text.toString(), "toString.txt");
                debugClass.writeTextListAsJSON(text);
                out = WynnTransText.of(text);
            }
        }
        this.printText(out);
        return true;
    }

    private boolean analyseMultilineText(Text text) {
        if(ChatType.DIALOG_PLACEHOLDER.match(text, 2)) {
            this.printFocusedText(text);
            return true;
        }
        if(this.concatingSelection && (ChatType.SELECTION_END.match(text) || ChatType.SELECTION_OPTION.match(text))) {
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
            WynnTransText out = WynnTransText.of(text);
            if(ChatType.DIALOG_NORMAL.match(text, 2)) {
                out.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNPCDialogTranslation(text.getSiblings().get(2)));
            }
            else if(ChatType.NEW_QUEST.match(text, 2)){
                out.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNewQuestTranslation(text.getSiblings().get(2)));
            }
            else if(ChatType.DIALOG_ITEM.match(text, 2)) {

            }
            else {
                out.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNarrationTranslation(text.getSiblings().get(2)));
            }
            out.setSiblingByIndex(6, WynnTrans.translationBuilder.buildPressShiftContinue(text.getSiblings().get(6)));
            this.printFocusedText(out);
            return true;
        }
        else if(ChatType.SELECTION_OPTION.match(text, 6)) {
            this.concatingSelection = !ChatType.SELECTION_END.match(text);
            if(this.concatingSelection) {
                this.concatSelectionDialog(text, false);
                return true;
            }
        }
        else {
            if(this.slownessRemoved) {
                this.backgroundText.add(text);
                if(this.backgroundText.size() == 4) {
                    this.pendingText = this.backgroundText;
                    this.backgroundText = Lists.newArrayList();
                }
            }
            return true;
        }
        return false;
    }

    private boolean processConfirmlessDialog(Text text) {
        WynnTransText out = WynnTransText.of(text);
        if(ChatType.DIALOG_NORMAL.match(text, 2)) {
            out.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNPCDialogTranslation(text.getSiblings().get(2)));
        }
        else {
            out.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNarrationTranslation(text.getSiblings().get(2)));
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
                WynnTransText out = WynnTrans.translationBuilder.buildNPCSelectionTranslation(this.gluedDialog);
                this.printFocusedText(out);
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
