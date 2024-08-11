package org.twspring.exercisejpa.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.Category;
import org.twspring.exercisejpa.Repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer id) {
        //Makes sure it returns null if no object with the id exists
        if (categoryRepository.findById(id).isPresent()) {
           return categoryRepository.getById(id);
        }
        return null;
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public boolean updateCategory(int id, Category category) {
        Category updatedCategory = getCategory(id);
        if (updatedCategory == null) {
            return false;
        }
        updatedCategory.setName(category.getName());
        categoryRepository.save(updatedCategory);
        return true;
    }

    public boolean deleteCategory(int id) {
        Category updatedCategory = getCategory(id);
        if (updatedCategory == null) {
            return false;
        }
        categoryRepository.delete(updatedCategory);
        return true;
    }
}
