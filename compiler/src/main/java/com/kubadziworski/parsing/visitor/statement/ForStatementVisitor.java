package com.kubadziworski.parsing.visitor.statement;

import com.kubadziworski.antlr.EnkelParser.ForConditionsContext;
import com.kubadziworski.antlr.EnkelParser.ForStatementContext;
import com.kubadziworski.antlr.EnkelParser.VariableReferenceContext;
import com.kubadziworski.antlr.EnkelParserBaseVisitor;
import com.kubadziworski.domain.node.RuleContextElementImpl;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.node.statement.Assignment;
import com.kubadziworski.domain.node.statement.RangedForStatement;
import com.kubadziworski.domain.node.statement.Statement;
import com.kubadziworski.domain.node.statement.VariableDeclaration;
import com.kubadziworski.domain.scope.FunctionScope;
import com.kubadziworski.domain.scope.LocalVariable;
import com.kubadziworski.parsing.visitor.expression.ExpressionVisitor;

/**
 * Created by kuba on 23.04.16.
 */
public class ForStatementVisitor extends EnkelParserBaseVisitor<RangedForStatement> {
    private final FunctionScope scope;
    private final ExpressionVisitor expressionVisitor;

    public ForStatementVisitor(FunctionScope scope) {
        this.scope = scope;
        expressionVisitor = new ExpressionVisitor(this.scope);
    }

    @Override
    public RangedForStatement visitForStatement(ForStatementContext ctx) {
        FunctionScope newScope = new FunctionScope(scope);
        ForConditionsContext forExpressionContext = ctx.forConditions();
        Expression startExpression = forExpressionContext.startExpr.accept(expressionVisitor);
        Expression endExpression = forExpressionContext.endExpr.accept(expressionVisitor);
        VariableReferenceContext iterator = forExpressionContext.iterator;
        StatementVisitor statementVisitor = new StatementVisitor(newScope);
        String varName = iterator.getText();
        if (newScope.isLocalVariableExists(varName)) {
            Statement iteratorVariable = new Assignment(newScope.getLocalVariable(varName), startExpression);
            Statement statement = ctx.statement().accept(statementVisitor);
            return new RangedForStatement(new RuleContextElementImpl(ctx), iteratorVariable, startExpression, endExpression, statement, varName, newScope);
        } else {
            LocalVariable localVariable = new LocalVariable(varName, startExpression.getType());
            newScope.addLocalVariable(new LocalVariable(varName, startExpression.getType()));
            Statement iteratorVariable = new VariableDeclaration(localVariable, startExpression);
            Statement statement = ctx.statement().accept(statementVisitor);
            return new RangedForStatement(new RuleContextElementImpl(ctx), iteratorVariable, startExpression, endExpression, statement, varName, newScope);
        }
    }

}
