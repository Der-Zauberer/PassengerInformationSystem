package eu.derzauberer.pis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTree<T extends Entity<?>> {
	
	private record Node (Map<Character, Node> children, List<Entity<?>> searchResults) { }
	private final Node root = new Node(new HashMap<>(), new ArrayList<>());
	private int counter = 0;
	
	public void add(T entity) {
		final char[] chars = filterString(entity.getName());
		final int lenght = chars.length;
		for (int i = 0; i < lenght; i++) {
			Node node = root;
			for (int j = i; j < lenght; j++) {
				Node child = null;
				if ((child = node.children.get(chars[j])) == null) {
					child = new Node(new HashMap<>(), new ArrayList<>());
					node.children.put(chars[j], child);
					counter++;
				}
				node = child;
				node.searchResults.add(entity);
			}
		}
	}
	
	public void remove(T entity) {
		final char[] chars = filterString(entity.getName());
		final int lenght = chars.length;
		for (int i = lenght; i > 0; i--) {
			Node node = root;
			final Map<Node, Node> parents = new HashMap<>();
			parents.put(null, node);
			for (int j = i; j < lenght; j++) {
				if ((node = node.children.get(chars[j])) != null) {
					parents.put(node, parents.get(null));
					parents.put(null, node);
					node.searchResults.remove(entity);
				}
			}			
			int j = lenght;
			while (node.children.isEmpty() && node.searchResults.isEmpty() && parents.get(node) != null) {
				parents.get(node).children.remove(chars[j]);
				node = parents.get(node);
				j--;
			}
		}
	}
	
	public boolean contains(T entity) {
		return search(entity.getName()).contains(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> search(String search) {
		final char[] chars = filterString(search);
		Node node = root;
		for (char character : chars) {
			node = node.children.get(character);
			if (node == null) return new ArrayList<>();
		}
		return (List<T>) node.searchResults;
	}
	
	private char[] filterString(String string) {
		return string.toLowerCase().replaceAll("\\W", "").toCharArray();
	}

}
