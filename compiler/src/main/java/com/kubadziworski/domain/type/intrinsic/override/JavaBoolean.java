package com.kubadziworski.domain.type.intrinsic.override;

import com.kubadziworski.domain.Modifier;
import com.kubadziworski.domain.Modifiers;
import com.kubadziworski.domain.node.expression.Parameter;
import com.kubadziworski.domain.node.expression.function.SignatureType;
import com.kubadziworski.domain.scope.FunctionSignature;
import com.kubadziworski.domain.type.JavaClassType;
import com.kubadziworski.domain.type.intrinsic.AnyType;
import com.kubadziworski.domain.type.intrinsic.primitive.PrimitiveTypes;
import org.apache.commons.collections4.ListUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JavaBoolean extends JavaClassType {

    private final List<FunctionSignature> functionSignatures;

    public JavaBoolean() {

        super(java.lang.Boolean.class);


        Parameter parameter = new Parameter("o", PrimitiveTypes.BOOLEAN_TYPE, null);

        FunctionSignature compareTo = new FunctionSignature("compareTo",
                Collections.singletonList(parameter), PrimitiveTypes.INT_TYPE, Modifiers.empty().with(Modifier.PUBLIC), this,
                SignatureType.FUNCTION_CALL);
        FunctionSignature booleanValue = new FunctionSignature("booleanValue",
                Collections.emptyList(), PrimitiveTypes.BOOLEAN_TYPE, Modifiers.empty().with(Modifier.PUBLIC), this,
                SignatureType.FUNCTION_CALL);

        functionSignatures = ListUtils.sum(AnyType.INSTANCE.getFunctionSignatures(), Arrays.asList(compareTo, booleanValue));
    }


    @Override
    public List<FunctionSignature> getFunctionSignatures() {
        return functionSignatures;
    }
}
