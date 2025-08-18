package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FriendRecruit extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.equals("Recruit a Friend")) return false;
        if(texts.getLast().getString().matches("§7§.[✔✖] §7Recruit Players: §f\\d§7/\\d")) return true;
        return texts.getFirst().getString().matches("§a§lRecruit a Friend|§b§lView Recruited Friends");
    }

    public FriendRecruit(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String string = getSibling(0).getString();
        if(string.equals("Recruit a Friend")) {
            buildRecruitInfo();
        }
        else if(string.equals("View Recruited Friends")) {
            buildRecruitedFriends();
        }
        else if(getSibling(-1).getString().contains(" Recruit Players: ")) {
            buildRewards();
        }
        else {
            resultText = inputText;
        }
    }

    private void buildRecruitInfo() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Recruit a Friend")) {
                resultText.append(Text.translatable("wytr.tooltip.compassItems.recruit")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.info.desc1").setStyle(GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("Friends can only be recruited if:")) {
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.info.desc2").formatted(Formatting.AQUA), 500)
                        .forEach(resultText::append);
            }
            else if(_string.contains("You and your recruitees will gain")) {
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.info.desc3").setStyle(GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("Click to recruit a friend!")) {
                resultText.append(Text.translatable("wytr.tooltip.friendRecruit.info.click").formatted(Formatting.GREEN));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildRecruitedFriends() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("View Recruited Friends")) {
                resultText.append(Text.translatable("wytr.tooltip.friendRecruit.state")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.state.desc").setStyle(GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Recruited Friends: ")) {
                resultText.append(Text.translatable("wytr.tooltip.friendRecruit.state.list")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle())
                        .append(seg.getFirst().getSiblings().getLast()));
                seg.subList(1, seg.size()).forEach(t -> {
                    MutableText line = Text.empty();
                    line.append(t.getSiblings().getFirst());
                    if(!t.getString().contains("recruited anyone")) line.append(t.getSiblings().get(1));
                    Text status = t.getSiblings().getLast();
                    if(status.getString().contains("Pending"))
                        line.append(" ").append(Text.translatable("wytr.tooltip.friendRecruit.state.pending").setStyle(status.getStyle()));
                    else if(status.getString().contains("Accepted"))
                        line.append(" ").append(Text.translatable("wytr.tooltip.friendRecruit.state.accepted").setStyle(status.getStyle()));
                    else if(status.getString().contains("Rewarded"))
                        line.append(" ").append(Text.translatable("wytr.tooltip.friendRecruit.state.rewarded").setStyle(status.getStyle()));
                    else if(status.getString().contains("anyone"))
                        line.append(Text.translatable("wytr.tooltip.friendRecruit.state.anyone").setStyle(status.getStyle()));
                    else line.append(status);
                    resultText.append(line);
                });
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildRewards() {
        List<List<Text>> segments = new ArrayList<>();
        segments.add(getSiblings().subList(0, 1));
        splitTooltipToSegments(getSiblings().subList(1, getSiblings().size()), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.contains("Emeralds")) {
                String num = _string.replaceAll("\\D", "");
                resultText.append(Text.translatable("wytr.func.reward.emerald", num).formatted(Formatting.GREEN));
                String players = segments.get(i + 1).getFirst().getString().replaceAll("\\D", "");
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.rewards.emerald.desc", players, num).setStyle(GRAY), 150)
                        .forEach(resultText::append);
                i++;
            }
            else if(_string.contains("Wybeel")) {
                resultText.append(Text.translatable("wytr.tooltip.friendRecruit.rewards.wybeel")
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.rewards.wybeel.desc").setStyle(GRAY), 150)
                        .forEach(resultText::append);
                i++;
            }
            else if(_string.contains("Totems")) {
                String num = _string.replaceAll("\\D", "");
                resultText.append(Text.translatable("wytr.tooltip.friendRecruit.rewards.mobTotem", num)
                        .setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
                String players = segments.get(i + 1).getFirst().getString().replaceAll("\\D", "").replaceFirst(num, "");
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.rewards.mobTotem.desc", players, num).setStyle(GRAY), 150)
                        .forEach(resultText::append);
                i++;
            }
            else if(_string.contains("Horse")) {
                MutableText horseName = Text.translatable("wytr.item.misc." + normalizeStringForKey(_string));
                resultText.append(horseName.setStyle(seg.getFirst().getSiblings().getFirst().getStyle()));
                String players = segments.get(i + 1).getFirst().getString().replaceAll("\\D", "");
                wrapLine(Text.translatable("wytr.tooltip.friendRecruit.rewards.horse.desc", players, horseName.getString()).setStyle(GRAY), 150)
                        .forEach(resultText::append);
                i++;
            }
            else if(_string.contains("Recruit Players: ")) {
                Deque<Text> siblings = new ArrayDeque<>(seg.getFirst().getSiblings());
                MutableText line = siblings.removeFirst().copy();
                line.append(Text.translatable("wytr.tooltip.friendRecruit.rewards.counter").setStyle(GRAY));
                siblings.removeFirst();
                siblings.forEach(line::append);
                resultText.append(line);
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }
}
