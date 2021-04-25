package com.yeqifu.bus.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yeqifu.bus.domain.AjaxResult;
import com.yeqifu.bus.domain.Car;
import com.yeqifu.bus.domain.Customer;
import com.yeqifu.bus.domain.FlowTable;
import com.yeqifu.bus.mapper.CarMapper;
import com.yeqifu.bus.vo.CarVo;
import com.yeqifu.sys.utils.DataGridView;
import com.yeqifu.sys.utils.WebUtils;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 行为记录
 */
@RestController
@RequestMapping("flowTable")
public class FlowTableController {


    @Autowired(required = false)
    private CarMapper carMapper;

    public DataModel createDataModel(List<FlowTable> selecter) {
        if (CollectionUtils.isEmpty(selecter))
            return null;
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
        Map<Integer, List<FlowTable>> groupBy = selecter.stream().
                collect(Collectors.groupingBy(FlowTable::getUserId));

        Set<Map.Entry<Integer, List<FlowTable>>> entries = groupBy.entrySet();

        Iterator<Map.Entry<Integer, List<FlowTable>>> iterator = entries.iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<Integer, List<FlowTable>> next = iterator.next();
            List<FlowTable> value = next.getValue();
            if (!CollectionUtil.isEmpty(value)) {
                PreferenceArray var = new GenericUserPreferenceArray(
                        value.size() + 100);
                int index = 0;
                for (FlowTable flowTable : value) {
                    var.setUserID(index, next.getKey());
                    var.setItemID(index, flowTable.getCarId());
                    Double sourceCount = flowTable.getSourceCount();
                    var.setValue(index, sourceCount == null ? new Float("0") :
                            sourceCount.floatValue());
                    index++;
                }
                preferences.put(next.getKey(), var);
            }

        }
        return new GenericDataModel(preferences);
    }

    //兜底推荐 访问过的
    public List<Car> getDefaultCar(Customer user, Integer RECOMMENDER_NUM) {
        FlowTable flowTable = new FlowTable();
        flowTable.setUserId(user.getId());
        List<FlowTable> selecter = carMapper.selecterFlow(flowTable);
        if (!CollectionUtil.isEmpty(selecter)) {
            List<Integer> carid =
                    selecter.stream().map(getId -> getId.getCarId()).collect(Collectors.toList());
            List<Integer> subList =
                    RECOMMENDER_NUM > carid.size() ? carid : carid.subList(0, RECOMMENDER_NUM);

            return carMapper.batchSelect(subList);
        }
        return null;
    }

    // new DataGridView(page.getTotal(), data);
    public DataGridView getDefaultResult(Customer customer) {
        List<Car> defaultCar = getDefaultCar(customer, 3);
        return new DataGridView(CollectionUtils.isEmpty(defaultCar) ?
                0 : Long.valueOf(defaultCar.size()), defaultCar);
    }

    @GetMapping("getCars")
    public DataGridView getCars(CarVo carVo) {
        final Integer RECOMMENDER_NUM = 15;
        Customer user = (Customer) WebUtils.getHttpSession().getAttribute("userGuestv2");
        try {

            if (user == null)
                return getDefaultResult(user);// guest ignore
            List<FlowTable> selecter = carMapper.selecterFlow(new FlowTable());
            for (FlowTable f:selecter
                 ) {
                System.out.println(f.getCarId()+f.getId()+f.getSourceCount());
            }
            DataModel dataModel = createDataModel(selecter);
            //createMenrot
            ItemSimilarity item = new EuclideanDistanceSimilarity(dataModel);

            Recommender r = new GenericItemBasedRecommender(dataModel, item);
            Integer id = user.getId();
            //查看训练库有没有存在
            FlowTable flowTable = new FlowTable();
            flowTable.setUserId(user.getId());
            List<FlowTable> judgeExist = carMapper.selecterFlow(flowTable);
            if (CollectionUtil.isEmpty(judgeExist)) {
                return new DataGridView(0L, null);
            }
            List<RecommendedItem> list = r.recommend(user.getId(), RECOMMENDER_NUM);  //获取推荐结果
            if (CollectionUtil.isEmpty(list))
                return getDefaultResult(user);// guest ignore
            else {
                List<Integer> recommand =
                        list.stream().map(getRecommand -> getRecommand.getItemID()).map(hit -> hit.intValue())
                                .collect(Collectors.toList());
                if (recommand == null)
                    return getDefaultResult(user);// guest ignore
                Page<Object> page = PageHelper.startPage(carVo.getPage(), carVo.getLimit());
                List<Car> cars = carMapper.batchSelect(recommand);
                return new DataGridView(page.getTotal(), cars);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getDefaultResult(user);// guest ignore

    }


    /**
     * 初始化添加出租单的表单的数据
     *
     * @return
     */
    @PostMapping("save")
    public AjaxResult initRentFrom(@RequestParam(name = "currentCar") String currentCarId) {
        try {
            if (com.github.pagehelper.util.StringUtil.isEmpty(currentCarId))
                return AjaxResult.error();
            synchronized (lock) {
                //设置操作员
                // Customer customer = new Customer();
                // customer.setId(999);
                //WebUtils.getHttpSession().setAttribute("userGuest", customer);
                Customer user = (Customer) WebUtils.getHttpSession().getAttribute("userGuestv2");
                if (user == null)
                    return AjaxResult.success();
                Integer userid = user.getId();
                // Integer icurrentCar = Integer.valueOf(currentCar);
                CarVo carVo = new CarVo();
                carVo.setId(Integer.valueOf(currentCarId));
                //  List<Car> cars = carMapper.queryAllCar(carVo);
                FlowTable flowTable = new FlowTable();
                flowTable.setUserId(userid);
                flowTable.setCarId(Integer.valueOf(currentCarId));
                // hit  user hobby
                Double aDouble = new Double(1);
                List<Car> cars = carMapper.queryAllCar(new CarVo());
                System.out.println(cars);
                List<FlowTable> selecter = carMapper.selecterFlow(flowTable);
                if (CollectionUtil.isEmpty(selecter)) {
                    flowTable.setSourceCount(aDouble);
                    carMapper.insertFlow(flowTable);
                } else {
                    FlowTable flowTableVar1 = selecter.get(0);
                    Double sourceCount = flowTableVar1.getSourceCount();
                    flowTableVar1.setSourceCount(sourceCount + aDouble);
                    carMapper.updateHobbyCount(flowTableVar1);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            //ignore exception
        }
        return AjaxResult.success();
    }

    private final byte lock[] = new byte[0];

}
