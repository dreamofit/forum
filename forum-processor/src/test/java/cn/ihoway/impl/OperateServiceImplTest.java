package cn.ihoway.impl;

import cn.ihoway.entity.Operate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperateServiceImplTest {

    OperateServiceImpl service = new OperateServiceImpl();

    @Test
    void addSupport() {
    }

    @Test
    void operateDaoTest(){
        List<Operate> operate = service.selectAll();
        for (Operate o : operate){
            System.out.println(o.toString());
        }

    }
}