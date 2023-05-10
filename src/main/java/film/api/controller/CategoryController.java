package film.api.controller;
import film.api.DTO.CategoryDTO;
import film.api.models.*;
import film.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import film.api.service.CategoryService;
import java.util.List;

@RestController
@RequestMapping(path = "/ApiV1", produces = "application/json")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/AllCategory")
    public ResponseEntity<?> getList(){

        return new ResponseEntity<>(categoryService.getList(), HttpStatus.OK);
    }
    @GetMapping("/CategoryByName/{Categoryname}")
    public ResponseEntity<?> searchCategory(@PathVariable String Categoryname) {
        List<Category> categories = categoryService.searchCategory(Categoryname);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> findById(@PathVariable("CategoryId") Long id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/AllCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO category) {
        Category category1 = categoryService.addCategory(Category.builder()
                .CategoryName(category.getCategoryName()).build());

        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }
    @PatchMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("CategoryId") Long id,@RequestBody CategoryDTO category){
        Category category1 = categoryService.updateCategory(id,Category.builder()
                .CategoryName(category.getCategoryName()).build());
        return new ResponseEntity<>(category1, HttpStatus.OK);
    }
    @DeleteMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("CategoryId") Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Xoa thanh cong",HttpStatus.NO_CONTENT);
    }
}
