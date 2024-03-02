package net.hexagreen.wynntrans.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.chat.types.*;
import net.hexagreen.wynntrans.chat.types.glue.SelectionGlue;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.ChatType;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.hexagreen.wynntrans.enums.GlueType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class IncomeTextHandler {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected TextGlue textGlue;
    private final BackgroundText backgroundText;

    public IncomeTextHandler() {
        this.textGlue = null;
        this.backgroundText = new BackgroundText();
    }

    public void attachGlue(Text text) {
        if(this.textGlue == null) {
            try {
                this.textGlue = GlueType.findAndGet(text);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ignore) {
                LOGGER.warn("GlueType.findAndGet Error.");
            }
        }
    }

    public void removeGlue() {
        this.textGlue = null;
    }

    public void onStartWorldTick() {
        this.backgroundText.flush();
        if(this.textGlue != null) textGlue.timer();
    }

    public boolean sortIncomeText(Text text) {
        try{
            if(TextContent.EMPTY.equals(text.getContent())) {
                if(!text.contains(Text.of("\n"))) {
                    return this.analyseSinglelineText(text);
                } else {
                    return this.analyseMultilineText(text);
                }
            }
            else {
                return this.analyseLiteralText(text);
            }
        } catch(Exception e) {
            LOGGER.error("Error in sortIncomeText", e);
            debugClass.writeString2File(text.getString(), "exception.txt");
            debugClass.writeTextAsJSON(text, "Exception");
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
            ChatType chatType = ChatType.findType(text);
            switch(chatType) {
                case NO_TYPE -> {
                    debugClass.writeString2File(text.getString(), "literal.txt");
                    debugClass.writeTextAsJSON(text, "Literal");
                    return false;
                }
                case PRIVATE_MESSAGE, NORMAL_CHAT -> {
                    return false;
                }
            }
            chatType.run(text);
        } catch(Exception e) {
            LOGGER.error("Error in analyseLiteralText", e);
        }
        return true;
    }

    private boolean analyseSinglelineText(Text text) {
        try {
            attachGlue(text);
            if(this.textGlue != null) {
                if(!this.textGlue.push(text)) {
                    attachGlue(text);
                    if(this.textGlue != null) return this.textGlue.push(text);
                }
                else return true;
            }
            return ChatType.findAndRun(text);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOGGER.warn("ChatType.findAndRun Error.");
            return false;
        }
    }

    private boolean analyseMultilineText(Text text) {
        if(FunctionalRegex.DIALOG_PLACEHOLDER.match(text, 2) || FunctionalRegex.DIALOG_PLACEHOLDER.match(text, 1)) {
            this.backgroundText.clear();
            return new DialogPlaceholder(text, FunctionalRegex.DIALOG_PLACEHOLDER.getRegex()).print();
        }
        if(FunctionalRegex.QUEST_COMPLETE.match(text, 1)) {
            return new QuestCompleted(editMultilineQuestComplete(text), null).print();
        }
        if(FunctionalRegex.QUEST_COMPLETE.match(text, 0)) {
            return new QuestCompleted(editMultilineQuestCompleteNoHeader(text), null).print();
        }
        if(textGlue instanceof SelectionGlue) {
            this.backgroundText.clear();
            return textGlue.push(text);
        }

        return this.processMultilineText(text);
    }

    private boolean processMultilineText(Text text) {
        if(FunctionalRegex.SELECTION_OPTION.match(text, 6)) {
            this.backgroundText.clear();
            this.textGlue = new SelectionGlue();
            return this.textGlue.push(text);
        }

        else if (text.getSiblings().get(0).equals(text.getSiblings().get(4))
                && text.getSiblings().get(0).equals(text.getSiblings().get(text.getSiblings().size() - 1))
                && text.getSiblings().get(0).equals(Text.empty())
                && (text.getSiblings().size() == 5 || text.getSiblings().size() == 9)) {
            this.backgroundText.clear();
            if (ChatType.DIALOG_NORMAL.match(text, 2)) {
                new NpcDialogFocused(text, ChatType.DIALOG_NORMAL.getRegex()).print();
            } else if (ChatType.NEW_QUEST.match(text, 2)) {
                new NewQuestFocused(text, ChatType.NEW_QUEST.getRegex()).print();
            } else if (ChatType.DIALOG_ITEM.match(text, 2)) {
                new ItemGiveAndTakeFocused(text, ChatType.DIALOG_ITEM.getRegex()).print();
            } else if (FunctionalRegex.MINI_QUEST_DESC.match(text, 2)) {
                new MiniQuestInfoConfirmable(text, FunctionalRegex.MINI_QUEST_DESC.getRegex()).print();
            } else if (FunctionalRegex.DIALOG_ALERT.match(text, 2)) {
                new GuideAlert(text.getSiblings().get(2), null).print();
            } else {
                new NarrationFocused(text, null).print();
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
            if(storage.isEmpty()) return;
            MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
            SimpleText.setTranslationControl(false);
            for(Text chunk : storage) {
                for(Text line : chunk.getSiblings()) {
                    if("\n".equals(line.getString())) continue;
                    if(!sortIncomeText(line)) {
                        //noinspection DataFlowIssue
                        MinecraftClient.getInstance().player.sendMessage(line);
                    }
                }
            }
            SimpleText.setTranslationControl(true);
            clear();
        }
    }
}