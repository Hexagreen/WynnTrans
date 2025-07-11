package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.HousingEnter;
import net.minecraft.text.Text;

public class HousingEnterGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return text.getString().matches(" +§d§l.+");
    }

    public HousingEnterGlue() {
        super(HousingEnter::new);
    }

    @Override
    public boolean push(Text text) {
        switch(count) {
            case 0, 1, 3 -> {
                if(count == 1 && !text.getString().matches(" +§7by §a.+")) {
                    pop();
                    return false;
                }
                if(count == 3 && !text.getString().matches(" +§7.+")) {
                    pop();
                    return false;
                }
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }
            case 2, 4 -> {
                if(text.getString().equals("À")) {
                    resetTimer();
                    gluedText.append(text);
                    if(count++ == 4) {
                        safeNow();
                        pop();
                    }
                    return true;
                }
                else {
                    pop();
                    return false;
                }
            }
        }
        return false;
    }
}
