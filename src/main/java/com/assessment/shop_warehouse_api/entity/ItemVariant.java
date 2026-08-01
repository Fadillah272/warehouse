package com.assessment.shop_warehouse_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_item_variant", indexes = {
        @Index(name = "idx_variant_item_id", columnList = "item_id")
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ItemVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "variant_name", nullable = false, length = 150)
    private String variantName;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "barcode", nullable = false, unique = true, length = 100)
    private String barcode;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Optimistic locking: mencegah race condition saat 2 transaksi
    // memperebutkan stok yang sama secara bersamaan.
    @Version
    @Column(name = "version")
    private Long version;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
