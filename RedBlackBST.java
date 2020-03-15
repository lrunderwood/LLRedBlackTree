import java.util.*;
import java.io.*;




/**
 * RedBlackBST class
 *
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> {

	private static final boolean RED   = true;
	private static final boolean BLACK = false;
	Node root;     // root of the BST

	/*************************************************************************
	 *  Node Class and methods - DO NOT MODIFY
	 *************************************************************************/
	public class Node {
		Key key;           // key
		Value val;         // associated data
		Node left, right;  // links to left and right subtrees
		boolean color;     // color of parent link
		int N;             // subtree count

		public Node(Key key, Value val, boolean color, int N) {
			this.key = key;
			this.val = val;
			this.color = color;
			this.N = N;
		}
	}

	// is node x red; false if x is null ?
	private boolean isRed(Node x) {
		if (x == null) return false;
		return (x.color == RED);
	}

	// number of node in subtree rooted at x; 0 if x is null
	private int size(Node x) {
		if (x == null) return 0;
		return x.N;
	}

	// return number of key-value pairs in this symbol table
	public int size() { return size(root); }

	// is this symbol table empty?
	public boolean isEmpty() {
		return root == null;
	}

	public RedBlackBST() {
		this.root = null;
	}

	/*************************************************************************
	 *  Modification Functions
	 *************************************************************************/

	// insert the key-value pair; overwrite the old value with the new value
	// if the key is already present
	public void insert(Key key, Value val) {
		//TODO
        root = insertNode(root, key, val);
        root.color = false;
	}
    private Node insertNode(Node searchNode, Key key, Value val){
        //check if there is another node with same key and replace
        //System.out.println(searchNode);
	    if(searchNode == null){
	        return new Node(key, val, true, 1);
        }
        //root.color = false;
        //System.out.println(searchNode.key);
	    if(key.compareTo(searchNode.key) == 0){
	        //System.out.println("Same key");
            searchNode.val = val;
        }
	    else if(key.compareTo(searchNode.key) > 0){
            searchNode.right = insertNode(searchNode.right, key, val);
        }
        else if(key.compareTo(searchNode.key) < 0){
            searchNode.left = insertNode(searchNode.left, key, val);
        }
        else{
	        return searchNode;
        }

        //check cases
        searchNode = balance(searchNode);

        return searchNode;
    }

	// delete the key-value pair with the given key
	public void delete(Key key) {
		//TODO
		if(key != null && searchNode(root, key) != null){
			root = deleteNode(root, key);
		}

	}
	private Node deleteNode(Node deleteNode, Key key){
		//tree is empty or branch ends
	    if(deleteNode == null)
	        return deleteNode;

        if(key.compareTo(deleteNode.key) < 0){
            if(deleteNode.left != null && deleteNode.left.color == false && deleteNode.left.left != null && deleteNode.left.left.color == false){
                deleteNode = moveRedLeft(deleteNode);
            }
            deleteNode.left = deleteNode(deleteNode.left, key);
        }
	    else if(key.compareTo(deleteNode.key) > 0){
	    	//if left node exists and red then rotate to the right
	        if(deleteNode.left != null && deleteNode.left.color == true){
	            deleteNode = rotateRight(deleteNode);
            }
            //if right and let node exist and are both black
            if(deleteNode.right != null && deleteNode.right.color == false && deleteNode.right.left != null && deleteNode.right.left.color == false){
	            deleteNode = moveRedRight(deleteNode);
            }
            //move down tree
            deleteNode.right = deleteNode(deleteNode.right, key);
        }

        else{ //case where key is equal
				//if end of branch is reached (both children are null) then return null
                if(deleteNode.right == null && deleteNode.left == null){
                    return null;
                }

                //case with two children
                if(deleteNode.right != null && deleteNode.left != null){
                    deleteNode.val = search(findMinKey(deleteNode.right));
                    deleteNode.key = findMinKey(deleteNode.right);
                    deleteNode.right = deleteMin(deleteNode.right);
                    //deleteNode.color = false;
                }
                //case with only right child
                else if(deleteNode.right != null) { //if already at right subtree
                    return deleteNode.right;
                }
                //case with only left child
                else if(deleteNode.left != null){
                    return deleteNode.left;
                }

            //deleteNode.right = deleteNode(deleteNode.right, deleteNode.key);
        }


        return balance(deleteNode);

	}
	private Key findMinKey(Node node){
        Key minKey = node.key;

        while(node.left != null){
             minKey = node.left.key;
             node = node.left;
        }
        return minKey;
    }
    private Node deleteMin(Node node){
	    //if left node is red then rotate right
        /*
	    if(node.left != null && node.left.color == true){
	        node = rotateRight(node);
        }
        */
        //reached end of branch

        if(node.left == null){
	        return null;
        }

        //left and right node exist and are both black then ro
        if(node.left != null && node.left.color == false && node.left.left != null && node.left.left.color == false) {
           node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
	    return balance(node);
    }

	/*************************************************************************
	 *  Search Functions
	 *************************************************************************/

	// value associated with the given key; null if no such key
	public Value search(Key key) { 
		//TODO
        if(searchNode(root, key) == null){
            return null;
        }
        if(key != null){
            return searchNode(root, key).val;
        }
		return null;
	}
	private Node searchNode(Node node, Key key){
	    if(node != null) {
            if (key.compareTo(node.key) == 0) {
                //System.out.println(node.val);
                return node;
            }
            if (key.compareTo(node.key) < 0)
                return searchNode(node.left, key);
            else
                return searchNode(node.right, key);
        }
        return node;
    }

	// is there a key-value pair with the given key?
	public boolean contains(Key key) {
		return (search(key) != null);
	}



	/*************************************************************************
	 *  Utility functions
	 *************************************************************************/

	// height of tree (1-node tree has height 0)
	public int height() { return height(root); }
	private int height(Node x) {
		if (x == null) return -1;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	/*************************************************************************
	 *  Rank Methods
	 *************************************************************************/

	// the key of rank k
    private int rank = 0;
	public Key getValByRank(int k) {
		//TODO
        if(k <= root.N) {
            rank = 0;
            try {
                return getValByRank(root, k).key;
            } catch(Exception e){
                return null;
            }
        }

		return null;
	}

	private Node getValByRank(Node node, int k){
        /*if(node != null) {
            if (k == node.N) {
                //System.out.println(node.val);
                return node;
            }
            if(node.left != null && k < node.left.N)
                return getValByRank(node.left, k);
            else
                return getValByRank(node.right, k - size(node.left) - 1);
        }
        return null;
        */
        Node traverseNode = null;
       if(node.left != null) {
           traverseNode = getValByRank(node.left, k);
       }

       if(traverseNode != null)
           return traverseNode;
       if (rank == k)
           return node;

       rank++;
       //System.out.println(rank);
       if(node.right != null) {
           traverseNode = getValByRank(node.right, k);
       }

       return traverseNode;
    }

	// number of keys less than key
    private int count = 0;
	public int rank(Key key) {
		//TODO
        if(key != null) {
            count = 0;
            return rank(root, key);
        }
		return 0;
	}
	private int rank(Node node, Key key){
        if(node.left != null) {
            rank(node.left, key);
        }
        if(key.compareTo(node.key) > 0)
            count++;
        //System.out.println(rank);
        if(node.right != null) {
            rank(node.right, key);
        }

        return count;
    }

	/***********************************************************************
	 *  Range count and range search.
	 ***********************************************************************/

	public List<Key> getElements(int a, int b){
		//TODO
        //what constitutes as invalid range?
        if(root == null || a < 0 || b < 0 || a > b || b >= root.N)
            return new ArrayList<>();

        List<Key> list = new ArrayList<>();

        getElements(root, a, b, list);

		return list;
	}
	private void getElements(Node node, int a, int b, List<Key> list){

            if (node.left != null) {
                getElements(node.left, a, b, list);
            }
            if (rank(node.key) <= b && rank(node.key) >= a) {
                //add key to list
                list.add(node.key);
            }
            if (node.right != null) {
                getElements(node.right, a, b, list);
            }

    }

	/*************************************************************************
	 *  red-black tree helper functions
	 *************************************************************************/

	// make a left-leaning link lean to the right
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		return x;
	}

	// make a right-leaning link lean to the left
	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		return x;
	}

	// flip the colors of a node and its two children
	private void flipColors(Node h) {
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	// Assuming that h is red and both h.left and h.left.left
	// are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h) {
		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
		}
		return h;
	}

	// Assuming that h is red and both h.right and h.right.left
	// are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {		
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node balance(Node h) {
		// assert (h != null);

		if (isRed(h.right))                      h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))     flipColors(h);

		h.N = size(h.left) + size(h.right) + 1;
		return h;
	}

    
    
    
    
    /*************************************************************************
     *  The Main Function
        Use this for testing
     *************************************************************************/
    public static void main(String[] args) {
        
        Scanner readerTest = null;

        try {
            //Change File name to test other test files.
            readerTest = new Scanner(new File(args[0])); //new File(args[0])
        } catch (IOException e) {
            System.out.println("Reading Oops");
        }
        
        RedBlackBST<Integer, Integer> test = new RedBlackBST<>();
        
        while(readerTest.hasNextLine()){
           String[] input  =readerTest.nextLine().split(" ");
           
           for(String x: input){
               System.out.print(x+" ");
           }
            
           System.out.println();
           switch (input[0]){
               case "insert":
                   Integer key = Integer.parseInt(input[1]);
                   Integer val = Integer.parseInt(input[2]);                 
                   test.insert(key,val);
                   printTree(test.root);
                   System.out.println();
                   break;
                   
               case "delete":
                    Integer key1 = Integer.parseInt(input[1]);
                    test.delete(key1);
                    printTree(test.root);
                    System.out.println();
                    break;
                   
               case "search":
                    Integer key2 = Integer.parseInt(input[1]);
                    Integer ans2 = test.search(key2);                    
                    System.out.println(ans2);
                    System.out.println();
                    break;   
                   
               case "getval":
                    Integer key3 = Integer.parseInt(input[1]);
                    Integer ans21 = test.getValByRank(key3);
                    System.out.println(ans21);
                    System.out.println();
                    break;
                   
               case "rank":
                    Integer key4 = Integer.parseInt(input[1]);
                    Object ans22 = test.rank(key4);
                    System.out.println(ans22);
                    System.out.println();
                    break;   
                   
               case "getelement":
                    Integer low = Integer.parseInt(input[1]);
                    Integer high = Integer.parseInt(input[2]);
                    List<Integer> testList = test.getElements(low,high);
                   
                    for(Integer list : testList){
                        System.out.println(list);    
                    }
                   
                    break;
               
               default:
                    System.out.println("Error, Invalid test instruction! "+input[0]);    
           }
        }
        
    }    
    
    
    /*************************************************************************
     *  Prints the tree
     *************************************************************************/
    public static void printTree(RedBlackBST.Node node){
        
	    if (node == null){
		    return;
	    }
	   
	    printTree(node.left);
	    System.out.print(((node.color == true)? "Color: Red; ":"Color: Black; ") + "Key: " + node.key + " Value: " + node.val + "\n");
        printTree(node.right);
    }
}