package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.NpcDialogSelection;
import net.hexagreen.wynntrans.enums.ChatType;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.minecraft.text.Text;

public class Selection extends TextGlue {
    private boolean ready;

    protected Selection() {
        super(ChatType.DIALOG_NORMAL.getRegex());
        this.ready = false;
        register(this);
    }

    public static Selection get() {
        return new Selection();
    }

    @Override
    public boolean push(Text text) {
        if(this.ready){
            for(Text sibling : text.getSiblings()) {
                this.gluedDialog.append(sibling);
            }
            if(FunctionalRegex.SELECTION_END.match(text)) {
                this.ready = false;
                pop(NpcDialogSelection.class);
            }
            else{
                this.gluedDialog.append(Text.of("\n"));
            }
        }
        else {
            if(FunctionalRegex.SELECTION_END.match(text)){
                this.ready = true;
            }
        }
        return true;
    }
}
