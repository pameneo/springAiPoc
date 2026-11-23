package com.pamscafe.mcpserverdemo.repository;

import com.pamscafe.mcpserverdemo.entity.PantryInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PantryInventorRepository extends JpaRepository<PantryInventory, Long> {
    List<PantryInventory> findByItemNameContainingIgnoreCase(String itemName);
}
