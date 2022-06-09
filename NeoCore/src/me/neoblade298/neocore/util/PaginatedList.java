package me.neoblade298.neocore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PaginatedList<E> {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private ArrayList<ArrayList<E>> pages = new ArrayList<ArrayList<E>>();
	private int totalPages;
	private int pageSize;
	
	public PaginatedList(Collection<E> col, int pageSize) {
		this.pageSize = pageSize;
		Iterator<E> iter = col.iterator();
		
		ArrayList<E> page = new ArrayList<E>(pageSize);
		pages.add(page);
		while (iter.hasNext()) {
			if (page.size() == pageSize) {
				page = new ArrayList<E>(pageSize);
				pages.add(page);
			}
			
			page.add(iter.next());
		}
	}
	
	public PaginatedList(Collection<E> col) {
		this(col, DEFAULT_PAGE_SIZE);
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public ArrayList<E> getPage(int page) {
		return pages.get(page);
	}
	
	public int getPageSize() {
		return pageSize;
	}
}
