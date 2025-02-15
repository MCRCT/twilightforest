package twilightforest.compat;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import twilightforest.TwilightForestMod;

import java.util.*;

//I was having an issue where the game refused to load with the enum method and I couldnt figure it out, so I moved to a new method of registering compat
//It works the same as it used to, but all the content for each mod should be in its own class.
public abstract class TFCompat {

    public static final String CURIOS_ID = "curios";
    public static final String IE_ID = "immersiveengineering";
    public static final String TCON_ID = "tconstruct";
    public static final String UNDERGARDEN_ID = "undergarden";

    public static HashMap<String, Class<? extends TFCompat>> classes = new HashMap<>();
    public static Set<TFCompat> modules = new HashSet<>();

    static {
        classes.put(CURIOS_ID, CuriosCompat.class);
        classes.put(IE_ID, IECompat.class);
        classes.put(TCON_ID, TConCompat.class);
        classes.put(UNDERGARDEN_ID, UndergardenCompat.class);
    }

    protected TFCompat(String modName) {
        this.modName = modName;
    }

    public static void preInitCompat() {
        for (Map.Entry<String, Class<? extends TFCompat>> entry : classes.entrySet()) {
            if (ModList.get().isLoaded(entry.getKey())) {
                try {
                    TFCompat compat = entry.getValue().newInstance();
                    modules.add(compat);
                    compat.isActivated = compat.preInit();

                    if (compat.isActivated) {
                        TwilightForestMod.LOGGER.info("Loaded compatibility for mod {}.", compat.modName);
                    } else {
                        TwilightForestMod.LOGGER.warn("Couldn't activate compatibility for mod {}!", compat.modName);
                    }
                } catch (Exception e) {
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in preInit!", e.getLocalizedMessage(), entry.getKey());
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            } else {
                TwilightForestMod.LOGGER.info("Skipped compatibility for mod {}.", entry.getKey());
            }
        }
    }

    public static void initCompat(FMLCommonSetupEvent event) {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.init(event);
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in init!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void initCompatItems(RegistryEvent.Register<Item> evt) {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.initItems(evt);
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in initializing items!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }


    public static void postInitCompat() {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.postInit();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in postInit!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void sendIMCs() {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.handleIMCs();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in sending IMCs!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    protected abstract boolean preInit();

    protected abstract void init(FMLCommonSetupEvent event);

    protected abstract void postInit();

    protected abstract void handleIMCs();

    protected abstract void initItems(RegistryEvent.Register<Item> evt);

    public final String modName;

    private boolean isActivated = false;
}
