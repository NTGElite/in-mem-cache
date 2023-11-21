package doublylinkedlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Node<K> {
    private Node<K> prev;
    private Node<K> next;
    private K value;

    public Node() {
        this.prev = this.next = null;
        this.value = null;
    }
}
