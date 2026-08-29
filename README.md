# PoopSky-FilthDomain（空中厕所：秽之域）

PoopSky 的附属模组（NeoForge 1.21.1 / Java 21）。

- 命名空间（mod id）：`poopsky_filthdomain`
- 包名：`com.altnoir.filthdomain`
- 前置：[PoopSky](https://github.com/Altnoir/PoopSkyMod)（`type = "required"`）

## 构建

1. 先把前置发布到本地仓库（首次或前置改动后执行）：

   ```bash
   cd ../PoopSkyMod
   ./gradlew publish
   ```

   产物输出到 `PoopSkyMod/repo/`（本项目 `build.gradle` 已把该目录配为仓库）。

2. 构建本模组：

   ```bash
   ./gradlew build
   ```

   产物在 `build/libs/`。

## 开发运行

```bash
./gradlew runClient   # 客户端（开发环境会自动加载前置 PoopSky）
./gradlew runData     # 数据生成（输出到 src/generated/resources）
```

## 调用前置代码

直接 `import com.altnoir.poopsky.*` 即可，例如：

```java
import com.altnoir.poopsky.init.PoItems;

// 前置的物品
PoItems.POOP.get();
```

要点：

- 前置在 `neoforge.mods.toml` 中声明为 `required`，加载顺序由 NeoForge 保证，代码里无需 `ModList.isLoaded()` 判断。
- 注册自己的方块/物品/实体请使用本项目的 `DeferredRegister`（`init/FDItems.java`），**不要**复用前置的 `PoopSky.registrate()`。
- 前置的配方类型（`poopsky:sieve`、`poopsky:fly_barrel` 等）是数据驱动的：在 `src/main/resources/data/poopsky_filthdomain/recipe/...` 里写 JSON 并把 `"type"` 指向前置序列化器即可，JEI 显示自动生效。
- 若想把自己的物品加进前置的创造栏，可用 `BuildCreativeModeTabContentsEvent` 判断 tab 后添加；本项目默认使用自己的创造栏。

## 内置维度：绝望世界（desperate_world）

模组内置了"绝望世界"维度数据包（Nether 风格，5 个生物群系），全部挂在本模组命名空间下，不与其他模组冲突：

- 维度 ID：`poopsky_filthdomain:desperate_world`
- 维度类型：`poopsky_filthdomain:desperate_world`
- 生物群系：`poopsky_filthdomain:desperate_wastes` / `desperate_soul_sand_valley` / `desperate_crimson` / `desperate_warped` / `desperate_basalt_deltas`
- 地表/噪声/特性/雕刻器全部为 `poopsky_filthdomain:desperate_*`

数据包内对**前置模组内容**的引用（`poopsky:poop_block`、`poopsky:urine` 流体、`poopsky:poolime`、`poopsky:poop_particle` 等）保持 `poopsky:` 不变。

进入方式（数据包本身不含传送门，先直接传送）：

```
/execute in poopsky_filthdomain:desperate_world run tp @s 0 64 0
```

> 提示：维度的世界生成数据位于 `src/main/resources/data/poopsky_filthdomain/`，直接手写维护即可；**方块标签（`tags/block/`）由 datagen 生成**：`datagen/FDBlockTagGen.java` 采用与前置一致的 Registrate `addDataGenerator` 写法，通过 `FDTagsProvider`（+ `FDRegistrate`）单独挂载，避免两个 Registrate 实例并存时 RegistrateDataProvider 的并发竞态。改标签后运行 `./gradlew runData`（输出到 `src/generated/resources`）。如需传送物品/传送门，可在附属代码里加。

## 可选：开发期免 publish（composite build）

在 `settings.gradle` 中取消注释 `includeBuild("../PoopSkyMod")`，Gradle 会用前置的实时源码替代本地仓库坐标，改完前置直接构建本模组即可生效。
