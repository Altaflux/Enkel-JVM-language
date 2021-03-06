package com.kubadziworski.parsing.visitor.statement;

import com.kubadziworski.antlr.EnkelParser.ReturnVoidContext;
import com.kubadziworski.antlr.EnkelParser.ReturnWithValueContext;
import com.kubadziworski.antlr.EnkelParserBaseVisitor;
import com.kubadziworski.domain.node.RuleContextElementImpl;
import com.kubadziworski.domain.node.expression.EmptyExpression;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.node.statement.ReturnStatement;
import com.kubadziworski.domain.type.intrinsic.VoidType;
import com.kubadziworski.parsing.visitor.expression.ExpressionVisitor;

public class ReturnStatementVisitor extends EnkelParserBaseVisitor<ReturnStatement> {
    private final ExpressionVisitor expressionVisitor;

    public ReturnStatementVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public ReturnStatement visitReturnVoid(ReturnVoidContext ctx) {
        return new ReturnStatement(new RuleContextElementImpl(ctx), new EmptyExpression(VoidType.INSTANCE));
    }

    @Override
    public ReturnStatement visitReturnWithValue(ReturnWithValueContext ctx) {
        Expression expression = ctx.expression().accept(expressionVisitor);
        return new ReturnStatement(new RuleContextElementImpl(ctx), expression);
    }
}