package com.altnoir.filthdomain.registrate;

import com.altnoir.filthdomain.PoopSkyFilthDomain;
import com.tterrag.registrate.AbstractRegistrate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;

/**
 * 附属的 Registrate 实例。
 * <p>
 * 注意：{@link #onData(GatherDataEvent)} 故意留空。Registrate 默认的
 * {@code RegistrateDataProvider} 在构造时会通过 {@code getFilledProvider()}
 * 急切触发 {@code createContentsProvider()}（内部执行 builders.clear() + addTags()），
 * 当同一 datagen 进程里还有前置 PoopSky 的 RegistrateDataProvider 并发运行时，
 * 该急切调用会与 TagsProvider.run 的迭代撞车，导致
 * {@code ConcurrentModificationException}（两个 Registrate 实例并存时稳定复现）。
 * 方块标签数据生成改由 {@code FDTagsProvider} 自行挂载，链路单线程、无竞态。
 */
public class FDRegistrate extends AbstractRegistrate<FDRegistrate> {

    protected FDRegistrate(String modid) {
        super(modid);
    }

    public static FDRegistrate create(String modId) {
        FDRegistrate registrate = new FDRegistrate(modId);
        Optional<IEventBus> modEventBus = ModList.get()
                .getModContainerById(modId)
                .map(ModContainer::getEventBus);
        modEventBus.ifPresentOrElse(registrate::registerEventListeners,
                () -> PoopSkyFilthDomain.LOGGER.error("Failed to register event listeners for mod {}", modId));
        return registrate;
    }

    @Override
    protected void onData(GatherDataEvent event) {
        // 有意留空，见类注释
    }
}
