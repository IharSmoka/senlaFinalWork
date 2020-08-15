package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.ICategoryDao;
import com.senla.training_2019.smolka.model.entities.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends ADao<Category, Integer> implements ICategoryDao {
}
