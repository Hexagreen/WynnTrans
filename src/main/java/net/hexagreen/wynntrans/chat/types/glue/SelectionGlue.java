package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.NarrationFocused;
import net.hexagreen.wynntrans.chat.NpcDialogFocused;
import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.enums.ChatType;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.minecraft.text.Text;

public class SelectionGlue extends TextGlue {
    private boolean ready;

    public SelectionGlue() {
        super(ChatType.DIALOG_NORMAL.getRegex(), NpcDialogFocused.class);
        this.ready = false;
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
                if(!ChatType.DIALOG_NORMAL.match(gluedText, 2)) {
                    changeWct(NarrationFocused.class);
                }
                pop();
                return true;
            }
            else {
                resetTimer();
                this.gluedText.append(Text.of("\n"));
            }
        }
        else {
            if(FunctionalRegex.SELECTION_END.match(text)) {
                this.ready = true;
            }
        }
        return true;
    }
}
