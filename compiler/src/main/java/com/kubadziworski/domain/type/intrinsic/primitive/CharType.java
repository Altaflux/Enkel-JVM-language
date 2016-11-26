package com.kubadziworski.domain.type.intrinsic.primitive;

import com.kubadziworski.domain.CompareSign;
import com.kubadziworski.domain.scope.Field;
import com.kubadziworski.domain.scope.FunctionSignature;
import com.kubadziworski.domain.type.BuiltInType;
import com.kubadziworski.domain.type.JavaClassType;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.domain.type.intrinsic.primitive.function.PrimitiveFunction;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class CharType extends AbstractPrimitiveType {


    private static final Type NULLABLE_TYPE = new JavaClassType("java.lang.Character");
    private static final Type PRIMITIVE_TYPE = BuiltInType.CHAR;
    private static final Type physicalType = new JavaClassType("radium.Char");

    public CharType(boolean primitive) {
        super(primitive ? PRIMITIVE_TYPE : NULLABLE_TYPE, primitive);
    }

    @Override
    public String getName() {
        return "radium.Char";
    }

    @Override
    public List<Field> getFields() {
        return physicalType.getFields();
    }

    @Override
    public List<FunctionSignature> getFunctionSignatures() {
        return physicalType.getFunctionSignatures();
    }

    @Override
    public Type getBoxedType() {
        return new CharType(false);
    }

    @Override
    public Type getUnBoxedType() {
        return new CharType(true);
    }

    @Override
    public void compare(CompareSign compareSign, MethodVisitor methodVisitor) {
        PrimitiveFunction.compareIntType(compareSign, methodVisitor);
    }
}