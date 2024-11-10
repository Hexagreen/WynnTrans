package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.PartyInvited;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PartyInvitedGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("\\n +§eYou have been invited to join .+'s party!").matcher(text.getString()).find();
    }

    public PartyInvitedGlue() {
        super(PartyInvited::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        if(count == 1 && text.getString().contains("Click here to join")) {
            resetTimer();
            safeNow();
            gluedText.append(text);
            pop();
            return true;
        }
        return false;
    }
}
