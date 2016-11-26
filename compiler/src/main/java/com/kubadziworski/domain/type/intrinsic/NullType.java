package com.kubadziworski.domain.type.intrinsic;

import com.kubadziworski.domain.scope.Field;
import com.kubadziworski.domain.scope.FunctionSignature;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.domain.type.TypeSpecificOpcodes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public final class NullType implements Type {

    public static NullType INSTANCE = new NullType();

    private NullType() {

    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public Class<?> getTypeClass() {
        return null;
    }

    @Override
    public String getDescriptor() {
        return null;
    }

    @Override
    public String getInternalName() {
        return null;
    }

    @Override
    public Optional<Type> getSuperType() {
        return Optional.empty();
    }

    public List<Field> getFields() {
        return Collections.emptyList();
    }

    public List<FunctionSignature> getFunctionSignatures() {
        return Collections.emptyList();
    }

    @Override
    public int inheritsFrom(Type type) {
        if(!type.isNullable().equals(Nullability.NOT_NULL)){
            return -1;
        }

        if (type.isPrimitive()) {
            return -1;
        }
        return 0;
    }

    @Override
    public Optional<Type> nearestDenominator(Type type) {
        return Optional.empty();
    }

    @Override
    public int getDupCode() {
        return TypeSpecificOpcodes.OBJECT.getDupCode();
    }

    @Override
    public int getDupX1Code() {
        return TypeSpecificOpcodes.OBJECT.getDupX1Code();
    }

    @Override
    public int getLoadVariableOpcode() {
        return TypeSpecificOpcodes.OBJECT.getLoad();
    }

    @Override
    public int getStoreVariableOpcode() {
        return TypeSpecificOpcodes.OBJECT.getStore();
    }

    @Override
    public int getReturnOpcode() {
        return TypeSpecificOpcodes.OBJECT.getReturn();
    }

    @Override
    public int getAddOpcode() {
        return 0;
    }

    @Override
    public int getSubstractOpcode() {
        return 0;
    }

    @Override
    public int getMultiplyOpcode() {
        return 0;
    }

    @Override
    public int getDividOpcode() {
        return 0;
    }

    @Override
    public int getNegation() {
        return 0;
    }

    @Override
    public int getStackSize() {
        return 1;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Nullability isNullable() {
        return Nullability.NULLABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof TypeProjection) {
            o = ((TypeProjection) o).getInternalType();
        }
        return o instanceof Type && getName().equals(((Type) o).getName());
    }

}
