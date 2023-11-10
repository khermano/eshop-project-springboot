package cz.muni.fi.categoryservice.repository;

import cz.fi.muni.pa165.entity.Category;

import java.util.List;


public interface CategoryRepository {
	public Category findById(Long id);
	public void create(Category c);
	public void delete(Category c);
	public List<Category> findAll();
	public Category findByName(String name);
}
