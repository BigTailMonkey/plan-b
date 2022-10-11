package com.btm.planb.swaggerenum;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApiModelEnumFieldBuildPlugin implements ModelPropertyBuilderPlugin {

    private static final String DESC = "description";

    @Override
    public void apply(ModelPropertyContext modelPropertyContext) {
        Optional<BeanPropertyDefinition> beanPropertyDefinition = modelPropertyContext.getBeanPropertyDefinition();
        if (!beanPropertyDefinition.isPresent()) {
            return;
        }
        AnnotatedField field = beanPropertyDefinition.get().getField();
        if (field.hasAnnotation(ApiModelEnumField.class)) {
            addDescForEnum(modelPropertyContext, field.getAnnotation(ApiModelEnumField.class));
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
    /**
     * 在使用ApiModelFieldEnum注解的字段的说明信息中加入注解信息
     * @param modelPropertyContext 模型字段解析上下文
     * @param apiModelFieldEnum 枚举信息解析注解
     */
    private void addDescForEnum(ModelPropertyContext modelPropertyContext, ApiModelEnumField apiModelFieldEnum) {
        //获得字段上的指定注解
        final String enumValue = apiModelFieldEnum.value();
        final String enumDesc = apiModelFieldEnum.desc();
        final Class<? extends Enum> enumClass = apiModelFieldEnum.type();

        Enum[] enumConstants = enumClass.getEnumConstants();
        if (Objects.isNull(enumConstants) || 0 >= enumConstants.length) {
            //若指定的枚举类中未定义任何内容，则直接结束解析过程
            return;
        }
        //按照enumValue & enumDesc 提取枚举的值与说明信息
        List<String> displayValues =
                Arrays.stream(enumConstants)
                        .filter(Objects::nonNull)
                        .map(item -> {
                            Class<?> currentClass = item.getClass();
                            //提取枚举值
                            Field indexField = ReflectionUtils.findField(currentClass, enumValue);
                            ReflectionUtils.makeAccessible(indexField);
                            Object value = ReflectionUtils.getField(indexField, item);
                            //提取枚举说明
                            Field descField = ReflectionUtils.findField(currentClass, enumDesc);
                            ReflectionUtils.makeAccessible(descField);
                            Object desc = ReflectionUtils.getField(descField, item);

                            return value + "-" + desc;

                        }).collect(Collectors.toList());
        //获得当前解析的接口字段的说明信息
        ModelPropertyBuilder builder = modelPropertyContext.getBuilder();
        Field descField = ReflectionUtils.findField(builder.getClass(), DESC);
        ReflectionUtils.makeAccessible(descField);
        String newDesc = ReflectionUtils.getField(descField, builder)
                + " (" + String.join("; ", displayValues) + ")";
        //应用新的字段说明信息
        builder.description(newDesc);
    }
}
