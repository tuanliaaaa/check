package film.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import  film.api.models.Category;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String CategoryName;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.CategoryName = category.getCategoryName();
    }
}
