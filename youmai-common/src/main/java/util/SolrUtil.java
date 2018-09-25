package util;

import com.alibaba.fastjson.JSON;
import com.youmai.mapper.TbItemMapper;
import com.youmai.pojo.TbItem;
import com.youmai.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SolrUtil
 * @Description: 创建类SolrUtil ,实现商品数据的查询
 * @Author: 泊松
 * @Date: 2018/9/25 20:05
 * @Version: 1.0
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * @return void
     * @Description 导入商品列表
     * @Date 20:08 2018/9/25
     * @Param []
     **/
    public void importItemDate() {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //是否已审核
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);

        for (TbItem tbItem : itemList) {
            //将spec字段中的json字符串转换为map
            Map specMap = JSON.parseObject(tbItem.getSpec());
            //给带注解的字段赋值
            tbItem.setSpecMap(specMap);
        }

        /*批量导入*/
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();

    }


}
