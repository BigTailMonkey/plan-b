package com.btm.planb.springtest.accelerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnterProcessor implements BeanFactoryPostProcessor, Ordered {

    private static final Logger log = LoggerFactory.getLogger(EnterProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Object o = configurableListableBeanFactory.getBeansWithAnnotation(SpringBootConfiguration.class).values().stream().findFirst().get();
        Set<String> scanBasePackage = findScanBasePackage(o);
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
        List<BeanDefinition> targetPackageBeanDefinition = new LinkedList<>();
        for (String name : beanDefinitionNames) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
            String beanClassName = beanDefinition.getBeanClassName();
            if (Objects.isNull(beanClassName) || beanClassName.equals(o.getClass().getName())) {
                continue;
            }
            String packageName = beanClassName.substring(0, beanClassName.lastIndexOf("."));
            if (scanBasePackage.contains(packageName)) {
                targetPackageBeanDefinition.add(beanDefinition);
            } else {
                for (String fatherPackage : scanBasePackage) {
                    if (packageName.startsWith(fatherPackage)) {
                        scanBasePackage.add(packageName);
                        targetPackageBeanDefinition.add(beanDefinition);
                        break;
                    }
                }
            }
        }
        log.info("bean factory post processor,{}", targetPackageBeanDefinition);
        for (BeanDefinition beanDefinition : targetPackageBeanDefinition) {
            if (beanDefinition instanceof ScannedGenericBeanDefinition) {
                beanDefinition.setLazyInit(true);
            }
        }
    }

    private Set<String> findScanBasePackage(Object o) {
        Set<String> scanBasePackage = new HashSet<>();
        // fixme 有待优化
//        if (AopUtils.isAopProxy(o) || AopUtils.isCglibProxy(o)) {
//            Class<?> originClass = AopUtils.getTargetClass(o);
//            ComponentScans componentScans = originClass.getAnnotation(ComponentScans.class);
//            if (Objects.nonNull(componentScans)) {
//                ComponentScan[] value = componentScans.value();
//                for (ComponentScan componentScan : value) {
//                    scanBasePackage.addAll(Arrays.asList(componentScan.value()));
//                }
//            }
//            ComponentScan componentScan = originClass.getAnnotation(ComponentScan.class);
//            if (Objects.nonNull(componentScan)) {
//                scanBasePackage.addAll(Arrays.asList(componentScan.value()));
//            }
//        }
        if (scanBasePackage.isEmpty()) {
            String className = o.getClass().getName();
            scanBasePackage.add(className.substring(0, className.lastIndexOf(".")));
        }
        removeThisPackage(scanBasePackage);
        return scanBasePackage;
    }

    private void removeThisPackage(Set<String> packages) {
        packages.remove("com.btm.planb.springtest.accelerator");
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
