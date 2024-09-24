package net.hexagreen.wynntrans.text.chat;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

public interface ICommonTooltip {

	default HoverEvent getHoverRunCommand() {
		Text text = Text.translatable("wytr.commonTooltip.runCommand");
		return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
	}
}
