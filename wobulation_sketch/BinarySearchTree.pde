// Java program to demonstrate insert operation in binary search tree 
class BinarySearchTree<T> { 
  
    /* Class containing left and right child of current node and key value*/
    class Node<T> { 
        int key; 
        Node left, right; 
        T payload;
        
        public Node(int item) { 
            key = item; 
            left = right = null; 
        }
        
        public T getPayload() {
          return this.payload;
        }
        
        public T setPayload(T payload) {
          T temp = this.payload;
          this.payload = payload;
          return temp;
        }
          
    } 
  
    // Root of BST 
    Node<T> root; 
  
    // Constructor 
    BinarySearchTree() {  
        root = null;  
    } 
  
    // This method mainly calls insertRec() 
    void insert(int key) { 
       root = insertRec(root, key); 
    } 
      
    /* A recursive function to insert a new key in BST */
    Node insertRec(Node root, int key) { 
  
        /* If the tree is empty, return a new node */
        if (root == null) { 
            root = new Node(key); 
            return root; 
        } 
  
        /* Otherwise, recur down the tree */
        if (key < root.key) 
            root.left = insertRec(root.left, key); 
        else if (key > root.key) 
            root.right = insertRec(root.right, key); 
  
        /* return the (unchanged) node pointer */
        return root; 
    } 
  
    // This method mainly calls InorderRec() 
    void inorder()  { 
       inorderRec(root); 
    } 
  
    // A utility function to do inorder traversal of BST 
    void inorderRec(Node root) { 
        if (root != null) { 
            inorderRec(root.left); 
            System.out.println(root.key); 
            inorderRec(root.right); 
        } 
    } 
}
