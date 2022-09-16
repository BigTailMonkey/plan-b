package com.btm.planb.parallel.thread.transaction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@ContextConfiguration(value = {"classpath:JunitConfig.xml"})
public class SpringTransactionThreadManagerTest {

    private final Logger logger = LoggerFactory.getLogger(SpringTransactionThreadManagerTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestBean testBean;

    @Test
    public void transactionCommit() throws ExecutionException {
        String name = "name_test_" + new Random().nextInt(999999);
        Assert.assertNotNull(this.jdbcTemplate);
        Assert.assertNotNull(testBean);
        logger.debug("the transaction name is {}", TransactionSynchronizationManager.getCurrentTransactionName());
        testBean.transaction(name, 1, false, false);
        List<Db> query1 = this.jdbcTemplate.query("select * from test_table where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertNotNull(query1);
        Assert.assertEquals(1, query1.size());
        Assert.assertEquals(name, query1.get(0).getName());
        List<Db> query2 = this.jdbcTemplate.query("select * from test_table_2 where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertNotNull(query2);
        Assert.assertEquals(1, query2.size());
        Assert.assertEquals(name, query2.get(0).getName());
        List<Db> queryMain = this.jdbcTemplate.query("select * from test_table_main where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertNotNull(queryMain);
        Assert.assertEquals(1, queryMain.size());
        Assert.assertEquals(name, queryMain.get(0).getName());
    }


    @Test
    public void transactionMainRollback() {
        String name = "name_test_" + new Random().nextInt(999999);
        Assert.assertNotNull(this.jdbcTemplate);
        Assert.assertNotNull(testBean);
        try {
            testBean.transaction(name, 1,true, false);
        } catch (Exception e) {
            Assert.assertEquals("rollback by logic", e.getMessage());
        }
        List<Db> query1 = this.jdbcTemplate.query("select * from test_table where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(query1.isEmpty());
        List<Db> query2 = this.jdbcTemplate.query("select * from test_table_2 where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(query2.isEmpty());
        List<Db> queryMain = this.jdbcTemplate.query("select * from test_table_main where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(queryMain.isEmpty());
    }


    @Test
    public void transactionSubRollback() {
        String name = "name_test_" + new Random().nextInt(999999);
        Assert.assertNotNull(this.jdbcTemplate);
        Assert.assertNotNull(testBean);
        try {
            testBean.transaction(name, 1, false, true);
        } catch (Exception e) {
            Assert.assertEquals("rollback by logic", e.getMessage());
        }
        List<Db> query1 = this.jdbcTemplate.query("select * from test_table where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(query1.isEmpty());
        List<Db> query2 = this.jdbcTemplate.query("select * from test_table_2 where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(query2.isEmpty());
        List<Db> queryMain = this.jdbcTemplate.query("select * from test_table_main where name = ?",
                (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                name);
        Assert.assertTrue(queryMain.isEmpty());
    }

    /**
     * 并发测试<br/>
     * <p>开启100个线程提交任务，每个线程提交100此请求</p>
     * <p>被测试方法中使用单例的公用线程池，被测试方法中共有17个子线程任务</p>
     * <p>经过测试，耗时82s，且数据全部正确</p>
     * @throws InterruptedException
     */
    @Test
    public void parallelTest() throws InterruptedException {
        String pre = "name_parallel_test_";
        List<List<ParallelTestElement>> sourceElements = new ArrayList<>();
        int countNumber = 0;
        for (int k = 0;k<100;k++) {
            List<ParallelTestElement> sourceElement = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                ParallelTestElement element = new ParallelTestElement();
                element.name = pre + i;
                if (i % 66 == 0) {
                    element.mainException = true;
                }
                if (i % 77 == 0) {
                    element.subException = true;
                }
                if (!element.subException && !element.mainException) {
                    countNumber++;
                }
                sourceElement.add(element);
            }
            sourceElements.add(sourceElement);
        }
        CountDownLatch latch = new CountDownLatch(sourceElements.size());
        sourceElements.forEach( sourceElement -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Assert.assertTrue(sourceElement.stream().anyMatch(ParallelTestElement::isMainException));
                            Assert.assertTrue(sourceElement.stream().anyMatch(ParallelTestElement::isSubException));
                            sourceElement.forEach(element -> {
                                try {
                                    testBean.transaction(element.name, 16, element.mainException, element.subException);
                                    logger.debug("one batch:{}", element.name);
                                } catch (Exception e) {
                                    Assert.assertEquals("rollback by logic", e.getMessage());
                                }
                            });
                            sourceElement.forEach(element -> {
                                List<Db> query1 = jdbcTemplate.query("select * from test_table where name = ?",
                                        (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                                        element.name);
                                List<Db> query2 = jdbcTemplate.query("select * from test_table_2 where name = ?",
                                        (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                                        element.name);
                                List<Db> queryMain = jdbcTemplate.query("select * from test_table_main where name = ?",
                                        (rs, rowNum) -> new Db(rs.getLong("id"), rs.getString("name")),
                                        element.name);
                                if (element.isMainException() || element.isSubException()) {
                                    Assert.assertTrue(query1.isEmpty());
                                    Assert.assertTrue(query2.isEmpty());
                                    Assert.assertTrue(queryMain.isEmpty());
                                } else {
                                    Assert.assertFalse(query1.isEmpty());
                                    Assert.assertFalse(query2.isEmpty());
                                    Assert.assertFalse(queryMain.isEmpty());
                                    Assert.assertEquals(element.name, query1.get(0).getName());
                                    Assert.assertEquals(element.name, query2.get(0).getName());
                                    Assert.assertEquals(element.name, queryMain.get(0).getName());
                                }
                            });
                            latch.countDown();
                        }
                    }).start();
                }
        );
        latch.await();
        List<Integer> testTable2CountList = jdbcTemplate.query("select count(1) from test_table_2 where name like ?",
                (rs, rowNum) -> rs.getInt(1), "%" + pre + "%");
        Assert.assertNotNull(testTable2CountList);
        Assert.assertEquals(countNumber, (int) testTable2CountList.get(0));
    }

    private class ParallelTestElement {
        public String name;
        public boolean mainException = false;
        public boolean subException = false;

        public boolean isMainException() {
            return mainException;
        }

        public boolean isSubException() {
            return subException;
        }
    }
}