package com.pamscafe.mcpserverdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pantry_log")
public class PantryInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private LocalDateTime boughtDate;

    private LocalDateTime expiryDate;

    private String quantity;
}
