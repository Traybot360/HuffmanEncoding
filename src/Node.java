public class Node implements java.io.Serializable {
	public int freq;
	public char value;
	public Node leftNode;
	public Node rightNode;

	/**
	 * Constructor of a single node with a frequency
	 * 
	 * @param n frequency of the Node
	 */
	public Node(int n) {
		this.freq = n;
	}

	/**
	 * Construction of a leaf Node
	 * 
	 * @param a character of the leaf Node
	 * @param n frequency of the leaf Node
	 */
	public Node(char a, int n) {
		this.value = a;
		this.freq = n;
	}

	/**
	 * Construction of a branch section with two children Nodes
	 * 
	 * @param r Node to be added to the right branch
	 * @param l Node to be added to the left branch
	 */
	public Node(Node r, Node l) {
		this.freq = r.freq + l.freq;
		this.rightNode = r;
		this.leftNode = l;
	}

	public void setFreq(int n) {
		this.freq = n;
	}

	public void setValue(char a) {
		this.value = a;
	}
}