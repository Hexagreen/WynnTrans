package net.hexagreen.wynntrans.entity.living;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class OnEntityTrackerUpdateHandler {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final int packetID;
    private final List<DataTracker.SerializedEntry<?>> packetData;

    public OnEntityTrackerUpdateHandler(EntityTrackerUpdateS2CPacket packet) {
        this.packetID = packet.id();
        this.packetData = packet.trackedValues();
        this.run();
    }

    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    private void run(){
        Entity entity = client.world.getEntityById(packetID);
        if(entity == null) return;

        for(DataTracker.SerializedEntry<?> packetDatum : packetData) {
            if(packetDatum.id() == Entity.CUSTOM_NAME.getId()) {
                Optional<Text> optionalNewName = (Optional<Text>) packetDatum.value();
                if(optionalNewName.isEmpty()) return;

                Text newName = optionalNewName.get();
//                if(newName.getString().equals("TranslateThis")) {
//                    entity.setCustomName(Text.literal("번역완료"));
//                }

//                String out = entity.getClass().getSimpleName() + " :: " + newName.getString();
//                debugClass.writeString2File(out, "Entity.txt");
            }
        }
    }
}
