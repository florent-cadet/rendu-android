package android.eservices.rendu.presentation.viewmodel;

public class Event<T> {
    private boolean hasBeenHandled;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    /**
     * Get the content of the event if it has not been handled
     * @return null if it has been handled, else the content of the event
     */
    public T getContentIfHasntBeenHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }
}
