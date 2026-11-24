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

## 内置维度：秽之域

维度 ID、维度类型和噪声配置均为 `poopsky_filthdomain:filthdomain`，分属不同注册表。

| 群系 | ID |
| --- | --- |
| 秽原 | `poopsky_filthdomain:filth_plains` |
| 粪便森林 | `poopsky_filthdomain:poop_forest` |
| 魂灰谷 | `poopsky_filthdomain:soul_ash_valley` |
| 赤菌林 | `poopsky_filthdomain:bloodcap_forest` |
| 孢瘴林 | `poopsky_filthdomain:sporewood` |
| 玄岩裂地 | `poopsky_filthdomain:basalt_badlands` |

世界生成由 `datagen/worldgen` 中的注册引导生成。修改维度、群系、地物或方块标签后，在附属工程运行 `./gradlew runData`，输出位于 `src/generated/resources`。

测试时可以用命令进入维度。

```mcfunction
/execute in poopsky_filthdomain:filthdomain run tp @s 0 64 0
```

群系使用了新的 ID。测试现有存档前请先备份，验证新区块生成时建议使用新存档。

## 可选：开发期免 publish（composite build）

在 `settings.gradle` 中取消注释 `includeBuild("../PoopSkyMod")`，Gradle 会用前置的实时源码替代本地仓库坐标，改完前置直接构建本模组即可生效。
