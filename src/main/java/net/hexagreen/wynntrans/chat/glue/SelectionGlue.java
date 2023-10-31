package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.NpcDialogSelection;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.enums.ChatType;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SelectionGlue extends TextGlue {
    private boolean ready;

    protected SelectionGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
        this.ready = false;
    }

    public static SelectionGlue get() {
        return new SelectionGlue(ChatType.DIALOG_NORMAL.getRegex(), NpcDialogSelection.class);
    }

    @Override
    public boolean push(Text text) {
        if(this.ready && (FunctionalRegex.SELECTION_END.match(text) || FunctionalRegex.SELECTION_OPTION.match(text))){
            for(Text sibling : text.getSiblings()) {
                resetTimer();
                this.gluedText.append(sibling);
            }
            if(FunctionalRegex.SELECTION_END.match(text)) {
                safeNow();
                pop();
                return true;
            }
            else{
                resetTimer();
                this.gluedText.append(Text.of("\n"));
            }
        }
        else {
            if(FunctionalRegex.SELECTION_END.match(text)){
                this.ready = true;
            }
        }
        return true;
    }

    @Override
    public void timer() {}
}
