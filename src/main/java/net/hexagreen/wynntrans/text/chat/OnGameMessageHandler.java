package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.chat.types.*;
import net.hexagreen.wynntrans.text.chat.types.glue.SelectionGlue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class OnGameMessageHandler {
    private static final Logger LOGGER = WynnTrans.LOGGER;
    private final BackgroundText backgroundText;
    protected TextGlue textGlue;
    private boolean recordAll;
    private boolean registerBackText;

    public OnGameMessageHandler() {
        this.textGlue = null;
        this.backgroundText = new BackgroundText();
        this.recordAll = false;
        this.registerBackText = false;
    }

    public void toggleRecordMode() {
        this.recordAll = !recordAll;
        if(recordAll)
            WynnTransText.transportMessage(Text.translatable("wytr.command.chatForceRecordMode.enable"));
        else
            WynnTransText.transportMessage(Text.translatable("wytr.command.chatForceRecordMode.disable"));
    }

    public void toggleBTRegisterMode() {
        this.registerBackText = !registerBackText;
        if(registerBackText)
            WynnTransText.transportMessage(Text.translatable("wytr.command.registerBackgroundTextMode.enable"));
        else
            WynnTransText.transportMessage(Text.translatable("wytr.command.registerBackgroundTextMode.disable"));
    }

    public void attachGlue(Text text) {
        if(this.textGlue == null) {
            this.textGlue = GlueType.findAndGet(text);
        }
    }

    public void removeGlue() {
        this.textGlue = null;
    }

    public void onStartWorldTick() {
        this.backgroundText.flush();
        if(this.textGlue != null) textGlue.timer();
    }

    public boolean translateChatText(Text text) {
        if(MinecraftClient.getInstance().world == null) return false;
        if(recordAll) debugClass.writeTextAsJSON(text, "Record");
        if(text.getContent() instanceof TranslatableTextContent) return false;
        try {
            if(PlainTextContent.EMPTY.equals(text.getContent())) {
                if(!text.contains(Text.of("\n"))) {
                    return this.analyseSinglelineText(text);
                }
                else {
                    if(text.getString().matches("\\n")) return true;
                    return this.analyseMultilineText(text);
                }
            }
            else {
                return this.analyseLiteralText(text);
            }
        }
        catch(Exception e) {
            LOGGER.error("Error in sortIncomeText", e);
            debugClass.writeTextAsJSON(text, "Exception");
            backgroundText.clear();
            removeGlue();
        }
        return false;
    }

    private boolean analyseLiteralText(Text text) {
        attachGlue(text);
        if(this.textGlue != null) {
            if(!this.textGlue.push(text)) {
                attachGlue(text);
                if(this.textGlue != null) return this.textGlue.push(text);
            }
            else return true;
        }

        if(isLiteralBackgroundText(text)) {
            this.backgroundText.push(reformLiteralBackgroundText(text));
            return true;
        }

        try {
            return ChatType.findAndRun(text);
        }
        catch(Exception e) {
            LOGGER.error("Error in analyseLiteralText");
            throw e;
        }
    }

    private boolean analyseSinglelineText(Text text) {
        attachGlue(text);
        if(this.textGlue != null) {
            if(!this.textGlue.push(text)) {
                attachGlue(text);
                if(this.textGlue != null) return this.textGlue.push(text);
            }
            else return true;
        }

        for(Text sibling : text.getSiblings()) {
            if(sibling.getString().equals("\n")) return false;
        }

        try {
            return ChatType.findAndRun(text);
        }
        catch(Exception e) {
            LOGGER.error("Error in analyseSinglelineText");
            throw e;
        }
    }

    private boolean analyseMultilineText(Text text) {
        if(FunctionalRegex.DIALOG_PLACEHOLDER.match(text, 2) || FunctionalRegex.DIALOG_PLACEHOLDER.match(text, 1)) {
            this.backgroundText.clear();
            return new DialogPlaceholder(text).print();
        }
        if(FunctionalRegex.QUEST_COMPLETE.match(text, 1)) {
            return new QuestCompleted(editMultilineQuestComplete(text)).print();
        }
        if(FunctionalRegex.QUEST_COMPLETE.match(text, 0)) {
            return new QuestCompleted(editMultilineQuestCompleteNoHeader(text)).print();
        }
        if(textGlue instanceof SelectionGlue) {
            this.backgroundText.clear();
            return textGlue.push(text);
        }
        if(isBrokenText(text)) return false;

        return this.processMultilineText(text);
    }

    private boolean processMultilineText(Text text) {
        if(FunctionalRegex.SELECTION_OPTION.match(text, 6)) {
            this.backgroundText.clear();
            this.textGlue = new SelectionGlue();
            return this.textGlue.push(text);
        }

        else if(text.getSiblings().getFirst().equals(text.getSiblings().get(4)) && text.getSiblings().getFirst().equals(text.getSiblings().getLast()) && text.getSiblings().getFirst().equals(Text.empty()) && (text.getSiblings().size() == 5 || text.getSiblings().size() == 9)) {
            this.backgroundText.clear();
            if(ChatType.DIALOG_NORMAL.match(text, 2)) {
                new NpcDialogFocused(text).print();
            }
            else if(ChatType.NEW_QUEST.match(text, 2)) {
                new NewQuestFocused(text).print();
            }
            else if(ChatType.DIALOG_ITEM.match(text, 2)) {
                new ItemGiveAndTakeFocused(text).print();
            }
            else if(FunctionalRegex.MINI_QUEST_DESC.match(text, 2)) {
                new MiniQuestInfoFocused(text).print();
            }
            else if(FunctionalRegex.DIALOG_ALERT.match(text, 2)) {
                new GuideAlert(text.getSiblings().get(2)).print();
            }
            else {
                new NarrationFocused(text).print();
            }
            return true;
        }

        else {
            this.backgroundText.push(text);
            return true;
        }
    }

    private Text editMultilineQuestComplete(Text text) {
        MutableText result = Text.empty();
        MutableText tmp = Text.empty();
        for(Text sibling : text.getSiblings()) {
            if(sibling.getString().equals("\n")) {
                tmp.append(sibling);
                result.append(tmp);
                tmp = Text.empty();
            }
            else {
                tmp.append(sibling);
            }
        }
        if(!tmp.equals(Text.empty())) {
            result.append(tmp);
        }
        return result;
    }

    private Text editMultilineQuestCompleteNoHeader(Text text) {
        MutableText result = Text.empty().append("\n");
        MutableText tmp = Text.empty();
        for(Text sibling : text.getSiblings()) {
            if(sibling.getString().equals("\n")) {
                tmp.append(sibling);
                result.append(tmp);
                tmp = Text.empty();
            }
            else {
                tmp.append(sibling);
            }
        }
        if(!tmp.equals(Text.empty())) {
            result.append(tmp);
        }
        return result;
    }

    private boolean isLiteralBackgroundText(Text text) {
        int i = 0;
        if(text.getContent().toString().equals("\n")) i++;
        for(Text sibling : text.getSiblings()) {
            if(sibling.equals(Text.of("\n"))) i++;
        }
        return i == 4;
    }

    private boolean isBrokenText(Text text) {
        return text.getContent() == PlainTextContent.EMPTY && Text.empty().equals(text.getSiblings().get(0)) && text.getSiblings().get(0) == text.getSiblings().get(2) && text.getSiblings().get(1).getString().equals("\n");
    }

    private Text reformLiteralBackgroundText(Text text) {
        MutableText reformed = Text.empty();
        MutableText line;
        if(text.getContent().toString().equals("\n")) {
            line = Text.empty().append("\n");
            reformed.append(line);
            line = Text.empty();
        }
        else {
            line = Text.empty();
            line.append(text.copyContentOnly().setStyle(text.getStyle()));
        }

        for(Text sibling : text.getSiblings()) {
            if(!sibling.getString().equals("\n")) line.append(sibling);
            else {
                line.append("\n");
                reformed.append(line);
                line = Text.empty();
            }
        }

        if(!line.equals(Text.empty())) reformed.append(line);

        return reformed;
    }

    private class BackgroundText {
        private final List<Text> storage = Lists.newArrayList();

        private void push(Text text) {
            this.storage.add(text);
        }

        private void clear() {
            this.storage.clear();
        }

        private void flush() {
            List<Text> storage = new ArrayList<>(this.storage);
            if(storage.isEmpty()) return;
            MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
            if(!registerBackText) {
                SimpleText.setTranslationControl(false);
                SimpleSystemText.setTranslationControl(false);
            }
            for(Text chunk : storage) {
                for(Text line : chunk.getSiblings()) {
                    if("\n".equals(line.getString())) continue;
                    if(!translateChatText(line)) {
                        WynnTransText.transportMessage(line);
                    }
                }
            }
            SimpleText.setTranslationControl(true);
            SimpleSystemText.setTranslationControl(true);
            clear();
        }
    }
}