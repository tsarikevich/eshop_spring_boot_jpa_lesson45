package by.teachmeskills.eshop.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@Table(name = "images")
public class Image extends BaseEntity {
    @CsvBindByName
    @Column(name = "PRIMARY_FLAG")
    private boolean primaryFlag;
    @CsvBindByName
    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    @ToString.Exclude
    private Product product;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @ToString.Exclude
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Image image = (Image) o;
        return primaryFlag == image.primaryFlag && Objects.equals(imagePath, image.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), primaryFlag, imagePath);
    }
}
