package com.kubadziworski.parsing.visitor;

import com.kubadziworski.antlr.EnkelBaseVisitor;
import com.kubadziworski.antlr.EnkelParser.FunctionDeclarationContext;
import com.kubadziworski.antlr.EnkelParser.ParametersListContext;
import com.kubadziworski.domain.node.expression.Parameter;
import com.kubadziworski.domain.scope.Scope;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.domain.scope.FunctionSignature;
import com.kubadziworski.util.TypeResolver;
import com.kubadziworski.parsing.visitor.expression.ExpressionVisitor;
import com.kubadziworski.parsing.visitor.expression.function.ParameterExpressionListVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/**
 * Created by kuba on 06.04.16.
 */
public class FunctionSignatureVisitor extends EnkelBaseVisitor<FunctionSignature> {

    private final ExpressionVisitor expressionVisitor;
    private final Scope scope;

    public FunctionSignatureVisitor(Scope scope) {
        this.expressionVisitor = new ExpressionVisitor(scope);
        this.scope = scope;
    }

    @Override
    public FunctionSignature visitFunctionDeclaration(@NotNull FunctionDeclarationContext ctx) {
        String functionName = ctx.functionName().getText();
        Type returnType = TypeResolver.getFromTypeContext(ctx.type());
        ParametersListContext parametersCtx = ctx.parametersList();


        int modifiers = ctx.methodModifiers().stream().map(methodModifiersContext -> {
            if (methodModifiersContext.getText().equals("static")) {
                return Modifier.STATIC;
            }
            return 0;
        }).mapToInt(Integer::intValue).sum();

        //TODO SET CORRECTLY MODIFIERS
        if (parametersCtx != null) {
            List<Parameter> parameters = parametersCtx.accept(new ParameterExpressionListVisitor(expressionVisitor));
            return new FunctionSignature(functionName, parameters, returnType, Modifier.PUBLIC + modifiers, scope.getClassType());
        }
        return new FunctionSignature(functionName, Collections.emptyList(), returnType, Modifier.PUBLIC + modifiers, scope.getClassType());

    }
}
