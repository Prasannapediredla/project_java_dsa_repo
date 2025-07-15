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
        return "ID: " + id + ", Name: " + name + ", Price: Rs. " + price;
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
        if (root == null) return new Node(product);

        if (product.price < root.product.price)
            root.left = insert(root.left, product);
        else if (product.price > root.product.price)
            root.right = insert(root.right, product);
        else
            System.out.println("Product with same price exists (Rs. " + product.price + ")");

        return balance(root);
    }

    Node findMin(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    Node deleteById(Node root, int id) {
        if (root == null) return null;

        if (id < root.product.id)
            root.left = deleteById(root.left, id);
        else if (id > root.product.id)
            root.right = deleteById(root.right, id);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            Node temp = findMin(root.right);
            root.product = temp.product;
            root.right = deleteById(root.right, temp.product.id);
        }

        return balance(root);
    }

    Product searchByPrice(Node root, double price) {
        if (root == null) return null;

        if (Math.abs(price - root.product.price) < 0.0001) return root.product;
        else if (price < root.product.price) return searchByPrice(root.left, price);
        else return searchByPrice(root.right, price);
    }

    Product searchById(Node root, int id) {
        if (root == null) return null;

        if (id == root.product.id) return root.product;
        else {
            Product foundLeft = searchById(root.left, id);
            if (foundLeft != null) return foundLeft;
            return searchById(root.right, id);
        }
    }

    void inOrder(Node root) {
        if (root != null) {
            inOrder(root.left);
            System.out.println(root.product);
            inOrder(root.right);
        }
    }

    // Allowed products data for validation (id, name, price)
    private final Product[] allowedProducts = {
        new Product(101, "Laptop", 49999),
        new Product(102, "Smartphone", 29999),
        new Product(103, "Headphones", 1999)
    };

    // Check if product is allowed (based on id, name, price)
    private boolean isAllowed(Product p) {
        for (Product ap : allowedProducts) {
            if (ap.id == p.id && ap.name.equalsIgnoreCase(p.name) && Math.abs(ap.price - p.price) < 0.0001) {
                return true;
            }
        }
        return false;
    }

    // Modified addProduct to return boolean
    boolean addProduct(Product product) {
        if (!isAllowed(product)) {
            return false; // Product not allowed
        }
        Root = insert(Root, product);
        return true; // Product added successfully
    }

    void deleteProductById(int id) {
        Product existing = searchById(Root, id);
        if (existing == null) {
            System.out.println("Product with ID " + id + " not found.");
        } else {
            Root = deleteById(Root, id);
            System.out.println("Product with ID " + id + " deleted successfully.");
        }
    }

    Product findProductByPrice(double price) {
        return searchByPrice(Root, price);
    }

    void showAllProducts() {
        if (Root == null) System.out.println("No products available.");
        else inOrder(Root);
    }
}

public class AVLEcommerce {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        Scanner sc = new Scanner(System.in);

        // Add allowed products silently to AVL at start
        tree.addProduct(new Product(101, "Laptop", 49999));
        tree.addProduct(new Product(102, "Smartphone", 29999));
        tree.addProduct(new Product(103, "Headphones", 1999));

        while (true) {
            System.out.println("\n1. Add Product");
            System.out.println("2. Delete Product by ID");
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
                    boolean added = tree.addProduct(new Product(id, name, price));
                    if (!added) {
                        System.out.println("Product not found in database. Cannot add.");
                    }
                    // No success message printed
                    break;

                case 2:
                    System.out.print("Enter Product ID to delete: ");
                    id = sc.nextInt();
                    tree.deleteProductById(id);
                    break;

                case 3:
                    System.out.print("Enter Price to search: ");
                    double searchPrice = sc.nextDouble();
                    Product p = tree.findProductByPrice(searchPrice);
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
