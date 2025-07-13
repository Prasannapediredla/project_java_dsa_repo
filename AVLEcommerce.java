import java.util.Scanner;

class Product {
    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: ₹" + price;
    }
}

class AVLTree {

    class Node {
        Product product;
        int height;
        Node left, right;

        Node(Product product) {
            this.product = product;
            this.height = 1;
        }
    }

    Node Root;

    int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    int getBalance(Node node) {
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }

    void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    Node balance(Node node) {
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        if (balance < -1) {
            if (getBalance(node.left) > 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        return node;
    }

    Node insert(Node root, Product product) {
        if (root == null)
            return new Node(product);

        if (product.price < root.product.price)
            root.left = insert(root.left, product);
        else if (product.price > root.product.price)
            root.right = insert(root.right, product);
        else
            System.out.println("Product with same price exists (₹" + product.price + ")");

        return balance(root);
    }

    Node findMin(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    Node delete(Node root, double price) {
        if (root == null)
            return null;

        if (price < root.product.price)
            root.left = delete(root.left, price);
        else if (price > root.product.price)
            root.right = delete(root.right, price);
        else {
            if (root.left == null)
                return root.right;
            if (root.right == null)
                return root.left;

            Node temp = findMin(root.right);
            root.product = temp.product;
            root.right = delete(root.right, temp.product.price);
        }

        return balance(root);
    }

    Product search(Node root, double price) {
        if (root == null)
            return null;

        if (price == root.product.price)
            return root.product;
        else if (price < root.product.price)
            return search(root.left, price);
        else
            return search(root.right, price);
    }

    void inOrder(Node root) {
        if (root != null) {
            inOrder(root.left);
            System.out.println(root.product);
            inOrder(root.right);
        }
    }

    // Public interfaces
    void addProduct(Product product) {
        Root = insert(Root, product);
    }

    void deleteProduct(double price) {
        Root = delete(Root, price);
    }

    Product findProduct(double price) {
        return search(Root, price);
    }

    void showAllProducts() {
        if (Root == null)
            System.out.println("No products available.");
        else
            inOrder(Root);
    }
}

public class AVLEcommerce {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Product");
            System.out.println("2. Delete Product by Price");
            System.out.println("3. Search Product by Price");
            System.out.println("4. Show All Products (Sorted)");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    tree.addProduct(new Product(id, name, price));
                    break;

                case 2:
                    System.out.print("Enter Price of product to delete: ");
                    price = sc.nextDouble();
                    tree.deleteProduct(price);
                    System.out.println("If product existed, it's removed.");
                    break;

                case 3:
                    System.out.print("Enter Price to search: ");
                    price = sc.nextDouble();
                    Product p = tree.findProduct(price);
                    if (p != null)
                        System.out.println("Product Found: " + p);
                    else
                        System.out.println("Product not found.");
                    break;

                case 4:
                    tree.showAllProducts();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
