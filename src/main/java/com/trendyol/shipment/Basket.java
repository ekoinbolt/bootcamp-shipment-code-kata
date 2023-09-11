package com.trendyol.shipment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {

    private List<Product> products;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ShipmentSize getShipmentSize() {
        List<ShipmentSize> shipmentSizesOfProducts = products.stream()
                .map(Product::getSize)
                .collect(Collectors.toList());

        Map<ShipmentSize, Long> sizeCount = shipmentSizesOfProducts.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        if (sizeCount.getOrDefault(ShipmentSize.X_LARGE, 0L) >= 3 ||
                sizeCount.getOrDefault(ShipmentSize.X_LARGE, 0L).equals(Long.valueOf(shipmentSizesOfProducts.size()))) {
            return ShipmentSize.X_LARGE;
        }

        for (ShipmentSize size : ShipmentSize.values()) {
            if (sizeCount.getOrDefault(size, 0L) >= 3) {
                int nextSize = Math.min(ShipmentSize.values().length - 1, size.ordinal() + 1);
                return ShipmentSize.values()[nextSize];
            }
        }

        return shipmentSizesOfProducts.stream()
                .max(Enum::compareTo)
                .orElse(ShipmentSize.SMALL);
    }
}
