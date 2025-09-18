package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgeryMessage extends WynnChatText {
    private static final Text FORGERY_CHEST = Text.translatable("wytr.func.forgery").formatted(Formatting.GOLD, Formatting.BOLD);
    private static final Pattern TIME_REMAIN = Pattern.compile("(\\d+ hours?)?(?:, )?(?: and )?(\\d+ minutes?)?(?: and )?(\\d+ seconds?)? remaining\\.?");

    public ForgeryMessage(Text text) {
        super(TextGlue.colorCodedToStyledGluedText(text));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.forgery";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        if(glueContext == null) {
            buildComplete();
            return;
        }
        switch((String) glueContext[0]) {
            case "KILL_REQUIRE" -> buildKillRequire();
            case "KILL_FULFILL" -> buildKillFulfill();
            case "TIME_REMAIN" -> buildTimeRemain();
            case "RUN_COUNT" -> buildRunCount();
            case "REWARD_CLAIM" -> buildRewardClaimConfirm();
            default -> buildComplete();
        }
    }

    private void buildKillRequire() {
        resultText.append(FORGERY_CHEST).append("\n");
        Text _num = getSibling(1).getSiblings().get(1);
        Text num = Text.literal(_num.getString().trim()).setStyle(_num.getStyle());
        resultText.append(Text.translatable("wytr.func.forgery.killRequire", num).formatted(Formatting.GRAY));
    }

    private void buildKillFulfill() {
        resultText.append(FORGERY_CHEST).append("\n").append(Text.translatable("wytr.func.forgery.killFulfill").formatted(Formatting.GRAY));
    }

    private void buildTimeRemain() {
        resultText.append(FORGERY_CHEST).append("\n");
        List<Text> line = new ArrayList<>(getSibling(1).getSiblings().reversed());
        List<Text> args = new ArrayList<>();
        for(int i = 1, size = line.size(); i < size; i++) {
            if(i % 2 == 0) args.add(Text.translatable("wytr.func.forgery.timer." + i));
            else args.add(ITime.translateTime(line.get(i)));
        }
        while(args.size() < 5) args.add(Text.literal(""));
        resultText.append(Text.translatable("wytr.func.forgery.timer", args.toArray(Object[]::new)).formatted(Formatting.GRAY));
    }

    private void buildRunCount() {
        resultText.append(FORGERY_CHEST).append("\n");
        Text counter = getCounter(getSibling(1).getSiblings().getFirst());
        resultText.append(Text.translatable("wytr.func.forgery.runCount", counter).formatted(Formatting.GRAY)).append("\n\n");
        Text line3 = getSibling(3).getSiblings().getFirst();
        if(line3.getString().contains("Timer enabled")) {
            resultText.append(Text.translatable("wytr.func.forgery.timer.enable").setStyle(line3.getStyle()));
        }
        else {
            resultText.append(Text.literal("            ").setStyle(line3.getStyle())
                    .append(Text.translatable("wytr.func.forgery.timer.remain")));
        }
        List<Text> line4 = getSibling(4).getSiblings();
        resultText.append("\n").append(line4.getFirst());
        Matcher m = TIME_REMAIN.matcher(line4.getLast().getString());
        List<Text> args = new ArrayList<>();
        if(m.find()) {
            if(m.group(1) != null) args.add(ITime.translateTime(m.group(1)));
            if(m.group(2) != null) args.add(ITime.translateTime(m.group(2)));
            if(m.group(3) != null) args.add(ITime.translateTime(m.group(3)));
            args = args.reversed();
            if(args.size() >= 2) args.add(1, Text.translatable("wytr.func.forgery.timer.2"));
            if(args.size() >= 4) args.add(3, Text.translatable("wytr.func.forgery.timer.4"));
            while(args.size() < 5) args.add(Text.literal(""));
            resultText.append(Text.translatable("wytr.func.forgery.timer", args.toArray(Object[]::new)).setStyle(line4.getLast().getStyle()));
        }
        else resultText.append(line4.getLast());
    }

    private void buildRewardClaimConfirm() {
        resultText.append(FORGERY_CHEST).append("\n");
        Text counter = getCounter(getSibling(1).getSiblings().get(2));
        resultText.append(Text.translatable("wytr.func.forgery.rewardClaim", counter).formatted(Formatting.GRAY)).append("\n\n");
        Text line3 = getSibling(3).getSiblings().getFirst();
        resultText.append(Text.literal("            ").append(Text.translatable("wytr.func.forgery.completedList"))
                .setStyle(line3.getStyle())).append("\n");
        int i = 4;
        for(int size = getSiblings().size(); i < size; i++) {
            if(getSibling(i).getString().isBlank()) break;
            List<Text> siblings = getSibling(i).getSiblings();
            MutableText line = Text.empty().append(siblings.getFirst());
            line.append(Dungeons.getDungeons(siblings.getLast().getString()).getDungeonName().formatted(Formatting.WHITE));
            resultText.append(line).append("\n");
        }
        if(i < getSiblings().size()) {
            resultText.append("\n");
            Text remain = getSibling(i + 1).getSiblings().getFirst();
            resultText.append(Text.literal("            ").setStyle(remain.getStyle())
                    .append(Text.translatable("wytr.func.forgery.timer.remain")));

            List<Text> timer = getSibling(i + 2).getSiblings();
            resultText.append("\n").append(timer.getFirst());
            Matcher m = TIME_REMAIN.matcher(timer.getLast().getString());
            List<Text> args = new ArrayList<>();
            if(m.find()) {
                if(m.group(1) != null) args.add(ITime.translateTime(m.group(1)));
                if(m.group(2) != null) args.add(ITime.translateTime(m.group(2)));
                if(m.group(3) != null) args.add(ITime.translateTime(m.group(3)));
                args = args.reversed();
                if(args.size() >= 2) args.add(1, Text.translatable("wytr.func.forgery.timer.2"));
                if(args.size() >= 4) args.add(3, Text.translatable("wytr.func.forgery.timer.4"));
                while(args.size() < 5) args.add(Text.literal(""));
                resultText.append(Text.translatable("wytr.func.forgery.timer", args.toArray(Object[]::new)).setStyle(timer.getLast().getStyle()));
            }
            else resultText.append(timer.getLast());
        }
    }

    private void buildComplete() {
        resultText.append(Text.translatable("wytr.func.forgery.complete").formatted(Formatting.GOLD, Formatting.BOLD)).append("\n");
        Text _num = getSibling(1).getSiblings().get(1);
        Text num = Text.literal(_num.getString().replaceAll("\\D", "")).setStyle(_num.getStyle());
        resultText.append(Text.translatable("wytr.func.forgery.complete.desc", num).formatted(Formatting.GRAY)).append("\n\n");
        resultText.append(Text.literal("    ").formatted(Formatting.LIGHT_PURPLE).append(Text.translatable("wytr.func.reward")));
        for(int i = 4, size = getSiblings().size(); i < size; i++) {
            resultText.append("\n");
            List<Text> siblings = getSibling(i).getSiblings();
            MutableText line = Text.empty().append(siblings.getFirst());
            if(siblings.getLast().getString().contains("Combat XP")) {
                Text _xp = siblings.get(1);
                Text xp = Text.literal(_xp.getString().trim()).setStyle(_xp.getStyle());
                line.append(Text.translatable("wytr.func.forgery.complete.xp", xp).formatted(Formatting.GRAY));
                resultText.append(line);
            }
            else if(siblings.getLast().getString().contains("Various Items")) {
                resultText.append(line.append(Text.translatable("wytr.func.forgery.complete.items").formatted(Formatting.WHITE)));
            }
            else {
                Text itemName = new ItemName(siblings.getLast()).noAdditionMode().textAsMutable();
                resultText.append(line.append(itemName));
            }
        }
    }

    private Text getCounter(Text text) {
        String[] num = text.getString().replaceAll("[^\\d/]", "").split("/");
        return Text.translatable("wytr.func.forgery.counter", num[0], num[1]).setStyle(text.getStyle());
    }
}
