package com.assessment.shop_warehouse_api.common.config;

import com.assessment.shop_warehouse_api.entity.*;
import com.assessment.shop_warehouse_api.repository.CategoryRepository;
import com.assessment.shop_warehouse_api.repository.ItemRepository;
import com.assessment.shop_warehouse_api.repository.ItemVariantRepository;
import com.assessment.shop_warehouse_api.repository.SaleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Mengisi data contoh saat aplikasi start (khususnya untuk H2 in-memory
// yang selalu kosong tiap kali di-restart). Hanya jalan kalau tabel
// category masih kosong, supaya tidak dobel kalau nanti pindah ke
// database yang persistent (mis. PostgreSQL).
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ItemVariantRepository variantRepository;
    private final SaleRepository saleRepository;

    public DataSeeder(CategoryRepository categoryRepository,
                      ItemRepository itemRepository,
                      ItemVariantRepository variantRepository,
                      SaleRepository saleRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.variantRepository = variantRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // sudah ada data, jangan seed ulang
        }

        // ---------- Category ----------
        Category shoes = newCategory("Sepatu", "Sepatu casual dan olahraga");
        Category apparel = newCategory("Pakaian", "Kaos, kemeja, dan jaket");
        Category accessories = newCategory("Aksesoris", "Tas, topi, dan aksesoris lainnya");
        categoryRepository.saveAll(List.of(shoes, apparel, accessories));

        // ---------- Item ----------
        Item nikeAirMax = newItem("SKU-001", "Nike Air Max", "Sepatu lari dengan bantalan udara", shoes);
        Item adidasUltra = newItem("SKU-002", "Adidas Ultraboost", "Sepatu lari performa tinggi", shoes);
        Item basicTee = newItem("SKU-003", "Basic Cotton Tee", "Kaos katun polos", apparel);
        Item denimJacket = newItem("SKU-004", "Denim Jacket", "Jaket denim unisex", apparel);
        Item toteBag = newItem("SKU-005", "Canvas Tote Bag", "Tas kanvas serbaguna", accessories);
        itemRepository.saveAll(List.of(nikeAirMax, adidasUltra, basicTee, denimJacket, toteBag));

        // ---------- Item Variant (10 data) ----------
        List<ItemVariant> variants = List.of(
                newVariant(nikeAirMax, "Black Size 42", "Black", "42", "899000000001", new BigDecimal("1250000"), 15),
                newVariant(nikeAirMax, "White Size 43", "White", "43", "899000000002", new BigDecimal("1250000"), 8),
                newVariant(adidasUltra, "Black Size 41", "Black", "41", "899000000003", new BigDecimal("1890000"), 5),
                newVariant(adidasUltra, "Grey Size 42", "Grey", "42", "899000000004", new BigDecimal("1890000"), 3),
                newVariant(basicTee, "White Size M", "White", "M", "899000000005", new BigDecimal("120000"), 50),
                newVariant(basicTee, "Black Size L", "Black", "L", "899000000006", new BigDecimal("120000"), 40),
                newVariant(basicTee, "Navy Size S", "Navy", "S", "899000000007", new BigDecimal("120000"), 2),
                newVariant(denimJacket, "Blue Size L", "Blue", "L", "899000000008", new BigDecimal("450000"), 12),
                newVariant(denimJacket, "Black Size M", "Black", "M", "899000000009", new BigDecimal("450000"), 0),
                newVariant(toteBag, "Natural", "Natural", "One Size", "899000000010", new BigDecimal("85000"), 25)
        );
        variantRepository.saveAll(variants);

        // ---------- Sample Sale (opsional, untuk isi dashboard & riwayat) ----------
        // Sengaja tidak ditambahkan di sini karena melibatkan pengurangan stok +
        // pencatatan StockMovement yang lebih pas dilakukan lewat SalesService
        // (biar konsisten dengan alur transaksi yang sebenarnya).
    }

    private Category newCategory(String name, String description) {
        Category category = new Category();
        category.setNameCategory(name);
        category.setDescription(description);
        return category;
    }

    private Item newItem(String sku, String name, String description, Category category) {
        Item item = new Item();
        item.setSku(sku);
        item.setNameItem(name);
        item.setDescription(description);
        item.setCategory(category);
        return item;
    }

    private ItemVariant newVariant(Item item, String variantName, String color, String size,
                                   String barcode, BigDecimal price, int stock) {
        ItemVariant variant = new ItemVariant();
        variant.setItem(item);
        variant.setVariantName(variantName);
        variant.setColor(color);
        variant.setSize(size);
        variant.setBarcode(barcode);
        variant.setPrice(price);
        variant.setStock(stock);
        return variant;
    }
}