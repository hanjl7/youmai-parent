package com.youmai.pojogroup;

import com.youmai.pojo.TbSpecification;
import com.youmai.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: Specification
 * @Description: 规格组合实体类
 * @Author: 泊松
 * @Date: 2018/9/13 15:09
 * @Version: 1.0
 */
public class Specification  implements Serializable {
    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptionList;


    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
