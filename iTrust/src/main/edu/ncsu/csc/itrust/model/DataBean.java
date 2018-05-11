package edu.ncsu.csc.itrust.model;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;

public interface DataBean<T> {
	public List<T> getAll() throws DBException;
	public T getByID(long id) throws DBException;
	public boolean add(T addObj) throws DBException;
	public boolean update(T updateObj) throws DBException;
	
}
