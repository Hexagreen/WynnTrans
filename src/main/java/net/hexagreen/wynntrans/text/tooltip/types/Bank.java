package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bank extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE00F")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public Bank(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        switch(Items.match(getSibling(0).getString())) {
            case QUICK_ACTION -> buildQuickAction();
            case STORAGE_TYPE -> buildStorageType();
            case PAGE -> buildPage();
            default -> resultText = inputText;
        }
    }

    private void buildQuickAction() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Quick Actions")) {
                resultText.append(Text.translatable("wytr.tooltip.bank.quickAction").formatted(Formatting.LIGHT_PURPLE));
            }
            else if(_string.equals("Stashing moves items from your")) {
                wrapLine(Text.translatable("wytr.tooltip.bank.quickAction.desc1").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("Dumping moves all items from")) {
                wrapLine(Text.translatable("wytr.tooltip.bank.quickAction.desc2").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Left-Click to quick stash")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    if(_s.contains("quick stash")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.bank.quickAction.stash").formatted(Formatting.GREEN)));
                    }
                    else if(_s.contains("dump except hotbar")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.bank.quickAction.dump").formatted(Formatting.GREEN)));
                    }
                    else if(_s.contains("dump all")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.bank.quickAction.dumpAll").formatted(Formatting.GREEN)));
                    }
                    else resultText.append(t);
                }
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildStorageType() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Storage Type")) {
                resultText.append(Text.translatable("wytr.tooltip.bank.type")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
            }
            else if(_string.equals("The character bank is a private")) {
                wrapLine(Text.translatable("wytr.tooltip.bank.type.character.desc").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("The account bank is a private")) {
                wrapLine(Text.translatable("wytr.tooltip.bank.type.account.desc").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("- Character Storage")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    Style style = t.getSiblings().getFirst().getStyle();
                    if(_s.contains("Character Storage")) {
                        resultText.append(Text.empty().setStyle(style).append("- ")
                                .append(Text.translatable("wytr.tooltip.bank.type.character")));
                    }
                    else if(_s.contains("Account Storage")) {
                        resultText.append(Text.empty().setStyle(style).append("- ")
                                .append(Text.translatable("wytr.tooltip.bank.type.account")));
                    }
                    else resultText.append(t);
                }
            }
            else if(_string.contains("Click to switch")) {
                resultText.append(Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.func.clickToSwitch").formatted(Formatting.GREEN)));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildPage() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.matches("Page \\d+ [<>]+")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    if(_s.contains("Page")) {
                        String num = _s.replaceAll("\\D", "");
                        MutableText line = Text.empty();
                        line.append(Text.translatable("wytr.tooltip.bank.page", num).formatted(Formatting.BOLD, Formatting.WHITE));
                        List<Text> sib = seg.getFirst().getSiblings();
                        sib.removeFirst();
                        sib.forEach(line::append);
                        resultText.append(line);
                    }
                    else if(_s.equals("Purchase the page")) {
                        wrapLine(Text.translatable("wytr.tooltip.bank.page.unlock").formatted(Formatting.GRAY), 200)
                                .forEach(resultText::append);
                    }
                }
            }
            else if(_string.equals("Jump to:")) {
                for(Text t : seg) {
                    if(t.getString().equals("Jump to:")) {
                        resultText.append(Text.translatable("wytr.tooltip.bank.page.jump").formatted(Formatting.GRAY));
                    }
                    else {
                        List<Text> siblings = t.getSiblings();
                        Text bar = siblings.getFirst();
                        Text key = siblings.get(1);
                        String num = siblings.getLast().getString().replaceAll("\\D", "");
                        Text line = Text.empty().append(bar).append(key).append(" ")
                                .append(Text.translatable("wytr.tooltip.bank.page", num).formatted(Formatting.DARK_GRAY));
                        resultText.append(line);
                    }
                }
            }
            else if(_string.equals("Price:")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    if(_s.equals("Price:")) {
                        resultText.append(Text.translatable("wytr.func.price").formatted(Formatting.GOLD));
                    }
                    else if(_s.contains("Combat Lv")) {
                        Text check = t.getSiblings().getFirst();
                        Text num = t.getSiblings().getLast();
                        MutableText line = Text.empty().append(check)
                                .append(Text.translatable("wytr.requirement.combat", num).formatted(Formatting.GRAY));
                        resultText.append(line);
                    }
                    else if(_s.contains("Price:")) {
                        Text check = t.getSiblings().getFirst();
                        Text num = t.getSiblings().getLast();
                        MutableText line = Text.empty().append(check)
                                .append(Text.translatable("wytr.func.price").formatted(Formatting.GRAY))
                                .append(" ").append(num);
                        resultText.append(line);
                    }
                    else resultText.append(t);
                }
            }
            else if(_string.contains("Click to go")) {
                resultText.append(Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.func.clickToGo").formatted(Formatting.GREEN)));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private enum Items {
        QUICK_ACTION("§dQuick Actions", "Quick Actions"),
        STORAGE_TYPE("§.Storage Type", "Storage Type"),
        PAGE("§f§lPage \\d+.+", "Page \\d+.+");

        private final String original;
        private final String styled;

        static Items match(String string) {
            return Arrays.stream(values()).parallel()
                    .filter(i -> string.matches(i.original) || string.matches(i.styled))
                    .findAny().orElse(null);
        }

        Items(String original, String styled) {
            this.original = original;
            this.styled = styled;
        }
    }
}
