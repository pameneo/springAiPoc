package com.pamscafe.mcpserverdemo.service;


import com.pamscafe.mcpserverdemo.entity.PantryInventory;
import com.pamscafe.mcpserverdemo.model.PantryItem;
import com.pamscafe.mcpserverdemo.repository.PantryInventorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PantryInventoryService {

    private final PantryInventorRepository pantryInventorRepository;

    public PantryInventory addToInventory(PantryItem ticketInput) {
        PantryInventory ticket = PantryInventory.builder()
                .itemName(ticketInput.item())
                .quantity(ticketInput.quantity())
                .boughtDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(60))
                .build();
        return pantryInventorRepository.save(ticket);

    }

    public List<PantryInventory> getItemDetails(String itemName) {
        return pantryInventorRepository.findByItemNameContainingIgnoreCase(itemName);
    }


}
