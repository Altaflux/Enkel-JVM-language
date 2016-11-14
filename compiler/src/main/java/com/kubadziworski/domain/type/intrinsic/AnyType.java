package com.kubadziworski.domain.type.intrinsic;


import com.kubadziworski.domain.node.expression.Parameter;
import com.kubadziworski.domain.scope.Field;
import com.kubadziworski.domain.scope.FunctionSignature;
import com.kubadziworski.domain.type.BuiltInType;
import com.kubadziworski.domain.type.JavaClassType;
import com.kubadziworski.domain.type.Type;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnyType implements Type {

    private final Type objectClass = new JavaClassType("java.lang.Object");
    private final List<FunctionSignature> functionSignatures;

    public static AnyType INSTANCE = new AnyType();

    private AnyType() {

        Parameter parameter = new Parameter("other", this, null);
        FunctionSignature equalsSignature = new FunctionSignature("equals", Collections.singletonList(parameter),
                BuiltInType.BOOLEAN, Modifier.PUBLIC, this);
        FunctionSignature constructorSignature = new FunctionSignature("Any", Collections.emptyList(),
                this, Modifier.PUBLIC, this);
        FunctionSignature toString = new FunctionSignature("toString", Collections.emptyList(),
               UnitType.INSTANCE, Modifier.PUBLIC, this);
        FunctionSignature hashCode = new FunctionSignature("hashCode", Collections.emptyList(),
                BuiltInType.INT, Modifier.PUBLIC, this);

        functionSignatures = Arrays.asList(equalsSignature, constructorSignature, toString, hashCode);
    }



    @Override
    public String getName() {
        return "radium.Any";
    }

    @Override
    public Class<?> getTypeClass() {
        return objectClass.getTypeClass();
    }

    @Override
    public String getDescriptor() {
        return objectClass.getDescriptor();
    }

    @Override
    public String getInternalName() {
        return objectClass.getInternalName();
    }

    @Override
    public Optional<Type> getSuperType() {
        return objectClass.getSuperType();
    }

    @Override
    public List<Field> getFields() {
        return Collections.emptyList();
    }

    @Override
    public List<FunctionSignature> getFunctionSignatures() {
        return functionSignatures;
    }

    @Override
    public int inheritsFrom(Type type) {
        return objectClass.inheritsFrom(type);
    }

    @Override
    public Optional<Type> nearestDenominator(Type type) {
        return objectClass.nearestDenominator(type);
    }

    @Override
    public int getDupCode() {
        return objectClass.getDupCode();
    }

    @Override
    public int getDupX1Code() {
        return objectClass.getDupX1Code();
    }

    @Override
    public int getLoadVariableOpcode() {
        return objectClass.getLoadVariableOpcode();
    }

    @Override
    public int getStoreVariableOpcode() {
        return objectClass.getStoreVariableOpcode();
    }

    @Override
    public int getReturnOpcode() {
        return objectClass.getReturnOpcode();
    }

    @Override
    public int getAddOpcode() {
        return objectClass.getAddOpcode();
    }

    @Override
    public int getSubstractOpcode() {
        return objectClass.getSubstractOpcode();
    }

    @Override
    public int getMultiplyOpcode() {
        return objectClass.getMultiplyOpcode();
    }

    @Override
    public int getDividOpcode() {
        return objectClass.getDividOpcode();
    }

    @Override
    public int getNegation() {
        return objectClass.getNegation();
    }

    @Override
    public int getStackSize() {
        return objectClass.getStackSize();
    }

    @Override
    public boolean isPrimitive() {
        return objectClass.isPrimitive();
    }
}