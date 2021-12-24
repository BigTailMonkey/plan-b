package com.btm.planb.worklogstatistic.keyNode;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 项目生成周期的关键节点
 */
public class NodeChain {

    private static final INode header;

    static {
        header = new TechnicalCommunication(null,"沟通","技术沟通","技术交流");
        INode td = new TechnicalDesign(header, "设计","设计","开始设计","设计完成");
        INode develop = new Develop(td, "开发","开发中","研发中","开发完成");
        INode test = new Test(develop, "测试","已提测","测试中","提测","自测","联调");
        INode release = new Release(test, "上线","已上线","发布","已发布");
    }

    public INode findNode(String info, LocalDate localDate) {
        INode node = header.isArrived(info);
        if (Objects.nonNull(node)) {
            node.setKeyInfo(info);
            return node.isArrived(localDate);
        }
        return null;
    }
}
