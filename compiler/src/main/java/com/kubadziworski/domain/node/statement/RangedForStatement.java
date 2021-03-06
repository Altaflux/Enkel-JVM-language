package com.kubadziworski.domain.node.statement;

import com.kubadziworski.bytecodegeneration.statement.StatementGenerator;
import com.kubadziworski.domain.node.ElementImpl;
import com.kubadziworski.domain.node.NodeData;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.scope.FunctionScope;
import com.kubadziworski.domain.type.Type;
import com.kubadziworski.exception.UnsupportedRangedLoopTypes;
import com.kubadziworski.util.TypeChecker;

/**
 * Created by kuba on 23.04.16.
 */
public class RangedForStatement extends ElementImpl implements Statement {
    private final Statement iteratorVariable;
    private final Expression startExpression;
    private final Expression endExpression;
    private final Statement statement;
    private final String iteratorVarName;
    private final FunctionScope scope;

    public RangedForStatement(NodeData element, Statement iteratorVariable, Expression startExpression, Expression endExpression, Statement statement, String iteratorVarName, FunctionScope scope) {
        super(element);
        this.scope = scope;
        Type startExpressionType = startExpression.getType();
        Type endExpressionType = endExpression.getType();
        boolean typesAreIntegers = TypeChecker.isInt(startExpressionType) || TypeChecker.isInt(endExpressionType);
        if(!typesAreIntegers) {
            throw new UnsupportedRangedLoopTypes(startExpression,endExpression);
        }
        this.iteratorVariable = iteratorVariable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.statement = statement;
        this.iteratorVarName = iteratorVarName;
    }

    public RangedForStatement(Statement iteratorVariable, Expression startExpression, Expression endExpression, Statement statement, String iteratorVarName, FunctionScope scope) {
        this(null, iteratorVariable, startExpression, endExpression,statement, iteratorVarName, scope);
    }

    public Statement getIteratorVariableStatement() {
        return iteratorVariable;
    }

    public Expression getStartExpression() {
        return startExpression;
    }

    public Expression getEndExpression() {
        return endExpression;
    }

    public Statement getStatement() {
        return statement;
    }

    public String getIteratorVarName() {
        return iteratorVarName;
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }

    public FunctionScope getScope() {
        return scope;
    }

    public Type getType() {
        return startExpression.getType();
    }
}
