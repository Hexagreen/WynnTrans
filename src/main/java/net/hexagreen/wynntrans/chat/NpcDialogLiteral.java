package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.enums.ChatType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class NpcDialogLiteral extends WynnChatText {
    private final String dialogIdx;
    private final String dialogLen;
    private final String npcName;
    private final String dialog;

    protected NpcDialogLiteral(Text text, Pattern regex) {
        super(text, regex);
        this.dialogIdx = matcher.group(1);
        this.dialogLen = matcher.group(2);
        this.npcName = matcher.group(3);
        this.dialog = matcher.group(4);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    public static NpcDialogLiteral of(Text text, Pattern regex) {
        return new NpcDialogLiteral(text, regex);
    }

    @Override
    public void build(){
        MutableText reformed = Text.literal("[" + dialogIdx + "/" + dialogLen + "] " ).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        reformed.append(Text.literal(npcName).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)))
                .append(dialog);
        resultText = NpcDialog.of(reformed, ChatType.DIALOG_NORMAL.getRegex()).text();
    }
}
