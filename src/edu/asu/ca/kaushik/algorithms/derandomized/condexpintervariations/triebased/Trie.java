package edu.asu.ca.kaushik.algorithms.derandomized.condexpintervariations.triebased;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trie {

	private int count;
	private int numChild;
	private Trie[] children;
	private int symb;	
	
	public Trie(int n) {
		this.numChild = n;
		this.children = new Trie[this.numChild];
		this.count = 0;
	}
	
	public Trie(int n, int s) {
		this(n);
		this.symb = s;
	}
	
	public void setSymb(int s) {
		this.symb = s;
	}
	
	public int getSymb() {
		return this.symb;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getNumChild() {
		return numChild;
	}

	public Trie[] getChildren() {
		return children;
	}
	
	public List<int[]> getInstances(int t) {
		List<int[]> instances = new ArrayList<int[]>();
		int[] instance = new int[t];
		this.traverse(instances, instance, t, t);	
		return instances;
	}
	
	
	private void traverse(List<int[]> instances, int[] instance, int t, int i) {
		boolean noChildren = true;
		for (int v = 0; v < this.numChild; v++) {
			if (this.children[v] != null) {
				instance[t-i] = v;
				this.children[v].traverse(instances, instance, t, i-1);
				noChildren = false;
			}
		}
		
		if (noChildren) {
			instances.add(Arrays.copyOf(instance, t));
		}		
	}

	/**
	 * @param prefix The prefix to be matched in the trie rooted at this node
	 * @return number of in
	 */
	public int getNumInstanceWPrefix(int[] prefix) {
		if (prefix.length == 0) {
			return this.count;
		} else {
			int index = prefix[0];
			if (this.children[index] == null) {
				return 0;
			} else {
				return this.children[index].getNumInstanceWPrefix(
						Arrays.copyOfRange(prefix, 1, prefix.length));
			}
		}
	}
	
	/**
	 * Deletes the suffix denoted by string from the trie rooted at
	 * this node
	 * @param string the suffix to be deleted
	 * @return 1 - if a suffix is deleted
	 * 			0 - if no suffix is deleted or the input is not valid
	 */
	public int deleteInstance(int[] string) {
		int ret = 0;
		if (string.length == 0) {
			if (this.noChildren()) {
				this.count = 0;
				ret = 1;
			}
		} else {
			int index = string[0];
			if (this.children[index] != null) {
				ret = this.children[index].deleteInstance(
						Arrays.copyOfRange(string, 1, string.length));
				this.count = this.count - ret;
				if (this.children[index].count == 0) {
					this.children[index] = null;
				}
			}
		}
		return ret;
	}
	
	public boolean containsInstance(int[] string) {
		boolean ret = false;
		if (string.length == 0) {
			if (this.noChildren()) {
				ret = true;
			}
		} else {
			int index = string[0];
			if (this.children[index] != null) {
				ret = this.children[index].containsInstance(
						Arrays.copyOfRange(string, 1, string.length));
			}
		}
		return ret;
	}
	
	public boolean noChildren() {
		for (int i = 0; i < this.numChild; i++) {
			if (this.children[i] != null) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return "Trie Node [count = " + this.count + "; symb = " + this.symb + "]\n";
	}
	
	public String printPreOrder() {
		String s = this.toString();
		for (int i = 0; i < this.numChild; i++) {
			if (this.children[i] != null) {
				s = s + this.children[i].printPreOrder();
			} else {
				s = s + "null\n";
			}
		}
		return s;
	}
	
	public static Trie buildTrie(int v, int t) {
		Trie root = new Trie(v, -1);
		root.setCount((int)Math.pow(v, t));
		if (t > 0) {
			for (int i = 0; i < v; i++) {
				root.children[i] = buildTrie(v, t-1);
				root.children[i].setSymb(i);
			}
		}
		return root;
	}
	
	/**
	 * Makes deep copy of the trie rooted at original
	 * @param original the root of the original trie
	 * @return root of the copied trie
	 */
	public static Trie copyTrie(Trie original) {
		Trie copy = null;
		if (original != null) {
			copy = new Trie(original.getNumChild(), original.getSymb());
			copy.setCount(original.getCount());
			int n = copy.getNumChild();
			for (int i = 0; i < n; i++) {
				copy.children[i] = copyTrie(original.children[i]);
			}
		}
		return copy;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Trie t = buildTrie(2,2);
		Trie p = copyTrie(t);
		
		List<int[]> instances = p.getInstances(2);
		for (int[] ins : instances) {
			System.out.println(Arrays.toString(ins));
		}
		
		int[] a1 = {0,0};
		int[] a2 = {0,1};
		int[] a3 = {1,1};
		int[] a4 = {1,0};
		
		System.out.println(t.printPreOrder());
		t.deleteInstance(a1);
		System.out.println(t.printPreOrder());
		
		t.deleteInstance(a2);
		System.out.println(t.printPreOrder());
		
		System.out.println(p.printPreOrder());
		p.deleteInstance(a3);
		System.out.println(p.printPreOrder());
		
		instances = p.getInstances(2);
		for (int[] ins : instances) {
			System.out.println(Arrays.toString(ins));
		}
		
		p.deleteInstance(a4);
		System.out.println(p.printPreOrder());		
		
		p.deleteInstance(a4);
		System.out.println(p.printPreOrder());		
		
		p.deleteInstance(new int[]{0});
		System.out.println(p.printPreOrder());		
		
		p.deleteInstance(new int[]{1,0,1});
		System.out.println(p.printPreOrder());
		
		System.out.println(p.containsInstance(a1));
		System.out.println(p.containsInstance(a3));
		System.out.println(p.containsInstance(new int[]{1,0,1}));
		System.out.println(p.containsInstance(new int[]{}));
		
		instances = p.getInstances(2);
		for (int[] ins : instances) {
			System.out.println(Arrays.toString(ins));
		}

	}

}
