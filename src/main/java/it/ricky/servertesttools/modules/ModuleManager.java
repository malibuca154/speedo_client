package it.ricky.servertesttools.modules;

import it.ricky.servertesttools.modules.combat.*;
import it.ricky.servertesttools.modules.misc.*;
import it.ricky.servertesttools.modules.render.*;
import it.ricky.servertesttools.modules.client.*;
import it.ricky.servertesttools.modules.donut.*;
import it.ricky.servertesttools.modules.basefinding.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    static {
        // ===== COMBAT =====
        register(new TriggerBot());
        register(new NoHitDelay());
        register(new HitBox());
        register(new AimAssist());
        register(new AutoTotem());
        register(new AutoCrystal());
        register(new CrystalOptimizer());
        register(new AutoJumpReset());
        register(new ShieldBreaker());
        register(new AnchorMacro());
        register(new AutoDoubleHand());
        register(new AutoHitCrystal());
        register(new AutoInvTotem());
        register(new DoubleAnchor());
        register(new ElytraSwap());
        register(new HoverTotem());
        register(new TotemOffhand());
        register(new MaceSwap());
        register(new SpearSwap());
        register(new StaticHitBoxes());
        register(new MaceBomber());

        // ===== MISC =====
        register(new Sprint());
        register(new FastPlace());
        register(new AutoClicker());
        register(new AutoEat());
        register(new Freecam());
        register(new AutoFirework());
        register(new AutoLog());
        register(new AutoLoot());
        register(new AutoMine());
        register(new AutoTool());
        register(new AutoTpa());
        register(new CordSnapper());
        register(new ElytraGlide());
        register(new KeyPearl());
        register(new KeyWindCharge());
        register(new NameProtect());
        register(new SkinProtect());
        register(new AutoReconnect());

        // ===== DONUT =====
        register(new AntiTrap());
        register(new AuctionSniper());
        register(new AutoSell());
        register(new AutoSpawnerSell());
        register(new ItemDropper());
        register(new NetheriteFinder());
        register(new RtpBaseFinder());
        register(new AutoShulkerBuy());
        register(new TunnelBaseFinder());
        register(new FakeStats());
        register(new SpawnerProtect());
        register(new ChunkFinder());
        register(new PrimeChunkFinder());

        // ===== BASEFINDING =====
        register(new SeedChunkFinder());
        register(new HoleESP());
        register(new LightFinder());
        register(new SusChunkFinder());
        register(new SuspiciousESP());

        // ===== RENDER =====
        register(new Fullbright());
        register(new PlayerESP());
        register(new StorageESP());
        register(new BlockESP());
        register(new FreeLook());
        register(new OreSim());
        register(new SwingSpeed());
        register(new JumpCircles());
        register(new TargetHUD());
        register(new RealHitBox());
        register(new CoordinatesHUD());
        register(new FpsHUD());
        register(new CurrentChunkHUD());

        // ===== CLIENT =====
        register(new KryptonPlus());
        register(new ChatMacro());
        register(new Radio());
        register(new Friends());
        register(new DiscordPresence());
    }

    private static void register(Module module) {
        MODULES.add(module);
    }

    public static List<Module> getModules() {
        return Collections.unmodifiableList(MODULES);
    }

    public static List<Module> getByCategory(Module.Category category) {
        return MODULES.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public static Module getByName(String name) {
        return MODULES.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
