package com.kubadziworski.parsing.visitor.expression.function;

import com.google.common.collect.Lists;
import com.kubadziworski.antlr.EnkelParser.ParameterContext;
import com.kubadziworski.antlr.EnkelParser.ParameterWithDefaultValueContext;
import com.kubadziworski.antlr.EnkelParser.ParametersListContext;
import com.kubadziworski.antlr.EnkelParserBaseVisitor;
import com.kubadziworski.domain.node.expression.Parameter;
import com.kubadziworski.domain.scope.Scope;
import com.kubadziworski.parsing.visitor.expression.ExpressionVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuba on 09.05.16.
 */
public class ParameterExpressionListVisitor extends EnkelParserBaseVisitor<List<Parameter>> {

    private final ExpressionVisitor expressionVisitor;
    private final Scope scope;

    public ParameterExpressionListVisitor(ExpressionVisitor expressionVisitor, Scope scope) {
        this.expressionVisitor = expressionVisitor;
        this.scope = scope;
    }

    @Override
    public List<Parameter> visitParametersList(@NotNull ParametersListContext ctx) {
        List<ParameterContext> paramsCtx = ctx.parameter();
        ParameterExpressionVisitor parameterExpressionVisitor = new ParameterExpressionVisitor(expressionVisitor, scope);
        List<Parameter> parameters = new ArrayList<>();
        if(paramsCtx != null) {
            List<Parameter> params = Lists.transform(paramsCtx, p -> p.accept(parameterExpressionVisitor));
            parameters.addAll(params);
        }
        List<ParameterWithDefaultValueContext> paramsWithDefaultValueCtx = ctx.parameterWithDefaultValue();
        if(paramsWithDefaultValueCtx != null) {
            List<Parameter> params = Lists.transform(paramsWithDefaultValueCtx, p -> p.accept(parameterExpressionVisitor));
            parameters.addAll(params);
        }
        return parameters;
    }

}

