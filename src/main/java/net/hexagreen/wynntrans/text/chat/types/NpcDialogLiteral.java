package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ChatType;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
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

    public NpcDialogLiteral(Text text, Pattern regex) {
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

    @Override
    public void build(){
        MutableText reformed = Text.literal("[" + dialogIdx + "/" + dialogLen + "] " ).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        reformed.append(Text.literal(npcName).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)))
                .append(dialog);
        resultText = new NpcDialog(reformed, ChatType.DIALOG_NORMAL.getRegex()).text();
    }
}
