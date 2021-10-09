package me.logwet.blinded.config;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BlindedConfig {
    private boolean f3Enabled = true;

    private boolean recipeBookEnabled = true;

    private List<UserConfigInventoryItemEntry> inventory;

    public BlindedConfig(List<UserConfigInventoryItemEntry> inventory) {
        this.inventory = inventory;
    }

    public static BlindedConfig fromFixedConfigs(List<InventoryItemEntry> inventory) {
        return new BlindedConfig(inventory
                .stream()
                .map(item -> new UserConfigInventoryItemEntry(item.getName(), item.getPrettySlot()))
                .collect(Collectors.toList()));
    }

    @NotNull
    public Boolean isF3Enabled() {
        return f3Enabled;
    }

    @NotNull
    public Boolean isRecipeBookEnabled() {
        return recipeBookEnabled;
    }

    @NotNull
    public List<UserConfigInventoryItemEntry> getInventory() {
        return inventory;
    }

    @NotNull
    public Map<String, Integer> getItems() {
        return inventory
                .stream()
                .collect(Collectors.toMap(UserConfigInventoryItemEntry::getName, UserConfigInventoryItemEntry::getSlot));
    }

    public boolean matches(List<InventoryItemEntry> uniqueItemsList) {
        Set<String> uniqueItems = uniqueItemsList
                .stream()
                .map(InventoryItemEntry::getName)
                .collect(Collectors.toSet());

        return getInventory().size() == uniqueItems.size()
                && getInventory()
                .stream()
                .map(UserConfigInventoryItemEntry::getName)
                .allMatch(uniqueItems::contains);
    }
}

