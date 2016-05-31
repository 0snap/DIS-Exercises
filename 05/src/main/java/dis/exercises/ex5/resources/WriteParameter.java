package dis.exercises.ex5.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WriteParameter {

    @JsonProperty
    private String transactionId;

    @JsonProperty
    private Integer pageId;

    @JsonProperty
    private String data;

    public WriteParameter() {    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
