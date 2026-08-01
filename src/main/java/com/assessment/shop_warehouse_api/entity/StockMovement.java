package com.assessment.shop_warehouse_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// StockMovement adalah log riwayat (immutable), tidak butuh is_deleted / updated_at.
@Entity
@Table(name = "t_stock_movement", indexes = {
        @Index(name = "idx_stockmovement_variant_id", columnList = "variant_id")
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ItemVariant variant;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "previous_stock", nullable = false)
    private Integer previousStock;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
