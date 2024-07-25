package com.microecommerce.productsservice.models;

import com.microecommerce.utilitymodule.models.TimeStamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
// The ProductDetails entity have a composite key to join the Product with the Detail information
// and represents details from a products in the system. Have the objective to store the value of
// the Details of a specific product, and optionally additional information.
public class ProductDetails implements Serializable {

    public static ProductDetails createDetailForProduct(Product product, Detail detail, Object value) {
        var productDetail = new ProductDetails();

        ProductDetailsKey key = new ProductDetailsKey();
        key.setProductId(product.getId());
        key.setDetailId(detail.getId());

        productDetail.setId(key);
        productDetail.setProduct(product);
        productDetail.setDetail(detail);
        productDetail.setValue(value);
        return productDetail;
    }

    @Getter
    @Setter
    @EmbeddedId
    @Column(unique = true, nullable = false)
    private ProductDetailsKey id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    @MapsId("productId")
    private Product product;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "detail_id")
    @MapsId("detailId")
    private Detail detail;

    @Column(length = 500)
    @Size(max = 500)
    private String value;
    private Long numberValue;
    private Double doubleValue;
    private Boolean booleanValue;
    private LocalDateTime dateTimeValue;
    private LocalDate dateValue;

    @Getter
    @Setter
    @Column(length = 500)
    @Size(max = 500)
    private String additionalInfo;

    @Getter
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Type type;

    @Embedded
    @Getter
    private TimeStamped timeStamp;

    // Fast access to detail name
    public String getDetailName() {
        return detail.getName();
    }

    // Fast access to detail description
    public String getDetailDescription() {
        return detail.getDescription();
    }

    public enum Type {
        STRING,
        LONG,
        DOUBLE,
        BOOLEAN,
        DATE,
        DATETIME;

        public static Type getTypeFromValue(Object value) {
            if (value instanceof String) {
                return STRING;
            } else if (value instanceof Long) {
                return LONG;
            } else if (value instanceof Double) {
                return DOUBLE;
            } else if (value instanceof Boolean) {
                return BOOLEAN;
            } else if (value instanceof LocalDate) {
                return DATE;
            } else if (value instanceof LocalDateTime) {
                return DATETIME;
            } else {
                throw new IllegalArgumentException("Unsupported value type");
            }
        }
    }

    public void setValue(Object value) {
        this.type = Type.getTypeFromValue(value);
        switch (type) {
            case STRING:
                this.value = value.toString();
                break;
            case LONG:
                this.numberValue = (Long) value;
                break;
            case DOUBLE:
                this.doubleValue = (Double) value;
                break;
            case BOOLEAN:
                this.booleanValue = (Boolean) value;
                break;
            case DATE:
                this.dateValue = (LocalDate) value;
                break;
            case DATETIME:
                this.dateTimeValue = (LocalDateTime) value;
                break;
            default:
                throw new IllegalArgumentException("Unsupported value type");
        }
    }

    public Object getValue() {
        return switch (type) {
            case STRING -> value;
            case LONG -> numberValue;
            case DOUBLE -> doubleValue;
            case BOOLEAN -> booleanValue;
            case DATE -> dateValue;
            case DATETIME -> dateTimeValue;
        };
    }
}
