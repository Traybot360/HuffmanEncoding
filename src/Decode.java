import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Decode {
	public static String decFileName = "File.decoded";
	public static String encFileName = "File.encoded";
	public static String serFileName = "Tree.ser";

	public static Node root = null;
	public static int freqs = 0;

	public static void deseralize() {
		try {
			FileInputStream file = new FileInputStream(serFileName);
			ObjectInputStream in = new ObjectInputStream(file);
			root = (Node) in.readObject();
			in.close();
			file.close();

		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
	}

	public static void huffmanDecode(Node root) {
		try {
			FileInputStream in = new FileInputStream(encFileName);
			FileOutputStream out = new FileOutputStream(decFileName);
			Node s = root;
			freqs = s.freq;

//			System.out.println("total chars: " + s.freq);

			while (freqs > 0) {
				int num = in.read();
//				System.out.println("test output: " + num);
//				System.out.println("c " + Integer.toBinaryString(num));
				if (num < 0)
					break;
				for (int i = 0; i < 8; i++) {
					if (freqs == 0)
						break;
					if (s.leftNode == null && s.rightNode == null) {
//						System.out.println(":::" + s.value);
						out.write(s.value);
						out.flush();
						s = root;
						freqs -= 1;
					}
//					System.out.println(num & 1);
					if (num % 2 == 0)
						s = s.leftNode;
					else
						s = s.rightNode;
					num /= 2;
				}
			}

			if (s.leftNode == null && s.rightNode == null) {
//				System.out.println(":::" + s.value);
				out.write(s.value);
				out.flush();
			}
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		System.out.println("Deseralizing...");
		deseralize();
		System.out.println("Starting decoding...");
		huffmanDecode(root);
		long timeEnd = System.currentTimeMillis();
		System.out.println((timeEnd - timeStart) + " MS");
	}
}