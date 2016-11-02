package com.kubadziworski.domain.node.statement;


import com.kubadziworski.bytecodegeneration.statement.StatementGenerator;
import com.kubadziworski.domain.node.expression.Expression;
import com.kubadziworski.domain.type.Type;

/**
 * Created by kuba on 28.03.16.
 */
public class VariableDeclaration implements Statement {
    private final String name;
    private final Expression expression;
    private final Type variableType;

    public VariableDeclaration(String name, Expression expression, Type variableType) {
        this.expression = expression;
        this.name = name;
        this.variableType = variableType;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }
}
