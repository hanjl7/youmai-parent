package entity;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PageResult
 * @Description: 分页结果类
 * @Author: 泊松
 * @Date: 2018/9/10 18:45
 * @Version: 1.0
 */
public class PageResult implements Serializable {
    //总记录数
    private Long total;
    //当前页记录
    private List rows;

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
