package util;

// Java program to demonstrate insert operation in binary search tree 
class BinarySearchTree<T> {

    /* Class containing left and right child of current node and key value*/
    static class Node<T> {

        private final int key;
        private Node left, right;
        private T payload;

        public Node(int item) {
            key = item;
            left = right = null;
        }

        public Node(int key, T payload) {
            this.key = key;
            this.payload = payload;
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
    Node insertRec(Node currentNode, int key) {

        /* If the tree is empty, return a new node */
        if (currentNode == null) {
            currentNode = new Node(key);
            return currentNode;
        }

        /* Otherwise, recur down the tree */
        if (key < currentNode.key) {
            currentNode.left = insertRec(currentNode.left, key);
        } else if (key > currentNode.key) {
            currentNode.right = insertRec(currentNode.right, key);
        }

        /* return the (unchanged) node pointer */
        return currentNode;
    }

    // This method mainly calls InorderRec() 
    void inorder(Visitor<T> v) {
        inOrderTraverse(root, v);
    }

    // A utility function to do inorder traversal of BST 
    void inOrderTraverse(Node root, Visitor v) {
        if (root != null) {
            inOrderTraverse(root.left, v);
            v.visit(root);
            inOrderTraverse(root.right, v);
        }
    }

    void reverseInOrderTraverse(Node root, Visitor v) {
        if (root != null) {
            reverseInOrderTraverse(root.right, v);
            v.visit(root);
            reverseInOrderTraverse(root.left, v);
        }
    }

    void preOrderTraverse(Node root, Visitor v) {
        if (root != null) {
            v.visit(root);
            preOrderTraverse(root.left, v);
            preOrderTraverse(root.right, v);
        }
    }

    void postOrderTraverse(Node root, Visitor v) {
        if (root != null) {
            postOrderTraverse(root.left, v);
            postOrderTraverse(root.right, v);
            v.visit(root);
        }
    }

    interface Visitor<T> {

        void visit(Node<T> node);
    }

    public static void main(String[] args) {
        BinarySearchTree<Void> tree = new BinarySearchTree<>();

        /* Let us create following BST 
              50 
           /     \ 
          30      70 
         /  \    /  \ 
       20   40  60   80 */
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // print inorder traversal of the BST 
        tree.inorder(new Visitor<Void>() {
            public void visit(Node<Void> node) {
                System.out.println(node.key);
            }
        });
    }

}
