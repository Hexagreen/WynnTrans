package net.hexagreen.wynntrans.entity;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class EntityTrackerUpdateHandler {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final int packetID;
    private final List<DataTracker.SerializedEntry<?>> packetData;

    public EntityTrackerUpdateHandler(EntityTrackerUpdateS2CPacket packet) {
        this.packetID = packet.id();
        this.packetData = packet.trackedValues();
        this.run();
    }

    private void run(){
        Entity entity = client.world.getEntityById(packetID);
        if(entity == null) return;

        for(DataTracker.SerializedEntry<?> packetDatum : packetData) {
            if(packetDatum.id() == Entity.CUSTOM_NAME.getId()) {
                Optional<Text> optionalNewName = (Optional<Text>) packetDatum.value();
                if(optionalNewName.isEmpty()) return;

                Text newName = optionalNewName.get();
                if(newName.equals(entity.getCustomName())) return;

                //debugClass.writeTextAsJSON(newName, "Entity");
            }
        }
    }
}
