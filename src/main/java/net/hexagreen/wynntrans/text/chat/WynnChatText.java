package net.hexagreen.wynntrans.text.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.chat.types.SimpleText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnChatText extends WynnTransText {
    protected static Text removedCustomNickname = null;
    protected Matcher matcher;

    public WynnChatText(Text text, Pattern regex) {
        super(text);
        if(regex != null) {
            this.matcher = createMatcher(text, regex);
            boolean ignore = this.matcher.find();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean print() {
        Text printLine = text();
        if(printLine == null) return true;
        MinecraftClient.getInstance().player.sendMessage(printLine);
        return true;
    }

    public MutableText text() {
        try {
            build();
            return resultText;
        } catch(IndexOutOfBoundsException e) {
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OutOfBound");
        } catch(TextTranslationFailException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed chat message has been recorded.\n", e);
            return new SimpleText(inputText, null).text();
        }
        return inputText;
    }

    protected Matcher createMatcher(Text text, Pattern regex) {
        return regex.matcher(text.getString());
    }

    /**
     * Get player name from sibling.
     * <p>
     * This method detects sibling would contain Custom nickname (Champion Feature) or not.
     * @param index Index of sibling contains Custom Nickname form text
     * @return Custom nickname or Original name as {@code Text}
     */
    protected Text getPlayerNameFromSibling(int index) {
        String name = getContentString(index);
        if("".equals(name)) {
            return getSibling(index).getSiblings().getFirst();
        }
        else {
            return getSibling(index);
        }
    }

    protected static Text removeCustomNicknameFromDialog(Text text) {
        MutableText newText = text.copyContentOnly().setStyle(text.getStyle());
        newText.append(text.getSiblings().get(0));
        String partial = "";
        Style style = text.getSiblings().get(1).getStyle();
        for(int index = 1; text.getSiblings().size() > index; index++) {
            Text sibling = text.getSiblings().get(index);
            if(!sibling.getContent().equals(PlainTextContent.EMPTY)) {
                if(style.equals(sibling.getStyle())) {
                    partial = partial.concat(sibling.getString());
                }
                else {
                    newText.append(Text.literal(partial).setStyle(style));
                    partial = sibling.getString();
                    style = sibling.getStyle();
                }
            }
            else {
                removedCustomNickname = sibling.copy();
                if(style.equals(sibling.getStyle())) {
                    partial = partial.concat("%1$s");
                }
                else {
                    newText.append(Text.literal(partial).setStyle(style));
                    partial = "%1$s";
                    style = sibling.getStyle();
                }
            }
        }
        newText.append(Text.literal(partial).setStyle(style));

        return newText;
    }
}
