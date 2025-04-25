package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.hexagreen.wynntrans.text.chat.ChatType;
import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.NarrationFocused;
import net.hexagreen.wynntrans.text.chat.types.NpcDialogFocused;
import net.minecraft.text.Text;

public class SelectionGlue extends TextGlue {

    public SelectionGlue() {
        super(NpcDialogFocused::new);
    }

    @Override
    public boolean push(Text text) {
        if(FunctionalRegex.SELECTION_END.match(text) || FunctionalRegex.SELECTION_OPTION.match(text)) {
            for(Text sibling : text.getSiblings()) {
                resetTimer();
                this.gluedText.append(sibling);
            }
            if(FunctionalRegex.SELECTION_END.match(text)) {
                safeNow();
                if(!ChatType.DIALOG_NORMAL.match(gluedText, 2)) {
                    changeWct(NarrationFocused::new);
                }
                pop();
                return true;
            }
            else {
                resetTimer();
                this.gluedText.append(Text.of("\n"));
            }
        }
        return true;
    }
}
