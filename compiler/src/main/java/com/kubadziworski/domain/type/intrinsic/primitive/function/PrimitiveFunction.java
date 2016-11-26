package com.kubadziworski.domain.type.intrinsic.primitive.function;

import com.kubadziworski.bytecodegeneration.statement.StatementGenerator;
import com.kubadziworski.domain.CompareSign;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.type.intrinsic.primitive.PrimitiveTypes;
import com.kubadziworski.util.PrimitiveTypesWrapperFactory;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;


public class PrimitiveFunction {

    public static void compareFloat(CompareSign compareSign, MethodVisitor methodVisitor) {
        callCompareFloatOrDouble(compareSign, methodVisitor, Type.FLOAT_TYPE);
    }

    private static void callCompareFloatOrDouble(CompareSign compareSign, MethodVisitor methodVisitor, Type type) {
        switch (compareSign) {
            case LESS: {
                methodVisitor.visitInsn(type == Type.FLOAT_TYPE ? Opcodes.FCMPG : Opcodes.DCMPG);
                break;
            }
            case LESS_OR_EQUAL: {
                methodVisitor.visitInsn(type == Type.FLOAT_TYPE ? Opcodes.FCMPG : Opcodes.DCMPG);
                break;
            }
            default: {
                methodVisitor.visitInsn(type == Type.FLOAT_TYPE ? Opcodes.FCMPL : Opcodes.DCMPL);
                break;
            }
        }
        callCompareSign(compareSign, methodVisitor);
    }

    public static void compareDouble(CompareSign compareSign, MethodVisitor methodVisitor) {
        callCompareFloatOrDouble(compareSign, methodVisitor, Type.DOUBLE_TYPE);
    }

    public static void compareLong(CompareSign compareSign, MethodVisitor methodVisitor) {
        methodVisitor.visitInsn(Opcodes.LCMP);
        callCompareSign(compareSign, methodVisitor);
    }


    private static void callCompareSign(CompareSign compareSign, MethodVisitor methodVisitor) {
        Label label = new Label();
        Label label2 = new Label();

        methodVisitor.visitInsn(Opcodes.LCMP);
        switch (compareSign) {
            case LESS: {
                methodVisitor.visitJumpInsn(Opcodes.IFGE, label);
                break;
            }
            case LESS_OR_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IFGT, label);
                break;
            }
            case GREATER: {
                methodVisitor.visitJumpInsn(Opcodes.IFLE, label);
                break;
            }
            case GRATER_OR_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IFLT, label);
                break;
            }
            case EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IFNE, label);
                break;
            }
            case NOT_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
                break;
            }
        }
        methodVisitor.visitInsn(Opcodes.ICONST_1);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label2);
        methodVisitor.visitLabel(label);
        methodVisitor.visitInsn(Opcodes.ICONST_0);
        methodVisitor.visitLabel(label2);
    }


    public static void compareIntType(CompareSign compareSign, MethodVisitor methodVisitor) {

        Label label = new Label();
        Label label2 = new Label();

        switch (compareSign) {
            case LESS: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, label);
                break;
            }
            case LESS_OR_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, label);
                break;
            }
            case GREATER: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, label);
                break;
            }
            case GRATER_OR_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, label);
                break;
            }
            case EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, label);
                break;
            }
            case NOT_EQUAL: {
                methodVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, label);
                break;
            }
        }
        methodVisitor.visitInsn(Opcodes.ICONST_1);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label2);
        methodVisitor.visitLabel(label);
        methodVisitor.visitInsn(Opcodes.ICONST_0);
        methodVisitor.visitLabel(label2);
    }

    public static void compareBoolType(CompareSign compareSign, MethodVisitor methodVisitor) {
        compareIntType(compareSign, methodVisitor);
    }
    ///////////////////////////////

    public static void toInt(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.INT_TYPE, value.getType(), ad);
    }

    public static void toLong(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.LONG_TYPE, value.getType(), ad);
    }

    public static void toFloat(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.FLOAT_TYPE, value.getType(), ad);
    }

    public static void toDouble(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.FLOAT_TYPE, value.getType(), ad);
    }

    public static void toChar(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.CHAR_TYPE, value.getType(), ad);
    }

    public static void toByte(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.BYTE_TYPE, value.getType(), ad);
    }

    public static void toShort(Expression value, MethodVisitor v, StatementGenerator generator) {
        InstructionAdapter ad = new InstructionAdapter(v);
        value.accept(generator);
        PrimitiveTypesWrapperFactory.coerce(PrimitiveTypes.SHORT_TYPE, value.getType(), ad);
    }


}