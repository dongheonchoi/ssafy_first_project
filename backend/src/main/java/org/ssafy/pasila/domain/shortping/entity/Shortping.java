package org.ssafy.pasila.domain.shortping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.sqids.Sqids;
import org.ssafy.pasila.domain.product.entity.Product;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shortping {

    @Id
    @Column(columnDefinition = "VARCHAR(10)")
    private String id;

    @PrePersist
    public void createUniqId() {

        Sqids sqids = Sqids.builder()
                .minLength(10)
                .build();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long timestampAsLong = timestamp.getTime();
        String newId = sqids.encode(List.of(timestampAsLong, 2L));

        this.id = newId;

    }

    @Column(length = 30, nullable = false)
    private String title;

    @Column(name = "like_cnt")
    private Integer likeCnt;

    @Column(name = "video_url", length = 2083)
    private String videoUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    @ColumnDefault("true")
    private boolean isActive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        product.setShortping(this);
    }

    public static Shortping createShortping(String title, String videoUrl, Product product) {
        Shortping shortping = new Shortping();
        shortping.setTitle(title);
        shortping.setVideoUrl(videoUrl);
        shortping.setProduct(product);
        shortping.setLikeCnt(0);
        shortping.setActive(true);
        return shortping;
    }

}
