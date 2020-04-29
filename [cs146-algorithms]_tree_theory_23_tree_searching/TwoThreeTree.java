public class TwoThreeTree {
    private Node root;

    public TwoThreeTree() {

    }

    public boolean insert(int value) {
        if (root == null) {
            root = new Node(value);
            return true;
        } else {
            boolean isNew = root.add(value);
            if (root.isFull()) {
                Node[] split = root.split();
                root = new Node(root.values.get(1), split[0], split[1]);
            }
            return isNew;
        }
    }

    public String search(int value) {
        return root.search(value).toString();
    }

}
