package cn.nankai.edu.cn;

public class PageBean {
    private int currentPage;
    private int pageSize = 10;
    private int totalCount;
    private int totalPage;
    private int startIndex;

    public PageBean() {
    }

    public PageBean(int currentPage, int totalCount) {
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        calculateTotalPage();
        calculateStartIndex();
    }

    public PageBean(int currentPage, int pageSize, int totalCount) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        calculateTotalPage();
        calculateStartIndex();
    }

    private void calculateTotalPage() {
        if (totalCount % pageSize == 0) {
            this.totalPage = totalCount / pageSize;
        } else {
            this.totalPage = totalCount / pageSize + 1;
        }
        if (this.totalPage == 0) {
            this.totalPage = 1;
        }
    }

    private void calculateStartIndex() {
        this.startIndex = (currentPage - 1) * pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if (this.currentPage < 1) {
            this.currentPage = 1;
        }
        if (this.totalPage > 0 && this.currentPage > this.totalPage) {
            this.currentPage = this.totalPage;
        }
        calculateStartIndex();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        calculateTotalPage();
        calculateStartIndex();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calculateTotalPage();
        if (this.currentPage > this.totalPage && this.totalPage > 0) {
            this.currentPage = this.totalPage;
        }
        calculateStartIndex();
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public boolean isHasPrevious() {
        return currentPage > 1;
    }

    public boolean isHasNext() {
        return currentPage < totalPage;
    }

    public int getPreviousPage() {
        return currentPage > 1 ? currentPage - 1 : 1;
    }

    public int getNextPage() {
        return currentPage < totalPage ? currentPage + 1 : totalPage;
    }
}
