package net.hexagreen.wynntrans;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.chat.*;
import net.hexagreen.wynntrans.chat.glue.SelectionGlue;
import net.hexagreen.wynntrans.chat.glue.TextGlue;
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
    private static final byte PENDINGTICKTHRES = 2;
    protected TextGlue textGlue;
    private List<Text> backgroundText;
    private List<Text> pendingText;
    private byte pendingTime;
    private byte pendingCounter;

    public IncomeTextHandler() {
        this.textGlue = null;
        this.backgroundText = Lists.newArrayList();
        this.pendingText = Lists.newArrayList();
        this.pendingTime = 0;
        this.pendingCounter = 0;
    }

    public void attachGlue(Text text) {
        if(this.textGlue == null) {
            try {
                this.textGlue = GlueType.findAndGet(text);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignore) {
                LOGGER.warn("GlueType.findAndGet Error.");
            }
        }
    }

    public void removeGlue() {
        this.textGlue = null;
    }

    @SuppressWarnings("DataFlowIssue")
    public void onStartWorldTick() {
        if(this.pendingCounter == 4) {
            if(++this.pendingTime >= PENDINGTICKTHRES) {
                this.pendingCounter = 0;
                this.pendingTime = 0;
                MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
                for(Text chunk : this.pendingText) {
                    for(Text line : chunk.getSiblings()) {
                        if("\n".equals(line.getString())) continue;
                        if(!sortIncomeText(line)){
                            MinecraftClient.getInstance().player.sendMessage(line);
                        }
                    }
                }
                this.pendingText = Lists.newArrayList();
            }
        }
        if(this.textGlue != null) textGlue.timer();
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
            LOGGER.error("Error in sortIncomeText", e);
            debugClass.writeString2File(text.getString(), "exception.txt");
            debugClass.writeTextAsJSON(text);
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
        try {
            switch (ChatType.findType(text)) {
                case COMBAT_LEVELUP -> {
                    return ChatType.COMBAT_LEVELUP.run(text);
                }
                case DIALOG_LITERAL -> {
                    return ChatType.DIALOG_LITERAL.run(text);
                }
                case NO_TYPE -> {
                    debugClass.writeString2File(text.getString(), "literal.txt");
                    debugClass.writeTextAsJSON(text);
                    return false;
                }
            }
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
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn("ChatType.findAndRun Error.");
            return false;
        }
    }

    private boolean analyseMultilineText(Text text) {
        if(FunctionalRegex.DIALOG_PLACEHOLDER.match(text, 2)) {
            this.pendingCounter = 0;
            return DialogPlaceholder.of(text, FunctionalRegex.DIALOG_PLACEHOLDER.getRegex()).print();
        }
        if(FunctionalRegex.QUEST_COMPLETE.match(text, 1)) {
            return QuestCompleted.of(editMalformedQuestComplete(text), null).print();
        }
        if(textGlue instanceof SelectionGlue && (FunctionalRegex.SELECTION_END.match(text) || FunctionalRegex.SELECTION_OPTION.match(text))) {
            this.pendingCounter = 0;
            return textGlue.push(text);
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
            default -> {
                debugClass.writeTextAsJSON(text);
                return false;
            }
        }
        return false;
    }

    private boolean processConfirmableDialog(Text text) {
        if(FunctionalRegex.DIALOG_END.match(text, 6)) {
            this.pendingCounter = 0;
            if(ChatType.DIALOG_NORMAL.match(text, 2)) {
                NpcDialogConfirmable.of(text, ChatType.DIALOG_NORMAL.getRegex()).print();
            }
            else if(ChatType.NEW_QUEST.match(text, 2)){
                NewQuest.of(text, ChatType.NEW_QUEST.getRegex()).print();
            }
            else if(ChatType.DIALOG_ITEM.match(text, 2)) {
                ItemGiveAndTakeConfirmable.of(text, ChatType.DIALOG_ITEM.getRegex()).print();
            }
            else {
                NarrationConfirmable.of(text, null).print();
            }
            return true;
        }
        else if(FunctionalRegex.SELECTION_OPTION.match(text, 6)) {
            this.pendingCounter = 0;
            this.textGlue = SelectionGlue.get();
            this.textGlue.push(text);
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
        if(ChatType.DIALOG_NORMAL.match(text, 2)) {
            NpcDialogConfirmless.of(text, ChatType.DIALOG_NORMAL.getRegex()).print();
        }
        else if(ChatType.DIALOG_ITEM.match(text, 2)) {
            ItemGiveAndTakeConfirmable.of(text, ChatType.DIALOG_ITEM.getRegex()).print();
        }
        else if(text.getSiblings().get(2).getString().equals("empty")) {
            return false;
        }
        else {
            NarrationConfirmless.of(text, null).print();
        }
        return true;
    }

    private Text editMalformedQuestComplete(Text text) {
        MutableText result = Text.empty();
        MutableText tmp = Text.empty();
        for(Text sibling : text.getSiblings()) {
            if(sibling.getString().equals("\n")) {
                tmp.append(text);
                result.append(tmp);
                tmp = Text.empty();
            }
            else {
                tmp.append(text);
            }
        }
        return result;
    }
}