package by.teachmeskills.eshop.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "categories")
public class Category extends BaseEntity {
    @Column(name = "NAME")
    private String name;
    @Column(name = "RATING")
    private double rating;
    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Double.compare(category.rating, rating) == 0 && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, rating);
    }
}
