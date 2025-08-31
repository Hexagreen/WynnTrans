package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.Elements;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowderMaster extends WynnTooltipText implements ITooltipSplitter {
    private final Screen screen;
    private final MutableText ICON = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("minecraft:keybind"))))
            .append(" ");

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.matches(".[\\uE01A\\uE01B\\uE01C]")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public PowderMaster(List<Text> texts) {
        super(texts);
        String scr = WynnTrans.currentScreen;
        this.screen = scr.contains("\uE01A") ? Screen.INFUSE : scr.contains("\uE01B") ? Screen.REMOVE : Screen.UPGRADE;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Items match = Items.match(getSibling(0).getString());
        switch(match) {
            case MENU -> buildMenu();
            case ITEM_SLOT -> buildItemSlot();
            case POWDER_SLOT -> buildPowderSlot();
            case WAITING_ITEM -> buildItemWait();
            case WAITING_POWDER -> buildPowderWait();
            case NO_SOCKET -> buildNoSocket();
            case NO_POWDER -> buildNoPowder();
            case PROCESS -> buildProcess();
        }
    }

    private void buildItemSlot() {
        resultText.append(Text.translatable("wytr.tooltip.ui.emptyItemSlot").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        resultText.append(SPLITTER);
        String descKey = screen == Screen.INFUSE ? "wytr.tooltip.ui.emptyItemSlot.powderInfuse" : "wytr.tooltip.ui.emptyItemSlot.powderRemove";
        wrapLine(Text.translatable(descKey).formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildPowderSlot() {
        resultText.append(Text.translatable("wytr.tooltip.powderMaster.emptyPowderSlot").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        resultText.append(SPLITTER);
        String descKey = screen == Screen.INFUSE ? "wytr.tooltip.powderMaster.emptyPowderSlot.infuse" : "wytr.tooltip.powderMaster.emptyPowderSlot.upgrade";
        wrapLine(Text.translatable(descKey).formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildItemWait() {
        resultText.append(Text.translatable("wytr.tooltip.powderMaster.waitItem").formatted(Formatting.GRAY, Formatting.BOLD));
        String descKey = screen == Screen.INFUSE ? "wytr.tooltip.powderMaster.waitItem.infuse" : "wytr.tooltip.powderMaster.waitItem.remove";
        wrapLine(Text.translatable(descKey).formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildPowderWait() {
        resultText.append(Text.translatable("wytr.tooltip.powderMaster.waitPowder").formatted(Formatting.GRAY, Formatting.BOLD));
        String descKey = screen == Screen.INFUSE ? "wytr.tooltip.powderMaster.waitPowder.infuse" : "wytr.tooltip.powderMaster.waitPowder.upgrade";
        wrapLine(Text.translatable(descKey).formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildNoSocket() {
        resultText.append(Text.translatable("wytr.tooltip.powderMaster.noSocket").formatted(Formatting.RED, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.powderMaster.noSocket.desc").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildNoPowder() {
        resultText.append(Text.translatable("wytr.tooltip.powderMaster.noPowder").formatted(Formatting.RED, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.powderMaster.noPowder").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildMenu() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        String menuKey = "wytr.tooltip.powderMaster.infuse";
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(i == 0) {
                if(_string.contains("Infuse")) menuKey = "wytr.tooltip.powderMaster.infuse";
                else if(_string.contains("Remove")) menuKey = "wytr.tooltip.powderMaster.remove";
                else if(_string.contains("Upgrade")) menuKey = "wytr.tooltip.powderMaster.upgrade";
                resultText.append(Text.translatable(menuKey).setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
            }
            else if(_string.matches("(Infuse powders|Remove all|Merge powders).+")) {
                wrapLine(Text.translatable(menuKey + ".desc").formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Click to Open")) {
                resultText.append(ICON.append(Text.translatable("wytr.func.clickToOpen").formatted(Formatting.GREEN)));
            }
            else if(_string.equals("Currently Open")) {
                resultText.append(ICON.append(Text.translatable("wytr.tooltip.powderMaster.menuSelected").formatted(Formatting.YELLOW)));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildProcess() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        String menuKey = switch(screen) {
            case INFUSE -> "wytr.tooltip.powderMaster.infusing";
            case REMOVE -> "wytr.tooltip.powderMaster.removing";
            case UPGRADE -> "wytr.tooltip.powderMaster.upgrading";
        };
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(i == 0) {
                resultText.append(Text.translatable(menuKey).setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
            }
            else if(_string.equals("All removed powders will be")) {
                wrapLine(Text.translatable("wytr.tooltip.powderMaster.removing.desc").formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Cannot afford to")) {
                resultText.append(Text.translatable(menuKey + ".block").formatted(Formatting.RED));
            }
            else if(_string.equals("Not enough powders")) {
                resultText.append(Text.translatable("wytr.tooltip.powderMaster.upgrading.notEnough").formatted(Formatting.RED));
            }
            else if(_string.contains("Click to")) {
                for(Text line : seg) {
                    String _str = line.getString();
                    Text icon = line.getSiblings().getFirst();
                    String subKey = _str.contains("once") ? ".once" : _str.contains("all") ? ".bulk" : ".run";
                    String num = line.getSiblings().getLast().getString().replaceAll("\\D", "");
                    resultText.append(Text.empty().append(icon).append(" ").append(Text.translatable(menuKey + subKey, num).formatted(Formatting.GREEN)));
                }
            }
            else if(_string.equals("Infusing") || _string.equals("Cleaning")) {
                Text segTitle = seg.removeFirst();
                if(segTitle.getString().equals("Infusing"))
                    resultText.append(Text.translatable("wytr.tooltip.powderMaster.process.infusing").formatted(Formatting.GRAY));
                else
                    resultText.append(Text.translatable("wytr.tooltip.powderMaster.process.cleaning").formatted(Formatting.GRAY));
                for(Text line : seg) {
                    Text bar = line.getSiblings().getFirst();
                    Text item = new ItemName(line.getSiblings().getLast()).textAsMutable();
                    resultText.append(Text.empty().append(bar).append(item));
                }
            }
            else if(_string.equals("Powders") || _string.equals("Result")) {
                Text segTitle = seg.removeFirst();
                if(segTitle.getString().equals("Powders"))
                    resultText.append(Text.translatable("wytr.tooltip.powderMaster.process.powders").formatted(Formatting.GRAY));
                else
                    resultText.append(Text.translatable("wytr.tooltip.powderMaster.process.result").formatted(Formatting.GRAY));
                for(Text line : seg) {
                    Matcher m = Pattern.compile("- (\\d+)([x%]) (?:. )?(Earth|Thunder|Water|Fire|Air) ([IV]+)").matcher(line.getString());
                    if(m.find()) {
                        Text bar = line.getSiblings().getFirst();
                        String num = m.group(1);
                        String key = "wytr.tooltip.powderMaster.process.powders" + (m.group(2).equals("x") ? ".quantity" : ".probability");
                        Text element = Elements.findElement(m.group(3)).getElementWithIcon().append(" " + m.group(4));
                        resultText.append(Text.empty().append(bar).append(Text.translatable(key, num, element).formatted(Formatting.DARK_GRAY)));
                    }
                    else resultText.append(line);
                }
            }
            else if(_string.contains("Price: ")) {
                MutableText price = Text.empty().formatted(Formatting.GRAY)
                        .append(Text.translatable("wytr.func.price")).append(": ");
                List<Text> siblings = seg.getFirst().getSiblings();
                siblings.subList(1, siblings.size()).forEach(price::append);
                resultText.append(price);
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private enum Screen {INFUSE, REMOVE, UPGRADE}

    private enum Items {
        MENU("(Infuse|Remove|Upgrade) Powders"),
        ITEM_SLOT("Empty Item Slot"),
        POWDER_SLOT("Empty Powder Slot"),
        WAITING_ITEM("Waiting for an item"),
        WAITING_POWDER("Waiting for powders"),
        NO_SOCKET("No empty sockets"),
        NO_POWDER("No powders to remove"),
        PROCESS("You are (infusing|cleaning|upgrading)");

        private final Pattern regex;

        static Items match(String string) {
            String finalString = string.replaceAll("§.", "");
            return Arrays.stream(values())
                    .filter(i -> i.regex.matcher(finalString).matches())
                    .findAny().orElse(null);
        }

        Items(String regex) {
            this.regex = Pattern.compile(regex);
        }
    }
}
