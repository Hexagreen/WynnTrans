package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

public class Blacksmith extends WynnTooltipText implements ITooltipSplitter {
    private final boolean isRepairScreen;
    private final MutableText ICON = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("minecraft:keybind"))))
            .append(" ");

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.matches(".[\\uE016\\uE017]")) return false;
        return Items.match(texts.getFirst().getString()) != null
                || Items.REPAIR_CONFIRM.itemMatch(texts.getLast().getString())
                || Items.REPAIR_OVERCOST.itemMatch(texts.getLast().getString());
    }

    public Blacksmith(List<Text> texts) {
        super(texts);
        this.isRepairScreen = WynnTrans.currentScreen.contains("\uE017");
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Items match = Items.match(getSibling(0).getString());
        if(match == null) {
            buildRepairing();
        }
        else if(match == Items.ITEM_SLOT) {
            buildItemSlot();
        }
        else if(match == Items.GO_TO_REPAIR) {
            buildGoToRepair();
        }
        else if(match == Items.GO_TO_SELL) {
            buildGoToSell();
        }
        else if(match == Items.NEED_SELL_TARGET) {
            buildNeedSellTarget();
        }
        else if(match == Items.SELLING_LIST || match == Items.SELLING_CONFIRM) {
            buildSelling();
        }
    }

    private void buildItemSlot() {
        resultText.append(Text.translatable("wytr.tooltip.ui.emptyItemSlot").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        resultText.append(SPLITTER);
        String descKey = isRepairScreen ? "wytr.tooltip.ui.emptyItemSlot.repair" : "wytr.tooltip.ui.emptyItemSlot.sell";
        wrapLine(Text.translatable(descKey).formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildGoToRepair() {
        resultText.append(Text.translatable("wytr.tooltip.blacksmith.repair").formatted(Formatting.GOLD, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.blacksmith.repair.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(ICON.append(Text.translatable("wytr.func.clickToSwitch").formatted(Formatting.GREEN)));
    }

    private void buildGoToSell() {
        resultText.append(Text.translatable("wytr.tooltip.blacksmith.sell").formatted(Formatting.GREEN, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.blacksmith.sell.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(ICON.append(Text.translatable("wytr.func.clickToSwitch").formatted(Formatting.GREEN)));
    }

    private void buildNeedSellTarget() {
        resultText.append(Text.translatable("wytr.tooltip.blacksmith.selling.wait").formatted(Formatting.GRAY, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.blacksmith.selling.wait.desc").formatted(Formatting.DARK_GRAY), 200)
                .forEach(resultText::append);
    }

    private void buildSelling() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        segments.forEach(seg -> {
            String _string = seg.getFirst().getString();
            if(_string.equals("You are selling")) {
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.selling.ready").formatted(Formatting.YELLOW, Formatting.BOLD));
            }
            else if(_string.equals("Confirm Selling")) {
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.selling.confirm").formatted(Formatting.GREEN, Formatting.BOLD));
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.selling.confirm.desc").formatted(Formatting.DARK_GRAY));
            }
            else if(_string.matches("^- .+")) {
                for(Text text : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());
                    Text _lastText = siblings.removeLast();
                    String[] split = _lastText.getString().split(" (?=\\[\\d+])");
                    Text lastText = Text.literal(split[0]).setStyle(_lastText.getStyle());
                    Text num = Text.literal(split[1]).setStyle(_lastText.getStyle());
                    siblings.addLast(lastText);
                    MutableText line = Text.empty();
                    line.append(siblings.removeFirst());
                    MutableText _itemName = Text.empty();
                    siblings.forEach(_itemName::append);
                    Text itemName = new ItemName(_itemName).noAddictionMode().textAsMutable();
                    line.append(itemName).append(" ").append(num);
                    resultText.append(line);
                }
            }
            else if(_string.matches("^Expected Price: .+")) {
                List<Text> siblings = new ArrayList<>(seg.getFirst().getSiblings());
                Text num1 = siblings.get(1), num2 = siblings.get(3);
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.selling.price", num1, num2).formatted(Formatting.GRAY));
            }
            else if(_string.contains("\uE000 Click to")) {
                resultText.append(ICON.append(Text.translatable("wytr.func.clickToConfirm").formatted(Formatting.YELLOW)));
                return;
            }
            else if(_string.contains("\uE000 Click again to")) {
                resultText.append(ICON.append(Text.translatable("wytr.func.clickAgainToConfirm").formatted(Formatting.GREEN)));
                return;
            }
            else seg.forEach(resultText::append);
            resultText.append(SPLITTER);
        });
    }

    private void buildRepairing() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(i == 0) {
                Text _itemName = seg.getFirst();
                Text itemName = new ItemName(_itemName).noAddictionMode().textAsMutable();
                String[] nums = seg.getLast().getString().replaceAll("[\\[\\]A-z ]", "").split("/");
                Text durability = Text.translatable("wytr.tooltip.equipment.durability", nums[0], nums[1]).formatted(Formatting.DARK_GRAY);
                resultText.append(itemName);
                resultText.append(durability);
            }
            else if(_string.equals("Repair Cost")) {
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.repairing.cost").formatted(Formatting.YELLOW));
                List<Text> siblings = seg.getLast().getSiblings();
                MutableText line = Text.empty().append(siblings.getFirst());
                Text cost = siblings.getLast();
                String current = siblings.get(1).getString().contains("Emerald") ? "wytr.tooltip.blacksmith.repairing.cost.emerald" : "wytr.tooltip.blacksmith.repairing.cost.scrap";
                line.append(Text.translatable(current, cost).formatted(Formatting.GRAY));
                resultText.append(line);
            }
            else if(_string.contains("Click to repair")) {
                resultText.append(ICON.append(Text.translatable("wytr.tooltip.blacksmith.repairing.click").formatted(Formatting.GREEN)));
                return;
            }
            else if(_string.equals("Cannot afford to repair")) {
                resultText.append(Text.translatable("wytr.tooltip.blacksmith.repairing.block").formatted(Formatting.RED));
                return;
            }
            else seg.forEach(resultText::append);
            resultText.append(SPLITTER);
        }
    }

    private enum Items {
        ITEM_SLOT("§8§lEmpty Item Slot", "Empty Item Slot"),
        GO_TO_REPAIR("Repair Items", "Repair Items"),
        GO_TO_SELL("Sell Items", "Sell Items"),
        NEED_SELL_TARGET("Add items to sell them", "Add items to sell them"),
        SELLING_LIST("§e§lYou are selling", "You are selling"),
        SELLING_CONFIRM("§a§lConfirm Selling", "Confirm Selling"),
        REPAIR_CONFIRM("§7\uE000 Click to repair", "\uE000 Click to repair"),
        REPAIR_OVERCOST("§7Cannot afford to repair", "Cannot afford to repair");

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

        boolean itemMatch(String string) {
            return string.matches(this.original) || string.matches(this.styled);
        }
    }
}
