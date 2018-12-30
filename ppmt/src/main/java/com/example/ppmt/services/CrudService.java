package com.example.ppmt.services;

public interface CrudService<T, ID> {
	T saveOrUpdate(T object);
	Iterable<T> findAll();
}
