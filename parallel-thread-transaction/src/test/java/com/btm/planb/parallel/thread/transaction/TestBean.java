package com.btm.planb.parallel.thread.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.*;

public class TestBean {

    private final Logger logger = LoggerFactory.getLogger(TestBean.class);

    private JdbcTemplate jdbcTemplate;

    private ThreadPoolExecutor taskExecutor;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TestBean() {
        this.taskExecutor = new ThreadPoolExecutor(16, 16,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000000),
                r -> new Thread(r, "task executor"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 事务测试方法
     *
     * @param name         测试参数
     * @param subThreadNumber 要开启的子线程的数量，实际开启的子线程数量多一个
     * @param mainRollback 是否在主线程发生异常从而引起事务回滚
     * @param subRollback  是否在子主线程发生异常从而引起事务回滚
     * @throws ExecutionException
     */
    @Transactional
    public void transaction(String name, int subThreadNumber, boolean mainRollback, boolean subRollback) throws ExecutionException {
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        logger.debug("当前事务名称:{}", currentTransactionName);
        SpringTransactionThreadManager<String> manager = new SpringTransactionThreadManager<>(taskExecutor);
        for (int i = 0;i < subThreadNumber;i++) {
            manager.addBeforeCommit(z -> {
                        logger.info("other logic by method 'addBeforeCommit'..........{}", z);
                        jdbcTemplate.query("select count(1) from test_table", (rs, rowNum) -> {
                            logger.info("query table count :{}", rs.getInt(1));
                            return rs.getInt(1);
                        });
                        return z;
                    },
                    a -> jdbcTemplate.update("insert into test_table(name) values (?)", a));
        }
        manager.add(a -> {
            logger.info("other logic by method 'add'..........{}", a);
            jdbcTemplate.query("select count(1) from test_table_2", (rs, rowNum) -> {
                logger.info("query table count :{}", rs.getInt(1));
                return rs.getInt(1);
            });
            return new TransactionSynchronizationAdapter() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    if (readOnly) {
                        throw new TransactionException("") {
                            @Override
                            public String getMessage() {
                                return "wanna repository data use a readonly transaction";
                            }
                        };
                    }
                    if (subRollback) {
                        logger.error("sub transaction thread exception, {}", a);
                        throw new RuntimeException("rollback by logic");
                    } else {
                        logger.debug("add new sub transaction thread.");
                        jdbcTemplate.update("insert into test_table_2(name) values (?)", a);
                    }
                }
            };
        });
        if (mainRollback) {
            logger.error("main transaction thread exception, {}", name);
            throw new RuntimeException("rollback by logic");
        } else {
            jdbcTemplate.update("insert into test_table_main(name) values (?)", name);
        }
        manager.addAction(name);
    }
}
