package film.api.service;

import film.api.DTO.CategoryDTO;
import film.api.models.Category;
import film.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getCategoryByFilmID(Long filmID){
        return categoryRepository.getCategoryByFilmID(filmID);
    }
    public List<CategoryDTO> getList(){
        List<CategoryDTO> categoryDTO = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        for(Category category : categories){
            CategoryDTO newDTO = new CategoryDTO(category);
            categoryDTO.add(newDTO);
        }
        return categoryDTO;
    }
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    public List<Category> searchCategory(String categoryname) {
        return categoryRepository.findCategoryByName(categoryname);
    }
    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }
    public Category updateCategory(Long id, Category category){
        Category old = categoryRepository.findById(id).get();
        old.setCategoryName(category.getCategoryName());
        return categoryRepository.save(old);
    }
    public  void deleteCategory(Long id){
        categoryRepository.delete(categoryRepository.findById(id).get());
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
