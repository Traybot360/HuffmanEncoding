import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

public class Encode {
	public static String encFileName = "File.encoded";
	public static String txtFileName = "WarAndPeace.txt";
	public static String serFileName = "Tree.ser";

	public static void seralize(Node n) {
		try {
			FileOutputStream fileOut = new FileOutputStream(serFileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(n);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + serFileName);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static String findHuffmanValue(Node n, char c, String bits) {
		if (n.leftNode == null && n.rightNode == null) {
			if (n.value == c)
				return bits;
			return "";
		}
		return findHuffmanValue(n.leftNode, c, bits + "0") + findHuffmanValue(n.rightNode, c, bits + "1");
	}

	public static void binaryOutput(Node root) {

		FileInputStream in;
		try {
			in = new FileInputStream(txtFileName);

			int bits_count = 0;
			boolean first_run = true;
			BitSet bits_out = new BitSet(8);
			FileOutputStream output = new FileOutputStream(encFileName);
			while (true) {
				try {
					int c = in.read();
					if (c < 0) {
						if (bits_out.length() == 0) {
//							System.out.println("Bits length: " + bits_out.size());
							byte[] b = { 0 };
							output.write(b, 0, 1);
						} else
							output.write(bits_out.toByteArray());
						output.flush();
						bits_out.clear();
						bits_count = 0;
//						}
						output.close();
						break;
					}
					String s_bits = findHuffmanValue(root, (char) c, "");
//					System.out.println((char) c + ":" + s_bits);

//					for each of the 1,0 in the s_bits, add them to the bitset
//					when it gets to 8, write & flush
					for (int j = 0; j < s_bits.length(); j++, bits_count++) {
						if (bits_count % 8 == 0 && !first_run) {
//							System.out.println(":::" + bits_out);
							if (bits_out.length() == 0)
								output.write(0);
							else
								output.write(bits_out.toByteArray());
							output.flush();
							bits_out.clear();
							bits_count = 0;
						}

						if (s_bits.charAt(j) == '0') {
							bits_out.set(bits_count, false);
						} else {
							bits_out.set(bits_count, true);
						}
						first_run = false;
					}
				} catch (IOException e) {
					System.out.println("IO EXCEPTION!!");
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void printHuffmanTree(Node n, String bits) {
		if (n.leftNode == null && n.rightNode == null) {
			System.out.println(n.value + " = " + bits);
		}
		if (n.leftNode != null) {
			printHuffmanTree(n.leftNode, bits + "0");
		}
		if (n.rightNode != null) {
			printHuffmanTree(n.rightNode, bits + "1");
		}
	}

	public static void sortArray(ArrayList<Node> nodes) {
		for (int i = 0; i < nodes.size() - 1; i++) {
			for (int j = 0; j < nodes.size() - 1; j++) {
				if (nodes.get(j).freq > nodes.get(j + 1).freq) {
					Node curr = nodes.get(j);
					Node next = nodes.get(j + 1);
					nodes.set(j, next);
					nodes.set(j + 1, curr);
				}
			}
		}
		nodes.add(new Node(nodes.remove(0), nodes.remove(0)));
	}

	public static Hashtable<Character, Integer> findFrequency() {
		Hashtable<Character, Integer> ht = new Hashtable<Character, Integer>();
		try {
			int c;
			FileInputStream in = new FileInputStream(txtFileName);

			while (true) {
				try {
					c = in.read();
					if (c < 0)
						break;
					if (ht.get((char) c) == null)
						ht.put((char) c, 1);
					else
						ht.replace((char) c, ht.get((char) c) + 1);

				} catch (IOException e) {
//				error code goes here when an I/O exception occurs
					System.out.println("IO EXCEPTION!!");
				} catch (NullPointerException e) {
					// Error code goes here when a null pointer is being
					// accessed.

					// for example when you're trying to output the length
					// of a null String
					System.err.println("NULL POINTER ERROR OCCURRED");
				}
			}
			return ht;
		} catch (FileNotFoundException e) {
			// Error code goes here when
			System.err.println("FILE NOT FOUND");
			return ht;
		}
	}

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		ArrayList<Node> nodes_list = new ArrayList<Node>();
		System.out.println("Generating frequencies...");
		Hashtable<Character, Integer> ht = findFrequency();
		System.out.println((System.currentTimeMillis() - timeStart) + " MS");
		System.out.println("Found frequencies...");

		ht.forEach((k, v) -> {
//			System.out.println(k + " " + v);
			nodes_list.add(new Node(k, v));
		});
		while (nodes_list.size() > 1)
			sortArray(nodes_list);
//		created tree
		Node leaf = nodes_list.get(0);
//		printHuffmanTree(leaf, "");
		System.out.println("Seralizing..");
		Encode.seralize(leaf);
		System.out.println("Writing bits...");
		Encode.binaryOutput(leaf);
		long timeEnd = System.currentTimeMillis();
		System.out.println((timeEnd - timeStart) + " MS");
	}
}
