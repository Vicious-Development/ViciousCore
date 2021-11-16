package com.vicious.viciouscore.client.render.entity.model.multimob;
/**
 * Model Overrides within this package apply to multiple entities. Here's the following effects each has
 * ModelBiped - applies to: Zombies, ZombieVillagers, Skeletons, Wither Skeletons, Zombie Pigman, Endermen.
 * ModelSpider - Spiders, Cavespiders
 * ModelVillager - Villagers and Witches
 *
 * Unfortunately, ModelVillager acts as both the models for villagers and the parent models for witches.
 * This means if you want to specifically modify villagers and not witches, you will have to make an inactive OverrideConfiguration for witches aswell.
 * Other entities might also have this problem.
 */