# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.16.5-2.7.0.1] - 2021-08-21
### Added
- Russian and Ukrainian translation (thanks to vstannumdum aka DMHYT) #59
- some more automated tests

## [1.16.5-2.7.0] - 2021-06-26
### Changed
- Update mod to Forge 1.16.5-36.1.0 to fix some small issues
- add ceramic items to item tag itemfilters:check_nbt to support the mods FTB Quests & Item Filters (thanks to ItayKisous for the report) #58
- change order of Minecraft version and mod version to fit [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/)
- Item capabilities are cached now for a slightly better performance
- change mappings to official channel
- add some automated tests to test the mod functionalities automatically

## [2.6.4_1.16] - 2021-06-22
### Added
- Support Alex's Mobs version 1.11.0 (two new entities)

## [2.6.3_1.16] - 2021-06-20
### Added
- Support Fins And Trails version 1.6.0 (new entity, some changed textures)

## [2.6.2_1.16] - 2021-06-12
### Added
- Support Cindershell entity of Creatures And Beats mod #57

### Changed
- Using the translation key of the fluid attributes to support the fluid translations of Create mod #49 (thanks to imliterallydoingthistoreportabug for the report and PepperCode1 for the help)

## [2.6.1_1.16] - 2021-05-28
### Added
- add support of 1 new mob of "Fins and Trails" mod (Gopjet) #55
- add support of the axolotl of "Caves and Cliffs Mod, 1.17 concept"
- add support of the axolotl of "Caves and Cliffs Backport" mod #56

## [2.6.0_1.16] - 2021-04-27
### Added
- add support of the 2 new mobs of Alex's Mobs mod #52
- add support of the 1 new mob of Upgrade Aquatic mod #50

### Changed
- change pike texture of Upgrade Aquatic mod #50
- change 4 textures of Fins and Trails mod #51

### Fixed
- Empty ceramic buckets cannot interact any longer with tanks with less than 1000mb. #53 (thanks to benbenlaw for the report)
- Filled ceramic buckets cannot interact with tanks with less than 1000mb empty space.

## [2.5.7_1.16] - 2021-02-09
### Added
- Add support for Fins and Trails mod.

## [2.5.6_1.16] - 2021-01-18
### Added
- Support the Forge milk addition

## [2.5.5_1.16] - 2021-01-13
### Changed
- Ceramic bucket coloring was adapted to terracotta colors.
- Tactical Fishing advancement is now triggered when a fish is caught with a ceramic bucket.

## [2.5.4_1.16] - 2021-01-11
### Added
- fish buckets of Alex's Mobs, Environmental and The Undergarden are now compatible (thanks to bloche1871 for the hint)

## [2.5.3_1.16] - 2021-01-10
### Added
- Support mods which are adding other cauldrons #44 (thanks to benbenlaw for the report)

## [2.5.2_1.16] - 2020-12-20
### Fixed
- fix possible server crash #42 (thanks to ClaudiusMinimus)

## [2.5.1_1.16] - 2020-12-08
### Changed
- enhance nbt handling for better mod compatibility
- infinity enchanted buckets are not looking cracked if filled with breaking fluid
- infinity enchanted buckets in crafting recipes are now working like intended

### Fixed
- bugfix: infinity enchanted buckets could be enchanted with infinity again

## [2.5.0_1.16] - 2020-12-06
### Added
- Ceramic Buckets can be dyed like leather armor (thanks to pandrian29 for the idea)

### Changed
- reworked textures of Ceramic Buckets

## [2.4.0_1.16] - 2020-09-10
### Added
- Slime Bucket of Quark mod is supported now
- fluid tag "ceramicbucket:ceramic_cracking" added
- All fluids listed there are breaking a ceramic bucket unrelated to the ceramicBucketBreakTemperature config.
- fluid tag "ceramicbucket:infinity_enchantable" added
- This tag has only an effect if infinityEnchantmentEnabled config is enabled.

### Removed
- corresponding infinityEnchantmentFluids config removed

## [2.3.2_1.16] - 2020-11-12
### Fixed
- Bugfix: Server crash when right-clicking an entity with a ceramic bucket #36 (thanks to nanonestor for reporting this issue)

## [2.3.1_1.16] - 2020-11-07
### Added
- add config option to define fluids that can be enchanted with Infinity (default: water)

## [2.3.0_1.16] - 2020-11-03
### Added
- add possibility to enchant filled ceramic buckets with infinity (only for multiplying fluids like water)
- add a config option for enabling this infinity enchantment (default disabled)

### Changed
- config has been moved to serverconfig folder of each savegame for consistent configuration
- entity buckets are displayed independently of the configuration

## [2.2.1_1.16] - 2020-10-28
### Added
- Water and Lava can be added to Botany Pots with ceramic buckets (since BotanyPots-1.16.3-5.1.9)
- Support entity buckets of Axolotl mod (when it is released for 1.16)

## [2.2.0_1.16] - 2020-10-21
### Added
- Fish buckets of other mods can now be supported.
- Milking entities with a Ceramic Bucket can now be enabled/disabled via config.
- support for fish buckets of Aquaculture 2
- support for fish buckets of Upgrade Aquatic
- support for fish bucket of Combustive Fishing

### Fixed
- soundfix of cauldron interaction

### Removed
- removed forge fluid tags of water and lava (changed recipes to use the corresponding vanilla tags)

## [2.1.1_1.16] - 2020-10-08
### Added
- added config option to disable fish obtaining with ceramic buckets
- if fish obtaining is disabled there are no ceramic fish buckets in creative tab and JEI

### Fixed
- bugfix: a fish of another mod was maybe transformed to salmon - fixed

## [2.1.0_1.16] - 2020-10-03
### Added
- filled Ceramic Buckets can now be used as ingredient in data packs (via the item itself or via its contained fluid)
- added compatible recipes for CauldronRecipes, CobblesForDays mod
- added two fluid tags (forge:water, forge:lava)

### Fixed
- When bucket was filled, the empty sound was played - fixed
- there was an error in the logs when jei was installed - fixed

## [2.0.6_1.16] - 2020-09-11
### Added
- blocks that can be waterlogged with other fluids than water are supported now

### Known Bugs
- Icons of filled ceramic buckets are a bit darker in JEI (fixed in jei-1.16.2-7.3.2.28 and later)

## [2.0.5_1.16] - 2020-09-11
### Added
- cauldrons can now be emptied or filled with water using a ceramic bucket

## [2.0.4_1.16] - 2020-09-10
### Added
- fuel recipe for ceramic lava bucket added to JEI (if installed)
- filling recipe added for Tinkers Construct (if installed)

### Removed
- internal change: removed old model files

## [2.0.3_1.16] - 2020-09-08
### Added
- Ceramic bucket filled with lava can now be used as fuel in furnaces. (thanks to NetherStriderMC for reporting this issue)
- It also works for other fluids that have a burn time for their buckets.

## [2.0.2_1.16] - 2020-09-01
### Changed
- Because of a breaking change in Forge 33.0.21 Minecraft 1.16.1 cannot be supported any longer
- requires Forge 33.0.21 or later
- requires Minecraft 1.16.2 or later

## [2.0.1_1.16] - 2020-08-25
### Changed
- Mod supports Minecraft 1.16.2 now
- requires Forge 32.0.108 or later (because of a breaking change in Forge 32.0.106)
- only works until Forge 33.0.20 (because of a breaking change in Forge 33.0.21)

## [2.0.0_1.16] - 2020-08-11
### Changed
Mod is now available for Minecraft 1.16.1! (Only works until Forge 32.0.106)

### Added
- Ceramic Buckets filled with a "too hot" fluid (like lava) are looking cracked
- All fluids and gases of all mods are supported now
- flipped buckets are also supported for all fluids and gases where the density is lower than 0
- Ceramic Milk Buckets can now be used in recipes of Pam's Harvestcraft 2

### Fixed
- reordered filled Ceramic Bucket names (from "Milk Ceramic Bucket" to "Ceramic Milk Bucket"
- not placeable fluids disappeared when the player tried to place it
- some buckets of other mods did not appear in creative tab
- duplicated milk buckets in creative tab
