import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class NoParentE extends Exception {
}

class NoLeftChildE extends Exception {
}

class NoRightChildE extends Exception {

}

public class BinaryHeap<E> {
    private int size;
    private ArrayList<Item<E>> elems;

    /*
     * Create an empty binary heap. You must initialize position 0 in the array to null. 
     */
    BinaryHeap(){
        elems = new ArrayList<>();
        elems.add(0, null);
        size = 0;
    }

    /*
     * Create a binary heap containing all the items in 'es' as
     * follows. First add all the items in 'es' to 'elems' starting
     * from position 1. Make sure that each item is aware of its own
     * position by calling setPosition on each item as it is
     * inserted. After that initial loop, call moveDown on the first
     * half of the array. 
     * 
     */
    BinaryHeap(ArrayList<Item<E>> es){
        elems = new ArrayList<>();
        size = es.size();
        elems.add(0, null);
        for(int i = 0; i < es.size(); i++){
            es.get(i).setPosition(i+1);
            elems.add(es.get(i));
        }
        for(int i = size/2; i >0; i--){
            moveDown(i);
        }
    }

    /*
     * Checks if the heap is empty.
     */
    boolean isEmpty() {
        return size == 0;
    }

    /*
     * Returns the number of elements in the heap. 
     */
    int getSize() {
        return size;
    }

    /*
     * Returns (but does not remove) the mininum element in the
     * heap. Assume the heap is not empty.
     */
    Item<E> findMin() {
        return elems.get(1);
    }

    /*
     * Returns the actual items in the heap (exclusing position 0). 
     */
    List<Item<E>> getElems() {
        return elems.subList(1, size+1);
    }

    /*
     * Calculates the index of the parent of the item at position 'i'
     * or throws an exception if we are at the root of the binary
     * heap.
     */
    int getParentIndex(int i) throws NoParentE {
        int index = (i/2);
        if(i == 1){
            throw new NoParentE();
        }
        else return index;
    }

    /*
     * Calculates the index of the left child of the item at position
     * 'i' or throws an exception if no such child exists.
     */
    int getLeftChildIndex(int i) throws NoLeftChildE {
        int index = (2 * i);
        if(index < elems.size()){
            return index;
        }
        else throw new NoLeftChildE();
    }

    /*
     * Calculates the index of the right child of the item at position
     * 'i' or throws an exception if no such child exists.
     */
    int getRightChildIndex(int i) throws NoRightChildE {
        int index = (2 * i + 1);
        if(index < elems.size()){
            return index;
        }
        else throw new NoRightChildE();
    }

    /*
     * Swaps the items at positions 'i' and 'j' making sure that each
     * item is aware of its new position.
     */
    void swap(int i, int j) {
        Item<E> temp = elems.get(j);
        int ipos = elems.get(i).getPosition();
        int jpos = elems.get(j).getPosition();
        elems.get(j).setPosition(ipos);
        elems.get(i).setPosition(jpos);
        elems.set(j, elems.get(i));
        elems.set(i, temp);
    }

    /*
     * Calls getValue() on the element at position 'i'.
     */
    int getKey(int i) {
        return elems.get(i).getValue();
    }

    /*
     * You can assume that the give value will be less than the
     * current value of the item at position 'i'. The update might
     * result in the item having to move up.
     */
    void updateKey(int i, int value){
        try{
        elems.get(i).setValue(value);
        if(elems.get(getParentIndex(i)).getValue() > elems.get(i).getValue()){
            moveUp(i);
        }}
        catch(NoParentE e){

        }
    }

    /*
     * Recursively move the item up to ensure the order property of
     * the heap is maintained.
     */
    void moveUp(int i){
        try{
        int index = i;
        int parentIndex = getParentIndex(i);
        if(elems.get(parentIndex).getValue() > elems.get(index).getValue()){
            swap(index, parentIndex);
            moveUp(parentIndex); }
        }
        catch(NoParentE e){

        }
    }

    /*
     * Inserts the give item in the heap.
     */
    void insert(Item<E> ek){
        elems.add(ek);
        size++;
        ek.setPosition(size);
        moveUp(size);

    }

    /*
     * Calculates the index of the smallest child. In case of a tie, return the right child. 
     */
    int minChildIndex(int i) throws NoLeftChildE{
        int leftChild = elems.get(getLeftChildIndex(i)).getValue();
        try{
        int rightChild = elems.get(getRightChildIndex(i)).getValue();
        if (leftChild < rightChild) {
            return getLeftChildIndex(i);
        }
        else return getRightChildIndex(i);}
        catch(NoRightChildE e){
            return getLeftChildIndex(i);
        }
    }

    /*
     * Recursively moves the item down to maintain the order property of the heap.
     */
    void moveDown(int i){
        try{
            int smallestIndex = minChildIndex(i);
            if(elems.get(i).getValue() > elems.get(smallestIndex).getValue() ){
                swap(i, smallestIndex);
                moveDown(smallestIndex);
            }
        }
        catch(NoLeftChildE e){

        }
    }

    /*
     * Deletes and returns the mininum element in the heap.
     */
    Item<E> extractMin(){
        Item<E> smallest = getElems().get(0);
        elems.set(1, elems.get(size));
        elems.get(1).setPosition(1);
        elems.remove(size);
        size = size -1;
        moveDown(1);
        return smallest;
    }

    public String toString() {
        return getElems().toString();
    }

}
