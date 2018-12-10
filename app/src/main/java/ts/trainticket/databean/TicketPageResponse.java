package ts.trainticket.databean;

import java.util.List;

/**
 * Created by liuZOZO on 2018/1/19.
 */
public class TicketPageResponse {

    private boolean status;
    private String msg;
    private int currentPageNum;
    private int pageSize;
    private int totalPageNum;
    private int totalCount;
    private List<Ticket> ticketList;

    public TicketPageResponse() {
    }

    public TicketPageResponse(boolean status, String msg, List<Ticket> ticketList) {
        this.status = status;
        this.msg = msg;
        this.ticketList = ticketList;
    }

    public TicketPageResponse(boolean status, String msg, int pageSize, int currentPageNum, int totalPageNum, int totalCount, List<Ticket> ticketList) {
        this.status = status;
        this.msg = msg;
        this.pageSize = pageSize;
        this.currentPageNum = currentPageNum;
        this.totalPageNum = totalPageNum;
        this.totalCount = totalCount;
        this.ticketList = ticketList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
}
