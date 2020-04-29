import java.util.ArrayList;
import java.util.List;

public class Node {
    public List<Integer> values;
    public List<Node> nodes;

    public Node(Integer value, Node left, Node right) {
        values = new ArrayList<>(3);
        nodes = new ArrayList<>(3);
        values.add(value);
        nodes.add(left);
        nodes.add(right);
    }

    public Node(Integer leftValue, Integer rightValue) {
        values = new ArrayList<>(3);
        nodes = new ArrayList<>(3);
        values.add(leftValue < rightValue ? leftValue : rightValue);
        values.add(rightValue > leftValue ? rightValue : leftValue);
    }

    public Node(Integer value) {
        values = new ArrayList<>(3);
        nodes = new ArrayList<>(3);
        values.add(value);
    }

    public boolean add(Integer value) {
        int i = 0;
        while (i < values.size() && value > values.get(i))
            i++;
        if (i < values.size() && value.equals(values.get(i)))
            return false;
        if (isLeaf()) {
            values.add(i, value);
            return true;
        }
        boolean returnVal = nodes.get(i).add(value); //shift the add to the child node below it
        if (nodes.get(i).isFull()) {
            fixIfFull(i);
        }
        return returnVal;
    }


//        public boolean add(Integer value) {
//        if (isLeaf()) {
//            for (int i = 0; i < values.size(); i++) {
//                if (value.equals(values.get(i))) {
//                    return false;
//                } else if (value < values.get(i)) {
//                    values.add(i, value);
//                    return true;
//                }
//            }
//            values.add(value);
//            return true;
//        } else {
//            for (int i = 0; i <= values.size(); i++) {
//                if (i == values.size() || value < values.get(i)) {
//                    boolean returnVal = nodes.get(i).add(value); //shift the add to the child node below it
//                    if (nodes.get(i).isFull()) {
//                        fixIfFull(i);
//                    }
//                    return returnVal;
//                } else if (value.equals(values.get(i))) {
//                    return false;
//                }
//            }
//        }
//        return false;
//    }

    public void fixIfFull(int childIndex) {
        Node childFullNode = nodes.get(childIndex);
        //move the middle value from the 4-node child to its parent
        if (childIndex == 2) {
            values.add(nodes.get(childIndex).values.get(1));
        } else {
            values.add(childIndex, nodes.get(childIndex).values.get(1));
        }

        nodes.remove(childFullNode);
        Node[] split = childFullNode.split();
        nodes.add(childIndex, split[1]);
        nodes.add(childIndex, split[0]);
    }

    public Node[] split() {
        if (this.isLeaf()) {
            return new Node[]{
                    new Node(values.get(0)),
                    new Node(values.get(2))
            };
        } else {
            return new Node[]{
                    new Node(values.get(0), nodes.get(0), nodes.get(1)),
                    new Node(values.get(2), nodes.get(2), nodes.get(3))
            };
        }
    }

    public Node search(int value) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).equals(value) || this.isLeaf()) {
                return this;
            }
            if (value < values.get(i)) {
                return nodes.get(i).search(value);
            }
        }
        return nodes.get(nodes.size() - 1).search(value);
    }

    public boolean isLeaf() {
        return nodes.size() == 0;
    }

    public boolean isFull() {
        return values.size() == 3;
    }

    @Override
    public String toString() {
        if (values.size() == 0) {
            return "Nothing in node :(";
        } else if (values.size() == 1) {
            return values.get(0).toString();
        } else if (values.size() == 2) {
            return values.get(0).toString() + " " + values.get(1).toString();
        }
        return "Heck.";
    }
}
