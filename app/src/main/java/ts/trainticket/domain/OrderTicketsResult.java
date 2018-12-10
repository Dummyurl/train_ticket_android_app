package ts.trainticket.domain;

public class OrderTicketsResult {

    private boolean status;

    private String message;

    private Order order;

    public OrderTicketsResult(){
        //Default Constructor
    }

    public OrderTicketsResult(boolean status, String message, Order order) {
        this.status = status;
        this.message = message;
        this.order = order;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
