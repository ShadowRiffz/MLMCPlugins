package me.neoblade298.neocore.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PaginatedList<E> {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private LinkedList<LinkedList<E>> pages = new LinkedList<LinkedList<E>>();
	private int pageSize;
	
	public PaginatedList() {
		this(DEFAULT_PAGE_SIZE);
	}
	
	public PaginatedList(int pageSize) {
		this.pageSize = pageSize;
		pages.add(new LinkedList<E>());
	}
	
	public PaginatedList(Collection<E> col, int pageSize) {
		this.pageSize = pageSize;
		Iterator<E> iter = col.iterator();
		
		LinkedList<E> page = new LinkedList<E>();
		pages.add(page);
		while (iter.hasNext()) {
			if (page.size() == pageSize) {
				page = new LinkedList<E>();
				pages.add(page);
			}
			
			page.add(iter.next());
		}
	}
	
	public PaginatedList(Collection<E> col) {
		this(col, DEFAULT_PAGE_SIZE);
	}
	
	public int size() {
		return ((pages.size() - 1) * pageSize) + pages.getLast().size();
	}
	
	public int pages() {
		return pages.size();
	}
	
	public LinkedList<E> get(int page) {
		return pages.get(page);
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public E remove(int pagenum, int index) {
		LinkedList<E> page = pages.get(pagenum);
		E item = page.remove(index);
		
		// Trickle down all pages after
		bubbleUp(pagenum);
		return item;
	}
	
	public E remove(int index) {
		return remove(index / pageSize, index % pageSize);
	}
	
	public E remove(E toRemove) {
		Iterator<LinkedList<E>> iter = pages.descendingIterator();
		int pagenum = pages.size() - 1;
		while (iter.hasNext()) {
			LinkedList<E> page = iter.next();
			int index = page.indexOf(toRemove);
			if (index != -1) {
				E item = page.remove(index);
				bubbleUp(pagenum);
				return item;
			}
			pagenum--;
		}
		return null;
	}
	
	private void bubbleUp(int pagenum) {
		// If this page is not the last page and its size is less than max size
		if (pagenum < pages.size() - 1 && pages.get(pagenum).size() < pageSize) {
			for (int i = pagenum; i + 1 < pages.size(); i++) {
				pages.get(i).add(pages.get(i + 1).removeFirst());
				// If the last page only had 1 item and now has 0, remove it
				if (pages.get(i + 1).size() == 0) {
					pages.remove(i + 1);
				}
			}
		}
	}
	
	private void trickleDown(int pagenum) {
		if (pages.get(pagenum).size() > pageSize) {
			for (int i = pagenum; i + 1 < pages.size(); i++) {
				pages.get(i + 1).push(pages.get(i).removeFirst());
			}
			
			// If the last page has more than max page size
			if (pages.getLast().size() > pageSize) {
				LinkedList<E> page = new LinkedList<E>();
				page.add(pages.getLast().removeLast());
			}
		}
	}
	
	public void push(E item) {
		pages.getFirst().push(item);
		trickleDown(0);
	}
	
	public void add(E item) {
		pages.getLast().add(item);
		trickleDown(pages.size() - 1);
	}
	
	public void clear() {
		pages.clear();
	}
	
	public void displayFooter(Player p, int page, String nextCmd, String prevCmd) {
		ComponentBuilder prev = new ComponentBuilder("§7§ ");
		if (page > 0) {
			prev.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, prevCmd))
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to previous page!")));
		}
		ComponentBuilder main = new ComponentBuilder("§7Page §f" + (page + 1) + " §7/ " + pages.size());
		ComponentBuilder next = new ComponentBuilder("§7 §");
		if (page < pages.size() - 1) {
			next.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, nextCmd))
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to next page!")));
		}
		p.spigot().sendMessage(prev.append(main.append(next.create()).create()).create());
	}
	
	public void displayFooter(Player p, int page) {
		Util.msg(p, "&7Page &f" + (page + 1) + " &7/ " + pages.size());
	}
}
