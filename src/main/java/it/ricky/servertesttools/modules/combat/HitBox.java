package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class HitBox extends Module {
    public static final float EXPAND = 0.25f;

    public HitBox() {
        super("HitBox", Category.COMBAT);
    }

    public static Box expand(Entity entity) {
        return entity.getBoundingBox().expand(EXPAND);
    }

    public static boolean active() {
        Module m = it.ricky.servertesttools.modules.ModuleManager.getByName("HitBox");
        return m != null && m.isEnabled();
    }
}
