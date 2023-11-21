package doublylinkedlist;


public class DoublyLinkedList<K> {
    private final Node<K> head;
    private final Node<K> tail;

    public DoublyLinkedList() {
        this.head = new Node<>();
        this.tail = new Node<>();
        head.setNext(tail);
        tail.setPrev(head);
    }

    public void append(final K value) {
        Node<K> prev = tail.getPrev();
        Node<K> node = Node.<K>builder()
                .value(value)
                .next(tail)
                .prev(prev)
                .build();

        prev.setNext(node);
        node.setPrev(prev);
        node.setNext(tail);
        tail.setPrev(node);
    }

    public void delete(final Node<K> node) {
        Node<K> prev = node.getPrev();
        Node<K> next = node.getNext();

        prev.setNext(next);
        next.setPrev(prev);
    }

    public Node<K> end() {
        return tail;
    }

    public Node<K> begin() {
        return head;
    }
}
