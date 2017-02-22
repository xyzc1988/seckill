package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhangcheng on 2016/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSecKillList() throws Exception {
        List<Seckill> seckills = seckillService.getSecKillList();
        logger.info("list={}", seckills);
    }

    @Test
    public void testGetById() throws Exception {
        Seckill seckill = seckillService.getById(1);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
        Exposer exposer = seckillService.exportSeckillUrl(1);
        logger.info(exposer.toString());
    }

    /*
    * Exposer{exposed=false, md5='92b2f12f478bd51b88d3e4dcd2b06497', seckillId=1, now=0, start=0, end=0}
    * */
    @Test
    public void testExecuteSeckill() throws Exception {
        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = seckillService.executeSeckill(1, 18653873931L,
                    "92b2f12f478bd51b88d3e4dcd2b06497");
            logger.info(seckillExecution.toString());
        } catch (SeckillCloseException e) {
            logger.info(e.getMessage());
        } catch (RepeatKillException e) {
            logger.info(e.getMessage());
        }
    }
    //注意集成测试业务覆盖的完整性
    @Test
    public void testSeckillLogic() {
        Exposer exposer = seckillService.exportSeckillUrl(1);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = null;
            try {
                seckillExecution = seckillService.executeSeckill(1, 18653873931L, md5);
                logger.info(seckillExecution.toString());
            } catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            }
        } else {
            logger.info("秒杀未开启");
        }

    }
    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1;
        long phone = 1212131209;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution =  seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(seckillExecution.getStateInfo());
        }
    }
}