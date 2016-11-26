package com.kubadziworski.domain;

import com.kubadziworski.domain.type.Type;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;


public enum ArithmeticOperator {

    ADD("+", "plus", Opcodes.IADD), SUBTRACT("-", "minus", Opcodes.ISUB), DIVIDE("/", "div", Opcodes.IDIV), MULTIPLY("*", "times", Opcodes.IMUL);

    private final String operator;
    private final String methodName;
    private final int operationOpCode;

    ArithmeticOperator(String operator, String methodName, int opCode) {
        this.operator = operator;
        this.operationOpCode = opCode;
        this.methodName = methodName;
    }

    public int getOperationOpCode(Type type) {
        org.objectweb.asm.Type asmType = org.objectweb.asm.Type.getType(type.getDescriptor());
        return asmType.getOpcode(operationOpCode);
    }

    public String getMethodName() {
        return methodName;
    }

    public static ArithmeticOperator fromString(String sign) {
        return Arrays.stream(values()).filter(opSign -> opSign.operator.equals(sign))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sign not implemented"));
    }

}
