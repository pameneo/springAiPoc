package com.pamscafe.mcpserverdemo.tools;

import com.pamscafe.mcpserverdemo.entity.PantryInventory;
import com.pamscafe.mcpserverdemo.model.PantryItem;
import com.pamscafe.mcpserverdemo.service.PantryInventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PantryInventoryTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(PantryInventoryTools.class);

    private final PantryInventoryService service;

    @McpTool(name = "addItem", description = "Add an item to pantry inventory")
    String addItem(@McpToolParam(description = "Details to add an item to pantry inventory")
                        PantryItem pantryItem) {
        LOGGER.info("Add an item to pantry inventory: {}", pantryItem);
        PantryInventory inventory = service.addToInventory(pantryItem);
        LOGGER.info("Item created successfully. Item ID: {}, Item name: {}", inventory.getId(), inventory.getItemName());
        return "Item #" + inventory.getId() + " created successfully for item " + inventory.getItemName();
    }

    @McpTool(name="checkMyPantry", description = "Fetch the details of the items based on a given itemname in my pantry")
    List<PantryInventory> getItemDetails(@McpToolParam(description =
            "itemName to fetch the details of the item in pantry inventory") String itemName) {
        LOGGER.info("Fetching details for item: {}", itemName);
        List<PantryInventory> itemDetails =  service.getItemDetails(itemName);
        LOGGER.info("Found {} items for user: {}", itemDetails.size(), itemName);
        return itemDetails;
    }

}
