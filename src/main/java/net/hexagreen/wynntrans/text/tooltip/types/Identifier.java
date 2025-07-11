package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class Identifier extends WynnTooltipText implements ITooltipSplitter {
    private final MutableText ICON = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(net.minecraft.util.Identifier.of("minecraft:keybind"))))
            .append(" ");

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.matches(".[\\uE018\\uE019]")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public Identifier(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Items match = Items.match(getSibling(0).getString());
        switch(match) {
            case ITEM_SLOT -> buildItemSlot();
            case GO_TO_AUGMENT -> buildGoToAugment();
            case NEED_IDENTIFY_TARGET -> buildNeedIdentifyTarget();
            case IDENTIFY_LIST -> buildIdentify();
            case IDENTIFY_CONFIRM -> buildWithdraw();
            case NEED_ITEM -> buildNeedItem();
            case NEED_AUGMENT -> buildNeedAugment();
            case AUGMENT_SLOT -> buildAugmentSlot();
        }
    }

    private void buildItemSlot() {
        resultText.append(Text.translatable("wytr.tooltip.ui.emptyItemSlot").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.ui.emptyItemSlot.identify").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildGoToAugment() {
        resultText.append(Text.translatable("wytr.tooltip.identifier.augment").formatted(Formatting.AQUA, Formatting.BOLD));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.identifier.augment.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(ICON.append(Text.translatable("wytr.func.clickToOpen").formatted(Formatting.GREEN)));
    }

    private void buildNeedIdentifyTarget() {
        resultText.append(Text.translatable("wytr.tooltip.identifier.identifying.wait").formatted(Formatting.GRAY, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.identifier.identifying.wait.desc").formatted(Formatting.DARK_GRAY), 200)
                .forEach(resultText::append);
    }

    private void buildIdentify() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        segments.forEach(seg -> {
            String _string = seg.getFirst().getString();
            if(_string.equals("You are identifying")) {
                resultText.append(Text.translatable("wytr.tooltip.identifier.identifying.ready")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
            }
            else if(_string.matches("^- .+|Identifying")) {
                for(Text text : seg) {
                    if(text.getString().equals("Identifying")) {
                        resultText.append(Text.translatable("wytr.tooltip.identifier.identifying.targets").formatted(Formatting.GRAY));
                    }
                    else {
                        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());
                        Text _lastText = siblings.removeLast();
                        String[] split = _lastText.getString().split(" (?=\\[\\d+])");
                        Text lastText = Text.literal(split[0]).setStyle(_lastText.getStyle());
                        siblings.addLast(lastText);
                        MutableText line = Text.empty();
                        line.append(siblings.removeFirst());
                        MutableText _itemName = Text.empty();
                        siblings.forEach(_itemName::append);
                        Text itemName = new ItemName(_itemName).noAddictionMode().textAsMutable();
                        line.append(itemName);
                        if(split.length == 2) {
                            line.append(Text.literal(split[1]).setStyle(_lastText.getStyle()));
                        }
                        resultText.append(line);
                    }
                }
            }
            else if(_string.equals("Augments")) {
                for(Text text : seg) {
                    if(text.getString().equals("Augments")) {
                        resultText.append(Text.translatable("wytr.tooltip.identifier.identifying.augments").formatted(Formatting.GRAY));
                    }
                    else {
                        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());
                        MutableText line = Text.empty();
                        line.append(siblings.removeFirst());
                        Text itemName = new ItemName(siblings.getFirst()).noAddictionMode().textAsMutable();
                        line.append(itemName);
                        resultText.append(line);
                    }
                }
            }
            else if(_string.matches("^Price: .+")) {
                List<Text> siblings = new ArrayList<>(seg.getFirst().getSiblings());
                MutableText price = Text.empty();
                siblings.subList(1, siblings.size()).forEach(price::append);
                resultText.append(Text.translatable("wytr.tooltip.identifier.identifying.price", price).formatted(Formatting.GRAY));
            }
            else if(_string.contains("\uE000 Click to")) {
                resultText.append(ICON.append(Text.translatable("wytr.func.clickToConfirm").formatted(Formatting.YELLOW)));
                return;
            }
            else if(_string.equals("Cannot afford to identify")) {
                resultText.append(ICON.append(Text.translatable("wytr.tooltip.identifier.identifying.block").formatted(Formatting.RED)));
                return;
            }
            else seg.forEach(resultText::append);
            resultText.append(SPLITTER);
        });
    }

    private void buildWithdraw() {
        resultText.append(Text.translatable("wytr.tooltip.identifier.withdraw").formatted(Formatting.GREEN, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.identifier.withdraw.desc").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildNeedItem() {
        resultText.append(Text.translatable("wytr.tooltip.identifier.waitItem").formatted(Formatting.GRAY, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.identifier.waitItem.desc").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildNeedAugment() {
        resultText.append(Text.translatable("wytr.tooltip.identifier.waitAugment").formatted(Formatting.GRAY, Formatting.BOLD));
        wrapLine(Text.translatable("wytr.tooltip.identifier.waitAugment.desc").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private void buildAugmentSlot() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        boolean[] addSplitter = {false};
        segments.forEach(seg -> {
            if(addSplitter[0]) resultText.append(SPLITTER);
            addSplitter[0] = true;
            String _string = seg.getFirst().getString();
            switch(_string) {
                case "Empty Augment Slot" ->
                        resultText.append(Text.translatable("wytr.tooltip.identifier.emptyAugmentSlot").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
                case "Add an item to apply" ->
                        wrapLine(Text.translatable("wytr.tooltip.identifier.emptyAugmentSlot.desc1").formatted(Formatting.DARK_GRAY), 150)
                                .forEach(resultText::append);
                case "Click an augment to equip" ->
                        wrapLine(Text.translatable("wytr.tooltip.identifier.emptyAugmentSlot.desc2").formatted(Formatting.DARK_GRAY), 150)
                                .forEach(resultText::append);
                case "Compatible Augments:" -> {
                    for(Text text : seg) {
                        String _s = text.getString();
                        String k;
                        switch(_s) {
                            case "- Simulator" -> k = "wytr.tooltip.identifier.augments.sim";
                            case "- Insulator" -> k = "wytr.tooltip.identifier.augments.ins";
                            case "- Amplifier" -> k = "wytr.tooltip.identifier.augments.amp";
                            case "- No remaining compatible" -> {
                                wrapLine(Text.translatable("wytr.tooltip.identifier.augments.no").formatted(Formatting.GRAY), 150)
                                        .forEach(resultText::append);
                                return;
                            }
                            default -> {
                                resultText.append(Text.translatable("wytr.tooltip.identifier.augments").formatted(Formatting.DARK_GRAY));
                                continue;
                            }
                        }
                        resultText.append(Text.translatable(k).formatted(Formatting.GRAY));
                    }
                }
                default -> seg.forEach(resultText::append);
            }
        });
    }

    private enum Items {
        ITEM_SLOT("§8§lEmpty Item Slot", "Empty Item Slot"),
        GO_TO_AUGMENT("§b§lCorkian Augments", "Corkian Augments"),
        NEED_IDENTIFY_TARGET("Add items to identify", "Add items to identify"),
        IDENTIFY_LIST("You are identifying", "You are identifying"),
        IDENTIFY_CONFIRM("Withdraw Items", "Withdraw Items"),
        NEED_ITEM("Waiting for an item", "Waiting for an item"),
        NEED_AUGMENT("Waiting for an Augment", "Waiting for an Augment"),
        AUGMENT_SLOT("Empty Augment Slot", "Empty Augment Slot");

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
